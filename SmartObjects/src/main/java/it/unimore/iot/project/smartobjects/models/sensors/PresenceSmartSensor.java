package it.unimore.iot.project.smartobjects.models.sensors;

import java.util.Random;

public class PresenceSmartSensor extends GenericSensor<Integer> {

    private static final int SIM_MIN_PEOPLE_COUNT = 0;
    private static final int SIM_MAX_PEOPLE_COUNT = 5;

    private static final int SIM_MIN_PEOPLE_VARIATION = 0;
    private static final int SIM_MAX_PEOPLE_VARIATION = 2;

    private transient Random random;

    public PresenceSmartSensor() {
        super();
    }

    public PresenceSmartSensor(long timerUpdatePeriod, long timerTaskDelayTime) {
        super(timerUpdatePeriod, timerTaskDelayTime);
    }

    @Override
    protected void init() {
        this.random = new Random();

        setValue(this.random.nextInt(SIM_MIN_PEOPLE_COUNT, SIM_MAX_PEOPLE_COUNT));
    }

    @Override
    protected void updateMeasurement() {
        int variation = this.random.nextInt(SIM_MIN_PEOPLE_VARIATION, SIM_MAX_PEOPLE_VARIATION);
        int sign = random.nextBoolean() ? -1 : 1;

        int nVal = getValue() + sign * variation;

        if (nVal > SIM_MAX_PEOPLE_COUNT) {
            nVal = SIM_MAX_PEOPLE_COUNT;
        }
        else if (nVal < SIM_MIN_PEOPLE_COUNT) {
            nVal = SIM_MIN_PEOPLE_COUNT;
        }

        setValue(nVal);
    }
}
