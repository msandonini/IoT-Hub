package it.unimore.iot.project.hub.http.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import it.unimore.iot.project.hub.http.conf.DefaultCORSFilter;
import it.unimore.iot.project.hub.http.persistence.DeviceManager;
import it.unimore.iot.project.hub.http.resources.DeviceDetailResource;
import it.unimore.iot.project.hub.http.resources.DeviceListResource;

public class AppService extends Application<AppConfig> {
    @Override
    public void run(AppConfig configuration, Environment environment) throws Exception {
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
