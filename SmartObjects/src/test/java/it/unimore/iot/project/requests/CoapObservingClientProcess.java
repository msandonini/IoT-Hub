package it.unimore.iot.project.requests;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class CoapObservingClientProcess {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5684";

    private static final String DEVICE_URI = "temperature";

    public static void main(String[] args) {
        String targetUri = String.format("%s/%s", COAP_ENDPOINT, DEVICE_URI);

        System.out.println("[TEST][OBSERVING] - Target: " + targetUri);

        CoapClient client = new CoapClient(targetUri);

        Request request = new Request(CoAP.Code.GET);
        request.setURI(targetUri);
        request.setObserve();
        request.setConfirmable(true);

        CoapObserveRelation relation = client.observe(request, new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("[RESPONSE]");
                System.out.println(Utils.prettyPrint(response));
            }

            @Override
            public void onError() {
                System.out.println("[ERROR] - Observing failed");
            }
        });

        try {
            Thread.sleep(60*3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        relation.proactiveCancel();
    }
}
