package it.unimore.iot.project.hub.coap.model;

import com.google.common.collect.Lists;
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
import java.util.*;
import java.util.concurrent.Executors;

public class DeviceDescriptor {

    private Set<ResourceDescriptor> resources;

    private String name;
    private String address;
    private int port;

<<<<<<< HEAD:hub/src/main/java/it/unimore/iot/project/hub/coap/model/DeviceDescriptor.java

    public DeviceDescriptor(String name, String address, int port) {
        this.resources = new HashSet<>();
=======
    public CoapDeviceDescriptor(String name, String address, int port) {
        this.resources = new ArrayList<CoapDeviceResourceDescriptor>();
>>>>>>> parent of feec322 (Started adding manual POST && PUT support setting to avoid resource code testing):Hub/src/main/java/it/unimore/iot/project/hub/http/models/coap/CoapDeviceDescriptor.java

        this.name = name;
        this.address = address;
        this.port = port;

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                discoverResources();
            } catch (ConnectorException e) {
                System.out.println("[ERROR][RESOURCE DISCOVERY] - [DEVICE] " + name + " [TYPE] ConnectorException");
            } catch (IOException e) {
                System.out.println("[ERROR][RESOURCE DISCOVERY] - [DEVICE] " + name + " [TYPE] IOException");
            } catch (Exception e) {
                System.out.println("[ERROR][RESOURCE DISCOVERY] - [DEVICE] " + name + " [TYPE] Exception (Generic)");
            }
        });
    }

    private void discoverResources() throws ConnectorException, IOException {
        System.out.println("[RESOURCE DISCOVERY] - [DEVICE] " + name + " [STATUS] Starting");
        CoapClient client = new CoapClient(String.format("coap://%s:%d/.well-known/core", address, port));

        Request request = new Request(CoAP.Code.GET);

        CoapResponse response = client.advanced(request);

        if (response != null && response.isSuccess()) {
            if (response.getOptions().getContentFormat() != MediaTypeRegistry.APPLICATION_LINK_FORMAT) {
                System.out.println("[RESOURCE DISCOVERY] - [DEVICE] " + name + " [STATUS] Error: Not link format");
                return;
            }

            Set<WebLink> links = LinkFormat.parse(response.getResponseText());

            for (WebLink link : links) {
                System.out.println("[RESOURCE DISCOVERY] - [DEVICE] " + name + " [LINK] " + link.toString());

                Set<String> keySet = link.getAttributes().getAttributeKeySet();

                if (keySet.contains("rt")) {
                    ResourceDescriptor resourceDescriptor;

                    if (keySet.contains("obs")) {
                        resourceDescriptor = new ObservableResourceDescriptor(this, link.getURI());
                    }
                    else {
                        resourceDescriptor = new ResourceDescriptor(this, link.getURI());
                    }
                    resources.add(resourceDescriptor);
                }
            }

            System.out.println("[RESOURCE DISCOVERY] - [DEVICE] " + name + " [STATUS] Ended [RESOURCES] " + resources);
        }
        else {
            System.out.println("[RESOURCE DISCOVERY] - [DEVICE] " + name + " [STATUS] Ended [RESOURCES] No resources found");
        }

    }

    // TODO: Manage data getters
    // TODO: Manage observing data updates
    // TODO: Manage non-observing GET requests
    // TODO: Manage data update requests

    public String sendGetRequest(String resourceName) {
        for (ResourceDescriptor resource : resources) {
            if (resource.getName().equals(resourceName)) {
                return resource.handleGET();
            }
        }

        return "Error: no resource with this name";
    }

    public String sendGetRequest(String resourceName, String querystring) {
        for (ResourceDescriptor resource : resources) {
            if (resource.getName().equals(resourceName)) {
                return resource.handleGET(querystring);
            }
        }

        return "Error: no resource with this name";
    }

    public String sendPostRequest(String resourceName) {
        for (ResourceDescriptor resource : resources) {
            if (resource.getName().equals(resourceName)) {
                return resource.handlePOST();
            }
        }

        return "Error: no resource with this name";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getResources() {
        ArrayList<String> resourceNames = new ArrayList<String>();
        for (ResourceDescriptor resource : resources) {
            resourceNames.add(resource.getName());
        }
        return resourceNames;
    }
}
