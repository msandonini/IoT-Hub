package it.unimore.iot.project.smartobjects.models.actuators;

import java.awt.*;

public class LightSmartActuator extends GenericActuator<Color, Integer> {

    public static final int STATUS_OFF = -1;
    public static final int STATUS_DARK = 0;
    public static final int STATUS_LIGHT = 1;

    public static final Color DEFAULT_COLOR = Color.WHITE;

    public LightSmartActuator() {
        this(DEFAULT_COLOR);
    }

    public LightSmartActuator(Color color) {
        if (color == null || color.equals(Color.BLACK)) {
            setColor(DEFAULT_COLOR);
        }
        else {
            setColor(color);
        }
    }

    public void setColor(Color value) {
        setSavedValue(value);
    }

    public Color getColor() {
        return this.savedValue;
    }

    public static String getStatusDescription(int status) {
        switch (status) {
            case STATUS_OFF -> {
                return "Off";
            }
            case STATUS_DARK -> {
                return "Dark";
            }
            case STATUS_LIGHT -> {
                return "Light";
            }
            default -> {
                return "Unknown";
            }
        }
    }

    @Override
    public void onDataChanged(Color value) {
        if (value == null) {
            value = DEFAULT_COLOR;
        }
        setSavedValue(value);

        if (this.isActive()) {
            if (this.getColor().getRed() == 0 && this.getColor().getGreen() == 0 && this.getColor().getBlue() == 0) {
                setStatus(STATUS_DARK);
            }
            else {
                setStatus(STATUS_LIGHT);
            }
        } else {
            setStatus(STATUS_OFF);
        }
    }

    @Override
    public String toString() {
        return "LightSmartActuator{" +
                "color=" + savedValue.getRGB() +
                ", status=" + status +
                '}';
    }
}
