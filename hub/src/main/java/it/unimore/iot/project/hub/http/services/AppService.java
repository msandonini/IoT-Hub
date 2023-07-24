package it.unimore.iot.project.hub.http.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import it.unimore.iot.project.hub.http.conf.DefaultCORSFilter;
import it.unimore.iot.project.hub.http.persistence.DeviceManager;
import it.unimore.iot.project.hub.http.resources.DeviceDetailResource;
import it.unimore.iot.project.hub.http.resources.DeviceListResource;

/**
 * The service application. The class contains the starter method to start a dropwizard application using the AppConfig configuration class.
 *
 * @see Application
 * @see AppConfig
 *
 * @author Sandonini Mirco
 */
public class AppService extends Application<AppConfig> {

    /**
     * The server starting method. It initializes the filters along with the resources and instantiates the DeviceManager object.
     *
     * @param configuration the parsed AppConfig configuration object
     * @param environment   the application's {@link Environment}
     *
     * @see AppConfig
     * @see Environment
     * @see DefaultCORSFilter
     * @see DeviceManager
     */
    @Override
    public void run(AppConfig configuration, Environment environment) {
        System.out.println("[STARTING][CORS]");

        environment.jersey().register(DefaultCORSFilter.class);

        System.out.println("[REGISTERING RESOURCES]");

        environment.jersey().register(new DeviceListResource(configuration));
        environment.jersey().register(new DeviceDetailResource(configuration));

        System.out.println("[RESOURCES REGISTERED]");

        System.out.println("[INSTANTIATING DEVICE MANAGER]");

        DeviceManager.instantiate();

        System.out.println("[DEVICE MANAGER INSTANTIATED]");

        System.out.println("[SERVICE RUNNING]");
        }
}
