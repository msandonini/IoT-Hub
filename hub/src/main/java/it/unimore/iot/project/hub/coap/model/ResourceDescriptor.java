package it.unimore.iot.project.hub.coap.model;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class ResourceDescriptor {

    protected DeviceDescriptor device;
    protected String name;

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

    public String handleGET() {
        return handleGET("");
    }

    public String handleGET(String querystring) {
        String targetUri = String.format("%s:%d/%s?%s", device.getAddress(), device.getPort(), name, querystring);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.GET);
        request.setConfirmable(true);

        String errorMessage;     // Error message

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && response.isSuccess()) {
                return response.getResponseText();
            }
            else if (response == null) {
                errorMessage = "response is null";
            }
            else {
                errorMessage = "code " + response.getCode().toString();
            }


        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            errorMessage = "communication problem";
        }

        return "Error: " + errorMessage;
    }

    public String handlePOST() {
        String targetUri = String.format("%s:%d/%s", device.getAddress(), device.getPort(), name);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.POST);
        request.setConfirmable(true);

        String errorMessage;

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && response.isSuccess()) {
                return "Status changed";
            }
            else if (response == null) {
                errorMessage = "response is null";
            }
            else {
                errorMessage = "code " + response.getCode().toString();
            }
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            errorMessage = "communication problem";
        }

        return "Error: " + errorMessage;
    }

    public String handlePUT(String payload) {
        String targetUri = String.format("%s:%d/%s", device.getAddress(), device.getPort(), name);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.PUT);
        request.setConfirmable(true);
        request.setPayload(payload);

        String errorMessage;

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && response.isSuccess()) {
                return "Status changed";
            }
            else if (response == null) {
                errorMessage = "response is null";
            }
            else {
                errorMessage = "code " + response.getCode().toString();
            }
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            errorMessage = "communication problem";
        }

        return "Error: " + errorMessage;
    }

    @Override
    public String toString() {
        return "DeviceCoapResource{" +
                "name='" + name + '\'' +
                '}';
    }
}
