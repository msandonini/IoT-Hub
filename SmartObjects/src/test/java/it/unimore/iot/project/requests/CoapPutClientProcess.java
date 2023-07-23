package it.unimore.iot.project.requests;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class CoapPutClientProcess {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5686";
    private static final String[] DEVICES_URI_ARR = {
            "shutter-actuator",
    };

    private static final String QUERYSTING = "";

    private static final int INDEX_TO_TEST = 0;

    private static final String PAYLOAD = "0.5";

    public static void main(String[] args) {
        try {
            CoapClient client = new CoapClient(String.format("%s/%s?%s", COAP_ENDPOINT, DEVICES_URI_ARR[INDEX_TO_TEST], QUERYSTING));

            Request request = new Request(CoAP.Code.PUT);
            request.setPayload(PAYLOAD);
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);

            System.out.println(Utils.prettyPrint(response));
        } catch (ConnectorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
