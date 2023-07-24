package it.unimore.iot.project.smartobjects.resources.actuators;

import com.google.gson.Gson;
import it.unimore.dipi.iot.utils.SenMLPack;
import it.unimore.dipi.iot.utils.SenMLRecord;
import it.unimore.iot.project.smartobjects.models.actuators.ShutterActuator;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Optional;

public class ShutterActuatorResource extends CoapSenmlActuatorResource<ShutterActuator> {

    private static final String OBJECT_TITLE = "ShutterActuator";

    private static final Number ACTUATOR_VERSION = 0.1;

    private static final String SENML_RT = "iot.actuator.shutter";
    private static final String SENML_UNIT = "/";

    private Gson gson;

    public ShutterActuatorResource(String deviceId, String name, ShutterActuator actuator) {
        super(deviceId, OBJECT_TITLE, name, SENML_RT, SENML_UNIT, ACTUATOR_VERSION, actuator);

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
            senMLRecord.setBver(this.actuatorVersion);
            senMLRecord.setT(System.currentTimeMillis());
            senMLRecord.setN("value");
            senMLRecord.setV(((double) this.actuator.getStatus()) / 100);
            senMLRecord.setU(this.senmlUnit);

            senMLPack.add(senMLRecord);

            return Optional.of(gson.toJson(senMLPack));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
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
        try {
            this.actuator.toggleActive();

            exchange.respond(CoAP.ResponseCode.CHANGED);
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange) {

        try {
            if (exchange.getRequestPayload() != null) {
                String payload = new String(exchange.getRequestPayload());

                this.actuator.setStatus((int) (Double.parseDouble(payload) * 100));

                exchange.respond(CoAP.ResponseCode.CHANGED);
            } else {
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
