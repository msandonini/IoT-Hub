package it.unimore.iot.project.smartobjects.models.sensors;

import java.util.Random;

public class LightSmartSensor extends GenericSensor<Double> {
    public static final double[] SIM_DAY_VALUES = {
            //107527.0,   // Sunlight
            10752.0,    // Full daylight
            1075,       // Overcast day
            107         // Very dark day
    };

    public static final double[] SIM_NIGHT_VALUES = {
            0.108,      // Full moon
            0.0108,     // Quarter moon
            0.0011,     // Starlight
            //0.0001      // Overcast night
    };
    public static final double SIM_TWILIGHT_VALUE = 10.8;
    public static final double SIM_DEEP_TWILIGHT_VALUE = 1.08;

    public static final int SIM_DAY_NIGHT_CYCLE_DURATION_S = 60;
    public static final double SIM_FIRST_DAY_MAX_LUX = SIM_DAY_VALUES[0];
    public static final double SIM_FIRST_DAY_TWILIGHT_LUX = SIM_TWILIGHT_VALUE;
    public static final double SIM_FIRST_NIGHT_MIN_LUX = SIM_NIGHT_VALUES[2];

    private transient double simDayMaxLux;
    private transient double simNightMinLux;
    private transient double simTwilightLux;

    private transient long lastUpdateTimestamp;

    private transient Random random;

    public LightSmartSensor() {
        super();
    }

    public LightSmartSensor(long timerUpdatePeriod, long timerTaskDelayTime) {
        super(timerUpdatePeriod, timerTaskDelayTime);
    }

    @Override
    protected void init() {
        this.random = new Random();

        this.simDayMaxLux = SIM_FIRST_DAY_MAX_LUX;
        this.simNightMinLux = SIM_FIRST_NIGHT_MIN_LUX;
        this.simTwilightLux = SIM_FIRST_DAY_TWILIGHT_LUX;
        this.lastUpdateTimestamp = System.currentTimeMillis();

        setValue(0.0);
    }

    @Override
    protected void updateMeasurement() {
        double timeElapsed = (double) (System.currentTimeMillis() - this.lastUpdateTimestamp) / 1000;

        if (timeElapsed >= SIM_DAY_NIGHT_CYCLE_DURATION_S) {        // If new day
            this.lastUpdateTimestamp = System.currentTimeMillis();
            timeElapsed = 0;

            this.simDayMaxLux = SIM_DAY_VALUES[random.nextInt(SIM_DAY_VALUES.length)];
            this.simNightMinLux = SIM_NIGHT_VALUES[random.nextInt(SIM_NIGHT_VALUES.length)];
            this.simTwilightLux = random.nextBoolean() ? SIM_TWILIGHT_VALUE : SIM_DEEP_TWILIGHT_VALUE;
        }

        double cyclePercentage = timeElapsed / SIM_DAY_NIGHT_CYCLE_DURATION_S;
        double angle = cyclePercentage * Math.PI * 2;

        double y = Math.sin(angle);

        double light;

        if (y >= 0) {    // Daytime - Positive sine
            light = y * (this.simDayMaxLux - this.simTwilightLux) + this.simTwilightLux;
        } else {        // Nighttime - Negative sine
            light = (this.simTwilightLux + (y * (this.simTwilightLux - this.simNightMinLux) + this.simNightMinLux));
        }

        setValue(light);
    }
}
