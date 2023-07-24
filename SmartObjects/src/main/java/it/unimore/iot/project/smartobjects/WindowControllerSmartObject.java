package it.unimore.iot.project.smartobjects;

import it.unimore.iot.project.smartobjects.models.actuators.HumidityActivatedActuator;
import it.unimore.iot.project.smartobjects.models.actuators.LightActivatedActuator;
import it.unimore.iot.project.smartobjects.models.actuators.ShutterActuator;
import it.unimore.iot.project.smartobjects.models.sensors.HumiditySmartSensor;
import it.unimore.iot.project.smartobjects.models.sensors.LightSmartSensor;
import it.unimore.iot.project.smartobjects.resources.actuators.ShutterActuatorResource;
import it.unimore.iot.project.smartobjects.resources.sensors.HumiditySensorResource;
import it.unimore.iot.project.smartobjects.resources.sensors.LightSensorResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

public class WindowControllerSmartObject extends CoapServer {
    public static final int COAP_PORT = 5686;

    public static final String DEVICE_ID = "iot:window";

    LightSmartSensor lightSensor;
    LightActivatedActuator lightActivatedActuator;
    HumiditySmartSensor humiditySensor;
    HumidityActivatedActuator humidityActivatedActuator;
    ShutterActuator shutterActuator;

    public WindowControllerSmartObject(int port) {
        super(port);

        lightSensor = new LightSmartSensor();

        lightActivatedActuator = new LightActivatedActuator();
        humiditySensor = new HumiditySmartSensor();
        humidityActivatedActuator = new HumidityActivatedActuator();
        shutterActuator = new ShutterActuator(humidityActivatedActuator);

        lightSensor.addDataListener(lightActivatedActuator);
        humiditySensor.addDataListener(humidityActivatedActuator);
        lightActivatedActuator.addDataListener(shutterActuator);

        humidityActivatedActuator.setActive(true);
        lightActivatedActuator.setActive(true);
        shutterActuator.setActive(true);

//        logLightSensor();
//        logHumiditySensor();
//        logLightActivatedActuator();
//        logShutterActuator();

        LightSensorResource lightSensorResource = new LightSensorResource(DEVICE_ID, "light-sensor", lightSensor);
        HumiditySensorResource humiditySensorResource = new HumiditySensorResource(DEVICE_ID, "humidity-sensor", humiditySensor);
        ShutterActuatorResource shutterActuatorResource = new ShutterActuatorResource(DEVICE_ID, "shutter-actuator", shutterActuator);

        this.add(lightSensorResource);
        this.add(humiditySensorResource);
        this.add(shutterActuatorResource);
    }

    private void logLightSensor() {
        lightSensor.addDataListener(value -> System.out.println(String.format("[SENSOR][LIGHT] -> [VALUE] %f", value)));
    }
    private void logHumiditySensor() {
        humiditySensor.addDataListener(value -> System.out.println(String.format("[SENSOR][HUMIDITY] -> [VALUE] %f", value)));
    }
    private void logHumidityActivatedSensor() {
        humidityActivatedActuator.addDataListener(value -> System.out.println(String.format("[ACTUATOR][HUMIDITY] -> [VALUE] %b", value)));
    }
    private void logLightActivatedActuator() {
        lightActivatedActuator.addDataListener(value -> System.out.println(String.format("[ACTUATOR][LIGHT] -> [VALUE] %b", value)));
    }
    private void logShutterActuator() {
        shutterActuator.addDataListener(value -> System.out.println(String.format("[ACTUATOR][SHUTTERS] -> [VALUE] %d", value)));
    }

    public static void main(String[] args) {
        WindowControllerSmartObject smartObject = new WindowControllerSmartObject(COAP_PORT);
        smartObject.start();

        smartObject.getRoot().getChildren().forEach(WindowControllerSmartObject::logResource);
    }

    private static void logResource(Resource resource) {
        System.out.println(String.format(
                "[RESOURCE FOUND] -> [NAME] %s [URI] %s [OBSERVABLE] %b",
                resource.getName(),
                resource.getURI(),
                resource.isObservable()
        ));

        if (!resource.getURI().equals("/.well-known")) {
            resource.getChildren().forEach(WindowControllerSmartObject::logResource);
        }
    }
}
