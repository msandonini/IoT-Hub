package it.unimore.iot.project.smartobjects.models.sensors;

import java.util.Random;

public class TemperatureSmartSensor extends GenericSensor<Double> {
    private static final double SIM_MIN_TEMPERATURE_VALUE = 18.0;
    private static final double SIM_MAX_TEMPERATURE_VALUE = 32.0;

    private static final double SIM_MIN_TEMPERATURE_VARIATION = 0.1;
    private static final double SIM_MAX_TEMPERATURE_VARIATION = 1.0;

    private transient Random random;


    public TemperatureSmartSensor() {
        super();
    }

    public TemperatureSmartSensor(long timerUpdatePeriod, long timerTaskDelayTime) {
        super(timerUpdatePeriod, timerTaskDelayTime);
    }

    @Override
    protected void init() {
        this.random = new Random();

        setValue(this.random.nextDouble(SIM_MIN_TEMPERATURE_VALUE, SIM_MAX_TEMPERATURE_VALUE));
    }

    @Override
    protected void updateMeasurement() {
        double variation = this.random.nextDouble(SIM_MIN_TEMPERATURE_VARIATION, SIM_MAX_TEMPERATURE_VARIATION);
        int sign = random.nextBoolean() ? -1 : 1;

        double nVal = getValue() + sign * variation;

        if (getValue() > SIM_MAX_TEMPERATURE_VALUE) {
            nVal = SIM_MAX_TEMPERATURE_VALUE;
        }
        else if (getValue() < SIM_MIN_TEMPERATURE_VALUE) {
            nVal = SIM_MIN_TEMPERATURE_VALUE;
        }

        setValue(nVal);
    }
}
