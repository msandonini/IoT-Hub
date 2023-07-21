package it.unimore.iot.project.smartobjects.models.sensors;

import java.util.Random;

public class LightSmartSensor extends GenericSensor<Double> {
    public static final double SIM_SUNLIGHT_LUX = 107527.0;
    public static final double SIM_FULL_DAYLIGHT_LUX = 10752.0;
    public static final double SIM_OVERCAST_LUX = 1075;
    public static final double SIM_DARK_DAY_LUX = 107;
    public static final double SIM_TWILIGHT_LUX = 10.8;
    public static final double SIM_DEEP_TWILIGHT_LUX = 1.08;
    public static final double SIM_FULL_MOON_LUX = 0.108;
    public static final double SIM_QUARTER_MOON_LUX = 0.0108;
    public static final double SIM_STARLIGHT_LUX = 0.0011;
    public static final double SIM_OVERCAST_NIGHT_LUX = 0.0001;

    public static final int SIM_DAY_CYCLE_DURATION_S = 60;
    public static final int SIM_NIGHT_CYCLE_DURATION_S = 60;

    public static final double SIM_START_LUX = SIM_SUNLIGHT_LUX;
    public static final double SIM_FIRST_DAY_MAX_LUX = SIM_SUNLIGHT_LUX;
    public static final double SIM_FIRST_NIGHT_MIN_LUX = SIM_OVERCAST_NIGHT_LUX;
    public static final int SIM_START_DIRECTION_SIGN = -1;          // +1 = Ascending lux, -1 = Descending lux

    private transient double simDayMaxLux;
    private transient double simNightMinLux;

    private transient int simDirectionSign;

    private transient long lastUpdateTimestamp;

    private transient Random random;

    @Override
    protected void init() {
        this.random = new Random();

        this.simDayMaxLux = SIM_FIRST_DAY_MAX_LUX;
        this.simNightMinLux = SIM_FIRST_NIGHT_MIN_LUX;
        this.simDirectionSign = SIM_START_DIRECTION_SIGN;
        this.lastUpdateTimestamp = System.currentTimeMillis();

        setValue(SIM_START_LUX);
    }

    @Override
    protected void updateMeasurement() {
        // TODO: Implement day-night cycle simulation with new day & night condition at each cycle
    }
}
