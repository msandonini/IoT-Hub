package it.unimore.iot.project.hub.http.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jersey.errors.ErrorMessage;
import it.unimore.iot.project.hub.coap.model.DeviceDescriptor;
import it.unimore.iot.project.hub.http.services.AppConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hub/api/detail/")
public class DeviceDetailResource {
    private AppConfig appConfig;

    public DeviceDetailResource(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GET
    @Path("/{dname}")
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized Response getDevice(@Context ContainerRequestContext req,
                                           @PathParam("dname") String dname) {
        DeviceDescriptor device = appConfig.getDeviceManager().getDevice(dname);

        if (device == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), "Device not found"))
                    .build();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(device);

            System.out.println(json);

            return Response.ok(json).build();
        }
        catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal server error"))
                    .build();
        }


    }

    // TODO: Add POST & PUT for operations on device

}
