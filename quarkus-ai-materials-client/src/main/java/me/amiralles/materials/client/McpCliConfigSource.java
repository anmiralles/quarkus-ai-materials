package me.amiralles.materials.client;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.*;

public class McpCliConfigSource implements ConfigSource {

    private static Map<String, String> configuration = new HashMap<>();

    public static List<String> setupConfigSource(String[] args) {
        List<String> remainingArgs = new ArrayList<>();
        for (String arg : args) {
            if (arg.startsWith("--") || arg.startsWith("-D")) {
                String[] parts = arg.substring(2).split("=");
                if (parts.length == 2) {
                    McpCliConfigSource.put(parts[0], parts[1]);
                } else {
                    McpCliConfigSource.put(parts[0], "true");
                }

                if (parts[0].equals("debug")) {
                    configuration.put("quarkus.mcp.server.client-logging.default-level", "DEBUG");
                    configuration.put("quarkus.mcp.server.traffic-logging.enabled", "true");
                    configuration.put("quarkus.log.category.\"io.quarkus.mcp.servers\".level", "DEBUG");
                }
            } else {
                remainingArgs.add(arg);
            }
        }

        boolean sse = Boolean.parseBoolean(configuration.get("sse"));

        if (sse) {
            configuration.put("quarkus.http.host-enabled", "true");
            configuration.put("quarkus.mcp.server.stdio.enabled", "false");
        } else {
            configuration.put("quarkus.http.host-enabled", "false");
            configuration.put("quarkus.mcp.server.stdio.enabled", "true");
        }

        return remainingArgs;
    }

    public static void put(String key, String value) {
        configuration.put(key, value);
    }

    @Override
    public Set<String> getPropertyNames() {
        return configuration.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return configuration.get(propertyName);
    }

    @Override
    public String getName() {
        return "McpCliConfigSource";
    }

    @Override
    public int getOrdinal() {
        return 400; // Higher than default to override
    }
}
