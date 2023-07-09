package it.unimore.iot.project.smartobjects;

import it.unimore.iot.project.smartobjects.models.actuators.HumiditySmartActuator;
import it.unimore.iot.project.smartobjects.models.actuators.TemperatureSmartActuator;
import it.unimore.iot.project.smartobjects.models.sensors.HumiditySmartSensor;
import it.unimore.iot.project.smartobjects.models.sensors.TemperatureSmartSensor;
import it.unimore.iot.project.smartobjects.resources.actuators.HumidityActuatorResource;
import it.unimore.iot.project.smartobjects.resources.sensors.HumiditySensorResource;
import it.unimore.iot.project.smartobjects.resources.actuators.TemperatureActuatorResource;
import it.unimore.iot.project.smartobjects.resources.sensors.TemperatureSensorResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

public class EnvironmentalControllerSmartObject extends CoapServer {
    public static final int COAP_PORT = 5683;

    public static final String DEVICE_ID = "iot:environment";

    TemperatureSmartSensor temperatureSensor;
    TemperatureSmartActuator temperatureActuator;

    HumiditySmartSensor humiditySensor;
    HumiditySmartActuator humidityActuator;

    public EnvironmentalControllerSmartObject(int port) {
        super(port);

        //String DEVICE_ID = String.format("iot:%s", UUID.randomUUID().toString());

        temperatureSensor = new TemperatureSmartSensor();
        temperatureActuator = new TemperatureSmartActuator();

        humiditySensor = new HumiditySmartSensor();
        humidityActuator = new HumiditySmartActuator();

        temperatureSensor.addDataListener(temperatureActuator);
        humiditySensor.addDataListener(humidityActuator);

        TemperatureSensorResource temperatureSensorResource = new TemperatureSensorResource(DEVICE_ID, "temperature-sensor", temperatureSensor);
        TemperatureActuatorResource temperatureActuatorResource = new TemperatureActuatorResource(DEVICE_ID, "temperature-actuator", temperatureActuator);
        HumiditySensorResource humiditySensorResource = new HumiditySensorResource(DEVICE_ID, "humidity-sensor", humiditySensor);
        HumidityActuatorResource humidityActuatorResource = new HumidityActuatorResource(DEVICE_ID, "humidity-actuator", humidityActuator);

        this.add(temperatureSensorResource);
        this.add(temperatureActuatorResource);
        this.add(humiditySensorResource);
        this.add(humidityActuatorResource);

//        logTemperatureSensor();
//        logTemperatureActuator();
//        logHumiditySensor();
//        logHumidityActuator();
    }

    private void logTemperatureSensor() {
        temperatureSensor.addDataListener(value -> System.out.println(String.format("[SENSOR][TEMPERATURE] -> [VALUE] %f", value)));
    }
    private void logTemperatureActuator() {
        temperatureActuator.addDataListener(value -> System.out.println(String.format("[ACTUATOR][TEMPERATURE] -> [VALUE] %s", TemperatureSmartActuator.getStatusDescription(value))));
    }
    private void logHumiditySensor() {
        humiditySensor.addDataListener(value -> System.out.println(String.format("[SENSOR][HUMIDITY] -> [VALUE] %f", value)));
    }
    private void logHumidityActuator() {
        humidityActuator.addDataListener(value -> System.out.println(String.format("[ACTUATOR][HUMIDITY] -> [VALUE] %s", HumiditySmartActuator.getStatusDescription(value))));
    }

    public static void main(String[] args) {
        EnvironmentalControllerSmartObject smartObject = new EnvironmentalControllerSmartObject(COAP_PORT);
        smartObject.start();

        smartObject.getRoot().getChildren().forEach(EnvironmentalControllerSmartObject::logResource);
    }

    private static void logResource(Resource resource) {
        System.out.println(String.format(
                "[RESOURCE FOUND] -> [NAME] %s [URI] %s [OBSERVABLE] %b",
                resource.getName(),
                resource.getURI(),
                resource.isObservable()
        ));

        if (!resource.getURI().equals("/.well-known")) {
            resource.getChildren().forEach(EnvironmentalControllerSmartObject::logResource);
        }
    }
}
