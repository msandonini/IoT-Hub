package it.unimore.iot.project.smartobjects;

import it.unimore.iot.project.smartobjects.models.sensors.PresenceSmartSensor;
import it.unimore.iot.project.smartobjects.resources.sensors.PresenceSensorResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

public class PresenceMonitoringSmartObject extends CoapServer {
    public static final int COAP_PORT = 5685;

    public static final String DEVICE_ID = "iot:presence";

    private PresenceSmartSensor presenceSensor;

    public PresenceMonitoringSmartObject(int port) {
        super(port);

        this.presenceSensor = new PresenceSmartSensor();

        PresenceSensorResource presenceSensorResource = new PresenceSensorResource(DEVICE_ID, "presence-sensor", presenceSensor);

        this.add(presenceSensorResource);


    }

    public static void main(String[] args) {
        PresenceMonitoringSmartObject smartObject = new PresenceMonitoringSmartObject(COAP_PORT);

        smartObject.start();

        smartObject.getRoot().getChildren().forEach(PresenceMonitoringSmartObject::logResource);
    }

    private static void logResource(Resource resource) {
        System.out.println(String.format(
                "[RESOURCE FOUND] -> [NAME] %s [URI] %s [OBSERVABLE] %b",
                resource.getName(),
                resource.getURI(),
                resource.isObservable()
        ));

        if (!resource.getURI().equals("/.well-known")) {
            resource.getChildren().forEach(PresenceMonitoringSmartObject::logResource);
        }
    }
}
