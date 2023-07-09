package it.unimore.iot.project.smartobjects.resources.sensors;

import com.google.gson.Gson;
import it.unimore.dipi.iot.utils.CoreInterfaces;
import it.unimore.iot.project.smartobjects.models.sensors.GenericSensor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public abstract class CoapSenmlSensorResource<T_SENSOR extends GenericSensor<?>> extends CoapResource {
    protected final String objectTitle;

    protected final String senmlRt;
    protected final String senmlUnit;

    protected final Number sensorVersion;

    protected String deviceId;
    protected Number updatedValue;

    protected T_SENSOR sensor;

    public CoapSenmlSensorResource(String deviceId, String objectTitle, String name, String senmlRt, String senmlUnit,
                                   Number sensorVersion, T_SENSOR sensor) {
        super(name);

        this.deviceId = deviceId;
        this.objectTitle = objectTitle;
        this.senmlRt = senmlRt;
        this.senmlUnit = senmlUnit;
        this.sensorVersion = sensorVersion;
        this.sensor = sensor;

        init();
    }

    private void init() {
        setObservable(true);
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(this.objectTitle);
        getAttributes().setObservable();
        getAttributes().addAttribute("rt", this.senmlRt);
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

        this.sensor.addDataListener((value) -> {
            updatedValue = (Number) value;
            changed();
        });
    }

}
