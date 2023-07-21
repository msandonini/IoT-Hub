package it.unimore.iot.project.smartobjects;

import it.unimore.iot.project.smartobjects.models.actuators.LightSmartActuator;
import it.unimore.iot.project.smartobjects.resources.actuators.LightActuatorResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

public class LightControllerSmartObject extends CoapServer {
    public static final int COAP_PORT = 5684;

    public static final String DEVICE_ID = "iot:light";

    private LightSmartActuator lightActuator;

    public LightControllerSmartObject(int port) {
        super(port);

        lightActuator = new LightSmartActuator();
        this.lightActuator.setActive(true);

        LightActuatorResource lightActuatorResource = new LightActuatorResource(DEVICE_ID, "light-actuator", lightActuator);

        this.add(lightActuatorResource);

        logLightActuator();
    }

    private void logLightActuator() {
        lightActuator.addDataListener(value -> System.out.println(
                String.format("[ACTUATOR][LIGHT] -> [ACTIVE] %b [STATUS] %b [COLOR] %s",
                        lightActuator.isActive(),
                        lightActuator.getStatus(),
                        lightActuator.getSavedValue().toString()))
        );
    }

    public static void main(String[] args) {
        LightControllerSmartObject smartObject = new LightControllerSmartObject(COAP_PORT);
        smartObject.start();

        smartObject.getRoot().getChildren().forEach(LightControllerSmartObject::logResource);
    }

    private static void logResource(Resource resource) {
        System.out.println(String.format(
                "[RESOURCE FOUND] -> [NAME] %s [URI] %s [OBSERVABLE] %b",
                resource.getName(),
                resource.getURI(),
                resource.isObservable()
        ));

        if (!resource.getURI().equals("/.well-known")) {
            resource.getChildren().forEach(LightControllerSmartObject::logResource);
        }
    }
}
