package it.unimore.iot.project.requests;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.*;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class CoapAutomaticGetClientProcess {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5684";
    private static final String COAP_RESOURCE_DISCOVERY = "/.well-known/core";

    private static final String LOG_PATH = "log/client.get/";

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

    private static void manageResourceDiscovery (CoapResponse response) {
        if (response.getOptions().getContentFormat() != MediaTypeRegistry.APPLICATION_LINK_FORMAT) {
            System.err.println("[ERROR] -> Response content is not link format");
            return;
        }
        Set<WebLink> links = LinkFormat.parse(response.getResponseText());

        System.out.println("[RESOURCES DISCOVERED] - Links: {");
        for (WebLink link : links) {
            System.out.println(link.toString());
        }
        System.out.println("}");

        links.forEach(CoapAutomaticGetClientProcess::manageSingleLink);
    }

    private static void manageSingleLink (WebLink link) {
        try {
            String uri = link.getURI();

            System.out.println("[LINK] - Uri: " + uri);

            link.getAttributes().getAttributeKeySet().forEach(key -> {
                String value = link.getAttributes().getAttributeValues(key).get(0);

                if (key.equals("rt")/* && RT_DEVICES.contains(value)*/) {
                    targetUri = uri;
                    System.out.println("[TARGET OK]");
                }
            });

            if (targetUri != null) {
                CoapResponse response = makeCoapGetRequest(String.format("%s%s", COAP_ENDPOINT, targetUri));
                String prettyPrint = Utils.prettyPrint(response);

                Files.createDirectories(Paths.get(LOG_PATH));

                try (
                        BufferedWriter writer = new BufferedWriter(
                                new FileWriter(String.format("%s/%s.log", LOG_PATH, targetUri))
                        )
                ) {
                    writer.write(prettyPrint);
                }
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

    private static CoapResponse makeCoapGetRequest(String endpoint) throws ConnectorException, IOException {
        CoapClient client = new CoapClient(endpoint);

        Request request = new Request(CoAP.Code.GET);
        request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_JSON));

        request.setConfirmable(true);

        return client.advanced(request);
    }
}
