package it.unimore.iot.project.smartobjects.models.actuators;

import it.unimore.iot.project.smartobjects.listeners.DataListenerManager;
import it.unimore.iot.project.smartobjects.listeners.ResourceDataListener;

public abstract class GenericActuator<T_READING, T_UPDATING> extends DataListenerManager<T_UPDATING> implements ResourceDataListener<T_READING> {
    protected boolean isActive;
    protected T_UPDATING status;
    protected T_READING savedValue;

    public GenericActuator() {
        this.isActive = true;
    }

    public void toggleActive() {
        setActive(!isActive);
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        onDataChanged(savedValue);
    }

    public T_READING getSavedValue() {
        return savedValue;
    }

    public void setSavedValue(T_READING savedValue) {
        this.savedValue = savedValue;
    }

    public T_UPDATING getStatus() {
        return status;
    }

    public void setStatus(T_UPDATING status) {
        if (this.status != status) {
            this.status = status;
            notifyListeners(status);
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
