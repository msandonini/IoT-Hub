package it.unimore.iot.project.smartobjects.models.sensors;

import java.util.Random;

public class HumiditySmartSensor extends GenericSensor<Double> {

    private static final double SIM_MIN_HUMIDITY_VALUE = 0.0;
    private static final double SIM_MAX_HUMIDITY_VALUE = 100.0;

    private static final double SIM_MIN_HUMIDITY_VARIATION = 0.1;
    private static final double SIM_MAX_HUMIDITY_VARIATION = 1.0;

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

        setValue(this.random.nextDouble(SIM_MIN_HUMIDITY_VALUE, SIM_MAX_HUMIDITY_VALUE));
    }

    @Override
    protected void updateMeasurement() {
        double variation = (random.nextDouble(SIM_MIN_HUMIDITY_VARIATION, SIM_MAX_HUMIDITY_VARIATION));
        int sign = random.nextBoolean() ? -1 : 1;

        setValue(getValue() + (sign * variation));

        if (getValue() > SIM_MAX_HUMIDITY_VALUE) {
            setValue(SIM_MAX_HUMIDITY_VALUE);
        }
        else if (getValue() < SIM_MIN_HUMIDITY_VALUE) {
            setValue(SIM_MIN_HUMIDITY_VALUE);
        }
    }

}
