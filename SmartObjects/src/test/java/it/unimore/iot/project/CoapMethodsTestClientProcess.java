package it.unimore.iot.project;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.List;

public class CoapMethodsTestClientProcess {

    public static final String COAP_ENDPOINT = "coap://127.0.0.1:5683";
    public static final String COAP_RESOURCE = "temperature-actuator";

    public static final List<CoAP.Code> CODES_TO_TEST = List.of(
            CoAP.Code.GET,
            CoAP.Code.POST,
            CoAP.Code.PUT,
            CoAP.Code.FETCH
    );

    public static void main(String[] args) throws ConnectorException, IOException {
        CoapClient client = new CoapClient(String.format("%s/%s", COAP_ENDPOINT, COAP_RESOURCE));

        for (CoAP.Code code: CODES_TO_TEST) {
            Request request = new Request(code);
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);

            System.out.println("---" + code + "---");

            if (response != null && response.isSuccess()) {
                System.out.println("Status: " + response.getCode());
                System.out.println("Accepted");
            }
            else {
                System.out.println("Not accepted");
            }
        }


    }
}
