package it.unimore.iot.project.requests;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class CoapGetClientProcess {

    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5683";
    private static final String[] DEVICES_URI_ARR = {
            ".well-known/core/",
            "temperature-sensor",
            "temperature-actuator",
            "humidity-sensor",
            "humidity-actuator",
            "light-actuator"
    };
    private static final int INDEX_TO_TEST = 0;

    private static final String QUERYSTRING = "";

    private static final int MEDIA_TYPE = MediaTypeRegistry.APPLICATION_LINK_FORMAT;

    public static void main(String[] args) {
        try {
            CoapClient client = new CoapClient(
                    String.format("%s/%s?%s", COAP_ENDPOINT, DEVICES_URI_ARR[INDEX_TO_TEST], QUERYSTRING));

            Request request = new Request(CoAP.Code.GET);

            OptionSet options = new OptionSet();
            options.setAccept(MEDIA_TYPE);

            request.setOptions(options);
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);

            System.out.println(Utils.prettyPrint(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
