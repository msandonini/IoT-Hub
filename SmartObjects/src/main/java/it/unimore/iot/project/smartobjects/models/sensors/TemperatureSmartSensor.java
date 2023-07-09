package it.unimore.iot.project.smartobjects.models.sensors;

import java.util.Random;

public class TemperatureSmartSensor extends GenericSensor<Double> {
    private static final double MIN_TEMPERATURE_VALUE = 18.0;
    private static final double MAX_TEMPERATURE_VALUE = 32.0;

    private static final double MIN_TEMPERATURE_VARIATION = 0.1;
    private static final double MAX_TEMPERATURE_VARIATION = 1.0;

    private static final String RESOURCE_TYPE = "iot.sensor.temperature";

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

        setValue(this.random.nextDouble(MIN_TEMPERATURE_VALUE, MAX_TEMPERATURE_VALUE));
    }

    @Override
    protected void updateMeasurement() {
        double variation = (random.nextDouble(MIN_TEMPERATURE_VARIATION, MAX_TEMPERATURE_VARIATION));
        int sign = random.nextBoolean() ? -1 : 1;

        setValue(getValue() + (sign * variation));

        if (getValue() > MAX_TEMPERATURE_VALUE) {
            setValue(MAX_TEMPERATURE_VALUE);
        }
        else if (getValue() < MIN_TEMPERATURE_VALUE) {
            setValue(MIN_TEMPERATURE_VALUE);
        }
    }
}
