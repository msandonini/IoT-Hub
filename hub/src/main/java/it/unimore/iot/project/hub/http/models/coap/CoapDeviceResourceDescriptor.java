package it.unimore.iot.project.hub.http.models.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CoapDeviceResourceDescriptor {
    private static final List<CoAP.Code> CODES_TO_TEST = List.of(
            CoAP.Code.GET,
            CoAP.Code.POST,
            CoAP.Code.PUT
    );

    private CoapDeviceDescriptor device;

    private String name;

    private List<CoAP.Code> methods;

    private String savedValue;

    private boolean observable;

    CoapObserveRelation observeRelation;

    public CoapDeviceResourceDescriptor(CoapDeviceDescriptor device, String name, boolean observable) {
        this.device = device;
        this.name = name;
        this.observable = observable;


        updateMethods();
        reloadObservability();
    }

    public void updateMethods() {
        this.methods = new ArrayList<CoAP.Code>();

        CoapClient client = new CoapClient(String.format("%s%s", device.getAddress(), name));

        for (CoAP.Code code: CODES_TO_TEST) {
            Executors.newSingleThreadExecutor().submit(() -> testCode(client, code));
            reloadObservability();
        }
    }

    private synchronized void testCode(CoapClient client, CoAP.Code code) {
        Request request = new Request(code);
        request.setConfirmable(true);

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && (response.isSuccess() || response.getCode() == CoAP.ResponseCode.INTERNAL_SERVER_ERROR)) {
                methods.add(code);
            }
        } catch (Exception ignored) {
        }
    }

    private void reloadObservability() {
        try {
            if (this.observable) {
                if (this.observeRelation != null) {
                    observeRelation.proactiveCancel();
                }

                String targetUri = String.format("%s%s", device.getAddress(), name);

                CoapClient client = new CoapClient(targetUri);

                Request request = new Request(CoAP.Code.GET);
                request.setURI(targetUri);
                request.setObserve();
                request.setConfirmable(true);

                observeRelation = client.observe(request, new CoapHandler() {
                    @Override
                    public void onLoad(CoapResponse response) {
                        savedValue = response.getResponseText();
                        // System.out.println("[RESOURCE][UPDATE] - [TARGET] " + targetUri + " [VALUE] " + savedValue);
                    }

                    @Override
                    public void onError() {
                        savedValue = "Error";
                    }
                });
            } else if (this.observeRelation != null){
                observeRelation.proactiveCancel();
                this.observeRelation = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String handleGET() {
        if (observable)
            return savedValue;

        String targetUri = String.format("%s%s", device.getAddress(), name);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.GET);
        request.setConfirmable(true);

        try {
            CoapResponse response = client.advanced(request);

            if (response != null && response.isSuccess()) {
                return response.getResponseText();
            }
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String handlePOST() {
        // TODO: Implement POST

        return "";
    }

    public String handlePUT(String payload) {
        // TODO: Implement PUT

        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CoAP.Code> getMethods() {
        return methods;
    }

    public void setMethods(List<CoAP.Code> methods) {
        this.methods = methods;
    }

    public boolean isObservable() {
        return observable;
    }

    public void setObservable(boolean observable) {
        if (observable != this.observable) {
            this.observable = observable;
            reloadObservability();
        }
    }

    @Override
    public String toString() {
        return "DeviceCoapResource{" +
                "name='" + name + '\'' +
                ", observable=" + observable +
                '}';
    }
}
