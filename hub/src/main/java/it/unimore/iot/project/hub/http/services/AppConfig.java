package it.unimore.iot.project.hub.http.services;

import io.dropwizard.Configuration;
import it.unimore.iot.project.hub.http.persistence.DeviceManager;

public class AppConfig extends Configuration {
    public DeviceManager getDeviceManager() {
        return DeviceManager.getInstance();
    }
}
