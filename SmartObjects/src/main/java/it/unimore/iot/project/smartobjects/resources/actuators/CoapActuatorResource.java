package it.unimore.iot.project.smartobjects.resources.actuators;

import com.google.gson.Gson;
import it.unimore.iot.project.smartobjects.models.actuators.GenericActuator;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;

public abstract class CoapActuatorResource<T_ACTUATOR extends GenericActuator<?, ?>> extends CoapResource {
    protected final String objectTitle;

    protected final Number actuatorVersion;

    protected String deviceId;

    protected T_ACTUATOR actuator;

    public CoapActuatorResource(String deviceId, String objectTitle, String name,
                                Number actuatorVersion, T_ACTUATOR actuator) {
        super(name);
        this.objectTitle = objectTitle;
        this.actuatorVersion = actuatorVersion;
        this.deviceId = deviceId;
        this.actuator = actuator;

        init();
    }

    private void init() {
        setObservable(true);
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(objectTitle);
        getAttributes().setObservable();
    }
}
