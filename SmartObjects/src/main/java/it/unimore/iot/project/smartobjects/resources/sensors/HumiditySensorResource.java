package it.unimore.iot.project.smartobjects.resources.sensors;

import com.google.gson.Gson;
import it.unimore.dipi.iot.utils.SenMLPack;
import it.unimore.dipi.iot.utils.SenMLRecord;
import it.unimore.iot.project.smartobjects.models.sensors.HumiditySmartSensor;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Optional;

public class HumiditySensorResource extends CoapSenmlSensorResource<HumiditySmartSensor> {
    private static final String OBJECT_TITLE = "HumiditySensor";

    private static final Number SENSOR_VERSION = 0.2;

    // SenML
    private static final String SENML_RT = "iot.sensor.humidity";
    private static final String SENML_UNIT = "%RH";

    private Gson gson;

    public HumiditySensorResource(String deviceId, String name, HumiditySmartSensor sensor) {
        super(deviceId, OBJECT_TITLE, name, SENML_RT, SENML_UNIT, SENSOR_VERSION, sensor);

        init();
    }

    private void init() {
        this.gson = new Gson();
    }

    private Optional<String> getJsonSenmlResponse() {
        try {
            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(String.format("%s:%s", this.deviceId, this.getName()));
            senMLRecord.setBver(this.sensorVersion);
            senMLRecord.setU(this.senmlRt);
            senMLRecord.setV(updatedValue);
            senMLRecord.setT(System.currentTimeMillis());

            senMLPack.add(senMLRecord);

            return Optional.of(gson.toJson(senMLPack));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.setMaxAge(sensor.getTimerUpdatePeriod());

        if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON) {
            Optional<String> payload = getJsonSenmlResponse();

            if (payload.isPresent()) {
                exchange.respond(CoAP.ResponseCode.CONTENT, payload.get(), exchange.getRequestOptions().getAccept());
            }
            else {
                exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(updatedValue), MediaTypeRegistry.TEXT_PLAIN);
        }
    }
}
