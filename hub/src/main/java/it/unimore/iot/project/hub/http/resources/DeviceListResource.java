package it.unimore.iot.project.hub.http.resources;

import io.dropwizard.jersey.errors.ErrorMessage;
import it.unimore.iot.project.hub.coap.model.DeviceDescriptor;
import it.unimore.iot.project.hub.http.persistence.DeviceManager;
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

/**
 * A resource to manage the api requests sent to the path <a href="#@{link}">{@link /hub/api/list/*}</a>.
 *
 * @see AppConfig
 *
 * @author Sandonini Mirco
 */
@Path("/hub/api/list/")
public class DeviceListResource {

    private AppConfig appConfig;

    public DeviceListResource(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * The method that manages the GET requests to the path <a href="#@{link}">{@link /hub/api/list/}</a>.
     * It returns a JSON representation of the devices currently saved in the device manager
     *
     * @param req the api request
     * @return the response to the api call
     *
     * @see Context
     * @see ContainerRequestContext
     * @see DeviceManager
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleGet(@Context ContainerRequestContext req) {
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

    /**
     * The method that manages the PUT requests to the path <a href="#@{link}">{@link /hub/api/list/}</a>.
     * It adds a new device to the device manager.
     *
     * @param req the api request
     * @param uriInfo the request uri
     * @param deviceRequest the serialized representation of the device to add
     * @return the response to the api call
     *
     * @see Context
     * @see ContainerRequestContext
     * @see UriInfo
     * @see DeviceManager
     * @see DeviceAdditionRequest
     */
    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized Response handlePut(@Context ContainerRequestContext req,
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

    /**
     * The method that manages the DELETE requests to the path <a href="#@{link}">{@link /hub/api/list/{device}}</a>.
     * It removes a device from the device manager.
     *
     * @param req the api request
     * @param dname the device name
     * @return the response to the api call
     *
     * @see Context
     * @see ContainerRequestContext
     * @see PathParam
     * @see DeviceManager
     */
    @DELETE
    @Path("/{dname}")
    @Produces(MediaType.APPLICATION_JSON)
    public synchronized Response handleDelete(@Context ContainerRequestContext req,
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
