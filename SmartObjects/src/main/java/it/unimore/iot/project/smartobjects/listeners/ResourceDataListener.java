package it.unimore.iot.project.smartobjects.listeners;

public interface ResourceDataListener<T_DATATYPE> {

    void onDataChanged(T_DATATYPE value);
}
