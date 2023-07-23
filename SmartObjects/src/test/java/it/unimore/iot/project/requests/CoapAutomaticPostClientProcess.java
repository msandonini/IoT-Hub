package it.unimore.iot.project.requests;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CoapAutomaticPostClientProcess {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5686";
    private static final String COAP_RESOURCE_DISCOVERY = "/.well-known/core";

    private static final String[] RT_DEVICES_ARR = {
            "iot.actuator.shutter"
    };
    private static ArrayList<String> RT_DEVICES = new ArrayList<>(List.of(RT_DEVICES_ARR));

    private static String targetUri = null;

    public static void main(String[] args) {
        try {
            CoapClient coapClient = new CoapClient();

            System.out.println("[CLIENT STARTED]");

            Request request = new Request(CoAP.Code.GET);
            request.setURI(String.format("%s%s", COAP_ENDPOINT, COAP_RESOURCE_DISCOVERY));
            request.setConfirmable(true);

            System.out.println(String.format("[REQUEST CREATED] -> %s", request.toString()));

            CoapResponse response = coapClient.advanced(request);

            if (response != null) {
                System.out.println(String.format("[RESPONSE RECEIVED] -> %s", response.toString()));
                manageResourceDiscovery(response);
            }
            else {
                System.err.println("[ERROR] -> Response is null");
            }

        } catch (ConnectorException | IOException e) {
            System.err.println("[ERROR] -> Stack trace:");
            e.printStackTrace();
        }
    }

    private static void manageResourceDiscovery(CoapResponse response) {
        if (response.getOptions().getContentFormat() != MediaTypeRegistry.APPLICATION_LINK_FORMAT) {
            System.err.println("[ERROR] -> Response content is not link format");
            return;
        }
        Set<WebLink> links = LinkFormat.parse(response.getResponseText());

        System.out.println("[RESOURCES DISCOVERED] - Links: ");
        System.out.println(links.toString());

        links.forEach(CoapAutomaticPostClientProcess::manageSingleLink);
    }

    private static void manageSingleLink(WebLink link) {
        try {
            String uri = link.getURI();

            System.out.println("[LINK] - Uri: " + uri);

            link.getAttributes().getAttributeKeySet().forEach(key -> {
                String value = link.getAttributes().getAttributeValues(key).get(0);

                if (key.equals("rt") && RT_DEVICES.contains(value)) {
                    targetUri = uri;
                    System.out.println("[TARGET OK]");
                }
            });

            if (targetUri != null) {
                System.out.println(Utils.prettyPrint(makeCoapPostRequest(String.format("%s%s", COAP_ENDPOINT, targetUri))));
            }
        }
        catch (ConnectorException | IOException e) {
            System.err.println("[ERROR] - Stack trace:");
            e.printStackTrace();
        }
        finally {
            targetUri = null;
        }
    }

    private static CoapResponse makeCoapPostRequest(String endpoint) throws ConnectorException, IOException {
        CoapClient client = new CoapClient(endpoint);

        Request request = new Request(CoAP.Code.POST);
        request.setPayload("");

        request.setConfirmable(true);

        return client.advanced(request);
    }
}
