package it.unimore.iot.project.smartobjects.models.sensors;

import it.unimore.iot.project.smartobjects.models.actuators.GenericActuator;

import java.util.Random;

public class HumiditySmartSensor extends GenericSensor<Double> {

    private static final double MIN_HUMIDITY_VALUE = 0.0;
    private static final double MAX_HUMIDITY_VALUE = 100.0;

    private static final double MIN_HUMIDITY_VARIATION = 0.1;
    private static final double MAX_HUMIDITY_VARIATION = 1.0;

    private transient Random random;

    public HumiditySmartSensor() {
        super();
    }

    public HumiditySmartSensor(long timerUpdatePeriod, long timerTaskDelayTime) {
        super(timerUpdatePeriod, timerTaskDelayTime);
    }

    @Override
    protected void init() {
        this.random = new Random();

        setValue(this.random.nextDouble(MIN_HUMIDITY_VALUE, MAX_HUMIDITY_VALUE));
    }

    @Override
    protected void updateMeasurement() {
        double variation = (random.nextDouble(MIN_HUMIDITY_VARIATION, MAX_HUMIDITY_VARIATION));
        int sign = random.nextBoolean() ? -1 : 1;

        setValue(getValue() + (sign * variation));

        if (getValue() > MAX_HUMIDITY_VALUE) {
            setValue(MAX_HUMIDITY_VALUE);
        }
        else if (getValue() < MIN_HUMIDITY_VALUE) {
            setValue(MIN_HUMIDITY_VALUE);
        }
    }

}
