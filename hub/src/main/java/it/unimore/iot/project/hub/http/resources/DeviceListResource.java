package it.unimore.iot.project.hub.http.resources;

import io.dropwizard.jersey.errors.ErrorMessage;
import it.unimore.iot.project.hub.coap.model.DeviceDescriptor;
import it.unimore.iot.project.hub.http.request.DeviceAdditionRequest;
import it.unimore.iot.project.hub.http.services.AppConfig;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;

@Path("/hub/api/list/")
public class DeviceListResource {

    private AppConfig appConfig;

    public DeviceListResource(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized Response getDeviceList(@Context ContainerRequestContext req) {
        try {
            Map<String, DeviceDescriptor> map = appConfig.getDeviceManager().getDeviceMap();

            if (map == null || map.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), "Devices not found"))
                        .build();
            }

            return Response.ok(map).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal server error"))
                    .build();
        }
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized Response addDevice(@Context ContainerRequestContext req,
                                           @Context UriInfo uriInfo,
                                           DeviceAdditionRequest deviceRequest) {
        try {
            if (deviceRequest.getName() == null || deviceRequest.getName().isBlank() ||
                    deviceRequest.getAddress() == null || deviceRequest.getAddress().isBlank() ||
                    deviceRequest.getPort() < 0 || deviceRequest.getPort() > Math.pow(2, 16)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid content"))
                        .build();
            }
            DeviceDescriptor device = new DeviceDescriptor(deviceRequest.getName(), deviceRequest.getAddress(), deviceRequest.getPort());

            String name = appConfig.getDeviceManager().addDevice(device);

            String requestPath = uriInfo.getAbsolutePath().toString();
            String locationHeaderString = String.format("%s/%s", requestPath, name);

            return Response.created(new URI(locationHeaderString)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal server error"))
                    .build();
        }

    }

    @DELETE
    @Path("/{dname}")
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized Response deleteDevice(@Context ContainerRequestContext req,
                                              @PathParam("dname") String dname) {

        try {
            if (appConfig.getDeviceManager().removeDevice(dname)) {
                return Response.noContent().build();
            }

            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), "Device not found"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal server error"))
                    .build();
        }
    }
}
