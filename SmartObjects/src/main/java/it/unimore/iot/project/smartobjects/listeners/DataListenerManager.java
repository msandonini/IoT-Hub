package it.unimore.iot.project.smartobjects.listeners;

import java.util.HashSet;
import java.util.Set;

public abstract class DataListenerManager<T_DATATYPE> {
    protected transient Set<ResourceDataListener<T_DATATYPE>> listeners;

    public DataListenerManager() {
        this.listeners = new HashSet<>();
    }

    public void addDataListener(ResourceDataListener<T_DATATYPE> listener) {
        this.listeners.add(listener);
    }

    public void removeDataListener(ResourceDataListener<T_DATATYPE> listener) {
        this.listeners.remove(listener);
    }

    protected void notifyListeners(T_DATATYPE value) {
        if (this.listeners == null) {
            return;
        }

        for (ResourceDataListener<T_DATATYPE> listener : this.listeners) {
            listener.onDataChanged(value);
        }
    }
}
