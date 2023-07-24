package it.unimore.iot.project.smartobjects.models.actuators;

import it.unimore.iot.project.smartobjects.listeners.ResourceDataListener;

public class ShutterActuator extends GenericActuator<Boolean, Integer> {

    public static final int CLOSED_SHUTTER_VALUE = 100;
    public static final int OPENED_SHUTTER_VALUE = 0;

    private HumidityActivatedActuator humidityActivatedActuator;

    private boolean lightActivatedValue;
    private boolean humidityActivatedValue;

    public ShutterActuator(HumidityActivatedActuator humidityActivatedActuator) {
        super();

        this.humidityActivatedActuator = humidityActivatedActuator;

        this.humidityActivatedActuator.addDataListener(new ResourceDataListener<Boolean>() {
            @Override
            public void onDataChanged(Boolean value) {
                humidityActivatedValue = value;

                changeStatus();
            }
        });
    }

    public HumidityActivatedActuator getHumidityActivatedActuator() {
        return humidityActivatedActuator;
    }

    public void setHumidityActivatedActuator(HumidityActivatedActuator humidityActivatedActuator) {
        this.humidityActivatedActuator = humidityActivatedActuator;
    }

    public void changeStatus() {
        if (this.isActive() && (lightActivatedValue || humidityActivatedValue)) {
            //System.out.println("STATUS CHANGED: CLOSED");
            setStatus(CLOSED_SHUTTER_VALUE);
        } else if (this.isActive()) {
            //System.out.println("STATUS CHANGED: OPENED");
            setStatus(OPENED_SHUTTER_VALUE);
        }
    }

    @Override
    public void onDataChanged(Boolean value) {
        if (value == null) {
            value = false;
        }

        this.lightActivatedValue = value;
        changeStatus();
    }
}
