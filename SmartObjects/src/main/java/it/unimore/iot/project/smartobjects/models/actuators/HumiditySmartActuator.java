package it.unimore.iot.project.smartobjects.models.actuators;

public class HumiditySmartActuator extends GenericActuator<Double, Integer>{
    public static final int STATUS_OFF = -1;
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_DEHUMIFIFYING = 1;
    public static final int STATUS_HUMIDIFYING = 2;

    public static final double DEFAULT_HUMIDITY_DELTA = 2.5;
    public static final double DEFAULT_WANTED_HUMIDITY = 50.0;

    private double wantedHumidity;
    private double delta;

    public HumiditySmartActuator() {
        this(DEFAULT_WANTED_HUMIDITY, DEFAULT_HUMIDITY_DELTA);
    }

    public HumiditySmartActuator(double wantedTemperature) {
        this(wantedTemperature, DEFAULT_HUMIDITY_DELTA);
    }

    public HumiditySmartActuator(double wantedTemperature, double temperatureDelta) {
        this.wantedHumidity = wantedTemperature;
        this.delta = temperatureDelta;
    }

    public double getWantedHumidity() {
        return wantedHumidity;
    }

    public void setWantedHumidity(double wantedHumidity) {
        this.wantedHumidity = wantedHumidity;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public void onDataChanged(Double value) {
        if (value == null) {
            value = wantedHumidity;
        }
        setSavedValue(value);

        if (this.isActive()) {
            if (value > wantedHumidity + delta) {
                setStatus(STATUS_DEHUMIFIFYING);
            }
            else if (value < wantedHumidity - delta) {
                setStatus(STATUS_HUMIDIFYING);
            }
            else {
                setStatus(STATUS_IDLE);
            }
        }
        else {
            setStatus(STATUS_OFF);
        }
    }

    public static String getStatusDescription(int status) {
        switch (status) {
            case STATUS_OFF -> {
                return "Off";
            }
            case STATUS_IDLE -> {
                return "Idle";
            }
            case STATUS_DEHUMIFIFYING -> {
                return "Dehumidifying";
            }
            case STATUS_HUMIDIFYING -> {
                return "Humidifying";
            }
            default -> {
                return "Unknown";
            }
        }
    }
}
