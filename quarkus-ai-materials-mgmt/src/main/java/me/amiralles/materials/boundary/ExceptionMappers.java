package me.amiralles.materials.boundary;

import jakarta.ws.rs.NotFoundException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionMappers {

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> mapNotFoundException(NotFoundException e) {
        return RestResponse.status(RestResponse.Status.NOT_FOUND,
                new ErrorResponse("NOT_FOUND", e.getMessage()));
    }
}
