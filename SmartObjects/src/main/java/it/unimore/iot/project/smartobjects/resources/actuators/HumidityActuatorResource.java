package it.unimore.iot.project.smartobjects.resources.actuators;

import com.google.gson.Gson;
import it.unimore.dipi.iot.utils.SenMLPack;
import it.unimore.dipi.iot.utils.SenMLRecord;
import it.unimore.iot.project.smartobjects.models.actuators.HumiditySmartActuator;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Optional;

public class HumidityActuatorResource extends CoapSenmlActuatorResource<HumiditySmartActuator> {
    private static final String OBJECT_TITLE = "HumidityActuator";
    private static final Number ACTUATOR_VERSION = 0.1;
    private static final String SENML_RT = "iot.actuator.humidity";
    private static final String SENML_UNIT = "%RH";

    private Gson gson;

    public HumidityActuatorResource(String deviceId, String name, HumiditySmartActuator actuator) {
        super(deviceId, OBJECT_TITLE, name, SENML_RT, SENML_UNIT, ACTUATOR_VERSION, actuator);

        init();
    }

    private void init() {
        this.gson = new Gson();
    }

    private Optional<String> getJsonSenmlResponse() {
        try {
            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord baseRecord = new SenMLRecord();
            baseRecord.setBn(String.format("%s:%s", this.deviceId, this.getName()));
            baseRecord.setBver(actuatorVersion);
            baseRecord.setT(System.currentTimeMillis());

            SenMLRecord valueRecord = new SenMLRecord();
            valueRecord.setN("value");
            valueRecord.setU(this.senmlUnit);
            valueRecord.setV(this.actuator.getWantedHumidity());

            SenMLRecord statusRecord = new SenMLRecord();
            statusRecord.setN("status");
            statusRecord.setVs(HumiditySmartActuator.getStatusDescription(this.actuator.getStatus()));

            senMLPack.add(baseRecord);
            senMLPack.add(valueRecord);
            senMLPack.add(statusRecord);

            return Optional.of(gson.toJson(senMLPack));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        /*
        GET REQUEST

        Returns wanted value and status
         */
        try {
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
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.actuator.getStatus()), MediaTypeRegistry.TEXT_PLAIN);
            }
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        /*
        POST REQUEST

        Toggle between on/off
         */
        try {
            this.actuator.toggleActive();

            exchange.respond(CoAP.ResponseCode.CHANGED);
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange) {
        /*
        PUT REQUEST

        Change wanted value
         */
        try {
            if (exchange.getRequestPayload() != null) {
                String payload = new String(exchange.getRequestPayload());

                this.actuator.setWantedHumidity(Double.parseDouble(payload));

                exchange.respond(CoAP.ResponseCode.CHANGED);
            } else {
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
