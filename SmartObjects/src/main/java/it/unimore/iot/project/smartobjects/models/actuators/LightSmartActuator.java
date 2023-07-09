package it.unimore.iot.project.smartobjects.models.actuators;

import java.awt.*;

public class LightSmartActuator extends GenericActuator<Color, Boolean> {

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
        if (value.equals(Color.BLACK)) {
            setStatus(false);
        }
        else {
            setSavedValue(value);
            setStatus(true);
        }
    }

    @Override
    public void onDataChanged(Color value) {
        setSavedValue(value);

        if (this.isActive()) {
            setColor(value);
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
