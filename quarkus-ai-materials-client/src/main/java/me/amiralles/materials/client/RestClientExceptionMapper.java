package me.amiralles.materials.client;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class RestClientExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    @Override
    public RuntimeException toThrowable(Response response) {
        String body = response.readEntity(String.class);
        return new WebApplicationException(
            "REST call failed: HTTP " + response.getStatus() + " - " + body,
            response.getStatus()
        );
    }
}
