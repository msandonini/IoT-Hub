package it.unimore.iot.project.hub.coap.model;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.Set;

public class ResourceDescriptor {

    protected DeviceDescriptor device;
    protected String name;

    protected Set<CoAP.Code> methods;

    public ResourceDescriptor(DeviceDescriptor device, String name) {
        this.device = device;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CoAP.Code> getMethods() {
        return methods;
    }

    public String handleGET() {
        return handleGET("");
    }

    public String handleGET(String querystring) {
        String targetUri = String.format("%s:%d/%s?%s", device.getAddress(), device.getPort(), name, querystring);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.GET);
        request.setConfirmable(true);

        String message;     // Error message

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && response.isSuccess()) {
                methods.add(CoAP.Code.GET);
                return response.getResponseText();
            }
            else if (response == null) {
                message = "response is null";
            }
            else {
                message = "code " + response.getCode().toString();
            }


        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            message = "communication problem";
        }

        return "Error: " + message;
    }

    public String handlePOST() {
        // TODO: Implement POST (Remember to add methods history)

        String targetUri = String.format("%s:%d/%s", device.getAddress(), device.getPort(), name);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.POST);
        request.setConfirmable(true);

        String message;

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && response.isSuccess()) {
                methods.add(CoAP.Code.POST);
                return "Status changed";
            }
            else if (response == null) {
                message = "response is null";
            }
            else {
                message = "code " + response.getCode().toString();
            }
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            message = "communication problem";
        }

        return "Error: " + message;
    }

    public String handlePUT(String payload) {
        // TODO: Implement PUT (Remember to add methods history)

        return "Error";
    }

    @Override
    public String toString() {
        return "DeviceCoapResource{" +
                "name='" + name + '\'' +
                '}';
    }
}
