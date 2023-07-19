package it.unimore.iot.project.smartobjects.resources.actuators;

import it.unimore.iot.project.smartobjects.models.actuators.LightSmartActuator;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONObject;

import java.awt.*;
import java.util.Optional;

public class LightActuatorResource extends CoapActuatorResource<LightSmartActuator> {
    private static final String OBJECT_TITLE = "LightActuator";
    private static final Number ACTUATOR_VERSION = 0.1;

    public LightActuatorResource(String deviceId, String name, LightSmartActuator actuator) {
        super(deviceId, OBJECT_TITLE, name, ACTUATOR_VERSION, actuator, false);
    }

    private Optional<String> getJsonResponse() {
        JSONObject json = new JSONObject();

        json.put("name", String.format("%s:%s", this.deviceId, this.getName()));
        json.put("version", this.actuatorVersion);
        json.put("timestamp", System.currentTimeMillis());
        json.put("color", "#" +
                Integer.toHexString(this.actuator.getSavedValue().getRed()) +
                Integer.toHexString(this.actuator.getSavedValue().getGreen()) +
                Integer.toHexString(this.actuator.getSavedValue().getBlue()));
        json.put("status", LightSmartActuator.getStatusDescription(this.actuator.getStatus()));

        return Optional.of(json.toString());
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        /*
        GET REQUEST
         */
        try {
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON) {
                Optional<String> payload = getJsonResponse();

                if (payload.isPresent()) {
                    exchange.respond(CoAP.ResponseCode.CONTENT, payload.get(), MediaTypeRegistry.APPLICATION_JSON);
                } else {
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
                }
            }
            else  {
                exchange.respond(CoAP.ResponseCode.CONTENT, actuator.toString(), MediaTypeRegistry.TEXT_PLAIN);
            }
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
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
                String payload = new String(exchange.getRequestPayload()).trim();

                this.actuator.setSavedValue(Color.decode(payload));

                exchange.respond(CoAP.ResponseCode.CHANGED);
            } else {
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
