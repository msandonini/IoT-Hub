package it.unimore.iot.project.hub.http.services;

import io.dropwizard.Configuration;
import it.unimore.iot.project.hub.http.persistence.DeviceManager;

/**
 * The server configuration class. It contains all the methods to give to the resources the objects they need.
 *
 * @see Configuration
 * @see DeviceManager
 *
 * @author Sandonini Mirco
 */
public class AppConfig extends Configuration {
    public DeviceManager getDeviceManager() {
        return DeviceManager.getInstance();
    }
}
