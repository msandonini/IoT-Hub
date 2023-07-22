package it.unimore.iot.project.smartobjects.models.actuators;

public class LightActivatedShutterActuator extends GenericActuator<Boolean, Integer> {

    public static final int CLOSED_SHUTTER_VALUE = 100;
    public static final int OPENED_SHUTTER_VALUE = 0;

    public LightActivatedShutterActuator() {
        super();
    }

    @Override
    public void onDataChanged(Boolean value) {
        if (value == null) {
            value = false;
        }

        if (this.isActive() && value) {
            setStatus(CLOSED_SHUTTER_VALUE);
        } else if (this.isActive()) {
            setStatus(OPENED_SHUTTER_VALUE);
        }
    }
}
