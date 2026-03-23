# Material Search Agent — Architecture

> **Domain:** Textile/Apparel Supply Chain · **Status:** Implemented · **Date:** March 2026

---

## 01 — Executive Summary

### From Faceted Filters to Conversational Discovery

Product designers and developers today discover materials through a digital materials management system backed by a relational database. The digital path relies on faceted navigation and keyword search — effective, but rigid. Designers must know the right filter combinations and attribute vocabulary to find what they need.

This project introduces a **Material Search Agent**: an AI-powered conversational layer that sits in front of existing material APIs. Rather than replacing the current system, the agent acts as a new access pattern — translating natural language queries into structured API calls, orchestrating multi-step searches, and returning contextualised results that include material attributes, product usage (BOM), and file attachments.

The architecture preserves all existing backend services and introduces no new data stores. The MCP server is stateless, tool-orchestrated, and the external AI client drives all reasoning.

**Core Principles:**

- **Augment, Don't Replace** — The agent adds a conversational interface to existing material APIs. Faceted search remains available in parallel.
- **Zero New Data Stores** — All queries resolve against the existing PostgreSQL database. No data duplication.
- **Authorization at the Tool Boundary** — Role-based access is enforced at each tool call, not at the prompt layer. The agent operates with the user's identity.
- **Grounded Responses Only** — The agent never invents material data. Every response is sourced from tool call results.

---

## 02 — Problem Domain

### Why Filters Aren't Enough

The materials catalogue spans multiple categories (fabrics, threads, buttons, zippers, etc.) with rich attribute sets: composition, colour, supplier reference, minimum stock, and BOM usage across product lines. The current filter-based interface requires users to already know the attribute vocabulary — a designer searching for "a cotton-blend fabric used in active jackets" must mentally translate that intent into the correct combination of category, composition filter, and product cross-reference.

### Identified Friction Points

| Friction | Current Behaviour | Desired Behaviour |
|---|---|---|
| **Vocabulary Gap** | Users must know exact filter values (e.g., category = FABRIC, composition = "60% cotton") | Natural language: "lightweight cotton blend fabric for outerwear" |
| **Multi-Step Discovery** | Finding a material, then separately checking which products already use it, requires switching views | "Show me recycled polyester fabrics used in active jackets" — agent chains `list_materials` → `get_products_by_material` automatically |
| **BOM Cross-Reference** | Checking effective material quantities (including waste) across BOM versions requires manual lookup | Agent can retrieve active BOM items with `effectiveQuantity` in a single conversational turn |
| **Attachment Discovery** | Locating spec sheets or certificates for a material requires navigating to a separate attachment view | Agent calls `list_material_attachments` inline, surfacing relevant files in context |

---

## 03 — Overview

This project provides a conversational AI interface over a materials management system for textile/apparel supply chains. Instead of navigating faceted filters, users query materials, products, and bills of materials (BOMs) through natural language via an AI agent.

The solution is a two-module Maven project built on **Quarkus 3.x** (Java 24):

| Module | Role |
|---|---|
| `quarkus-ai-materials-mgmt` | Reactive REST API backed by PostgreSQL |
| `quarkus-ai-materials-client` | MCP server exposing domain operations as AI tools |

The MCP server delegates all data access to the mgmt REST API — it has no direct database connection. An external AI agent (e.g. Goose CLI, Claude Desktop) connects via MCP and drives the tool calls.

---

## 04 — Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│  AI AGENT CLIENT (external)                                      │
│  Goose CLI / Claude Desktop / MCP Inspector                      │
└────────────────────────┬─────────────────────────────────────────┘
                         │ MCP (STDIO or SSE)
┌────────────────────────▼─────────────────────────────────────────┐
│  quarkus-ai-materials-client                                     │
│                                                                  │
│  MaterialsMcpTools   ← @Tool-annotated methods                   │
│  McpCliConfigSource  ← STDIO/SSE transport selection at startup  │
└────────────────────────┬─────────────────────────────────────────┘
                         │ HTTP REST
┌────────────────────────▼─────────────────────────────────────────┐
│  quarkus-ai-materials-mgmt                                       │
│                                                                  │
│  boundary/  ← JAX-RS resources (Uni<RestResponse<T>>)           │
│  control/   ← CDI services (@WithTransaction for writes)         │
│  entity/    ← Panache entities + repositories                    │
└────────────────────────┬─────────────────────────────────────────┘
                         │
                    PostgreSQL
```

### Transport Selection

The MCP server supports two transports, selected at startup:

- **STDIO** (default) — HTTP server disabled; used for Goose CLI / Claude Desktop integration
- **SSE** — enabled via `--sse` CLI flag; HTTP server enabled for browser/network clients

`McpCliConfigSource` (ordinal 400) parses CLI arguments and injects the appropriate Quarkus config properties before the application starts.

---

## 05 — Domain Model

```
┌──────────────────────┐       ┌────────────────────────────┐
│      Material        │       │     MaterialAttachment     │
├──────────────────────┤       ├────────────────────────────┤
│ id : Long     [PK]   │──1:N─▶│ id : Long          [PK]   │
│ code : String [UK]   │       │ material_id : Long  [FK]   │
│ name : String        │       │ attachmentType : enum      │
│ description : String │       │ fileName : String          │
│ category : enum      │       │ contentType : String       │
│ color : String       │       └────────────────────────────┘
│ composition : String │
│ supplierRef : String │       AttachmentType: IMAGE, SPEC, CERTIFICATE
│ minStock : Decimal   │
└──────────┬───────────┘       MaterialCategory:
           │                   FABRIC, THREAD, BUTTON, ZIPPER, LABEL,
           │ via BomItem        LINING, INTERLINING, ELASTIC, PACKAGING
           ▼
┌──────────────────────┐       ┌──────────────────────┐
│      BomItem         │       │        Bom           │
├──────────────────────┤       ├──────────────────────┤
│ id : Long     [PK]   │──N:1─▶│ id : Long     [PK]   │
│ bom_id : Long [FK]   │       │ product_id    [FK]   │
│ material_id   [FK]   │       │ version : Int        │
│ quantity : Decimal   │       │ status : enum        │
│ wastePct : Decimal   │       │ effectiveDate : Date │
│ notes : String       │       └──────────┬───────────┘
└──────────────────────┘                  │ N:1
  effectiveQuantity =                     ▼
  quantity × (1 + wastePct/100) ┌──────────────────────┐
                                │      Product         │
                                ├──────────────────────┤
                                │ id : Long     [PK]   │
                                │ sku : String  [UK]   │
                                │ name : String        │
                                │ status : enum        │
                                │ category : enum      │
                                └──────────────────────┘

  BomStatus: DRAFT, ACTIVE, OBSOLETE
  ProductStatus: DEVELOPMENT, ACTIVE, DISCONTINUED
  ProductCategory: SHIRT, PANTS, JACKET, DRESS, ACCESSORY, HOME_TEXTILE
```

---

## 06 — MCP Tool Catalogue

All tools are defined in `MaterialsMcpTools` using `@Tool` / `@ToolArg` from the Quarkus MCP server extension. Return types are `Uni<T>` — the extension handles async resolution transparently.

### Material Tools

| Tool | Parameters | Description |
|---|---|---|
| `list_materials` | `name?`, `supplierRef?`, `category?` | List materials with optional filters |
| `get_material_by_id` | `id` | Fetch a single material |
| `get_products_by_material` | `id` | Find all products using a material |
| `list_material_attachments` | `id` | List all attachments for a material |
| `list_material_attachments_rbac` | `id` | Attachments filtered by role (see RBAC below) |
| `get_material_attachment` | `id`, `attachmentId` | Fetch a specific attachment |

### Product Tools

| Tool | Parameters | Description |
|---|---|---|
| `list_products` | `sku?`, `status?`, `category?` | List products with optional filters |
| `get_product_by_id` | `id` | Fetch a single product |
| `get_materials_by_product` | `id` | All materials used across a product's BOMs |
| `list_product_boms` | `id`, `status?` | List BOMs for a product |
| `get_active_bom_for_product` | `id` | The current active BOM for a product |

### BOM Tools

| Tool | Parameters | Description |
|---|---|---|
| `list_boms` | `productId?`, `status?` | List BOMs with optional filters |
| `get_active_bom` | `productId` | Active BOM for a given product |
| `get_bom_by_id` | `id` | Fetch a BOM by ID |
| `list_bom_items` | `id` | All items (materials + quantities) in a BOM |

---

## 07 — RBAC

Role-based access is demonstrated on the `list_material_attachments_rbac` tool via `@RolesAllowed` and `SecurityIdentity`:

| Role | Visible attachment types |
|---|---|
| `material-engineer` | All types (IMAGE, SPEC, CERTIFICATE, …) |
| `junior-material-engineer` | IMAGE only |

The role check is enforced inside the tool method — the tool filters the REST API response based on the caller's identity claims. This demonstrates RBAC at the tool boundary without any changes to the mgmt service.

---

## 08 — Key Architecture Decisions

### ADR-01: No direct database access from the MCP server

The MCP server calls the mgmt REST API exclusively. Authorization, validation, and data access logic live in one place (mgmt), and the MCP server can be deployed, scaled, or replaced independently.

### ADR-02: Quarkus MCP server extension over langchain4j

The `quarkus-mcp-server` Quarkiverse extension exposes `@Tool`-annotated CDI beans directly as MCP tools. The LLM remains external (Goose CLI, Claude Desktop, or any MCP-compatible client). This avoids embedding an LLM in the service — the agent orchestration happens on the client side.

### ADR-03: STDIO as default transport

STDIO is the standard transport for local AI desktop clients (Goose, Claude Desktop). SSE is available for network-accessible deployments via the `--sse` flag. Transport selection requires no code changes — only startup arguments.

### ADR-04: PostgreSQL only (no OpenSearch)

All queries run against PostgreSQL using Panache (JPQL, LIKE-based text search). This keeps the infrastructure footprint minimal. A dedicated search index (e.g. OpenSearch) can be added later if full-text search quality or scale demands it.

### ADR-05: BCE package structure

Following the Boundary–Control–Entity pattern:
- `boundary/` — JAX-RS resources and MCP tool definitions
- `control/` — CDI service beans with `@WithTransaction`
- `entity/` — Panache entities and repositories

### ADR-06: Reactive stack throughout

All database operations return `Uni<T>` or `Multi<T>`. The Quarkus MCP server extension resolves `Uni` return types natively — tools do not need to block.

---

## 09 — Module Structure

```
quarkus-ai-materials/
├── quarkus-ai-materials-mgmt/
│   └── src/main/java/me/amiralles/materials/
│       ├── boundary/
│       │   ├── MaterialResource.java        ← @Path("/materials")
│       │   ├── ProductResource.java         ← @Path("/products")
│       │   ├── BomResource.java             ← @Path("/boms")
│       │   └── ExceptionMappers.java
│       ├── control/
│       │   ├── MaterialService.java
│       │   ├── ProductService.java
│       │   └── BomService.java
│       └── entity/
│           ├── Material.java
│           ├── Product.java
│           ├── Bom.java
│           ├── BomItem.java
│           ├── MaterialAttachment.java
│           └── (enums: MaterialCategory, ProductStatus, BomStatus, …)
│
└── quarkus-ai-materials-client/
    └── src/main/java/me/amiralles/materials/client/
        ├── MaterialsMcpTools.java           ← @Tool definitions
        ├── McpClientServerApplication.java  ← @QuarkusMain entry point
        ├── McpCliConfigSource.java          ← CLI arg → config (ordinal 400)
        ├── MaterialClient.java              ← REST client interfaces
        ├── ProductClient.java
        ├── BomClient.java
        └── model/                           ← Client-side DTOs
```

---

## 10 — Running the System

```bash
# Start the mgmt service (with Dev Services for PostgreSQL)
mvn quarkus:dev -f quarkus-ai-materials-mgmt/pom.xml

# Build and run the MCP server (STDIO — for Goose / Claude Desktop)
mvn package -f quarkus-ai-materials-client/pom.xml
java -jar quarkus-ai-materials-client/target/quarkus-app/quarkus-run.jar

# Run MCP server in SSE mode
java -jar quarkus-ai-materials-client/target/quarkus-app/quarkus-run.jar --sse

# Enable debug/traffic logging
java -jar ... --debug
```

Configure `server.url` to point at the mgmt service (default: `http://localhost:8080`).
