package it.unimore.iot.project.hub.coap.model;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;

public class ObservableResourceDescriptor extends ResourceDescriptor{
    protected String savedValue;
    CoapObserveRelation observeRelation;

    public ObservableResourceDescriptor(DeviceDescriptor device, String name) {
        super(device, name);
        reloadObserveRelation();
    }

    public void reloadObserveRelation() {
        try {
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
                    if (response.isSuccess() && !response.getResponseText().isBlank()) {
                        savedValue = response.getResponseText();
                    } else {
                        savedValue = "Error";
                    }
                    // System.out.println("[RESOURCE][UPDATE] - [TARGET] " + targetUri + " [VALUE] " + savedValue);
                }

                @Override
                public void onError() {
                    savedValue = "Error";
                }
            });
        } catch (Exception e) {
            this.savedValue = "Error";
            e.printStackTrace();
        }
    }

    @Override
    public String handleGET() {
        return this.savedValue;
    }
}
