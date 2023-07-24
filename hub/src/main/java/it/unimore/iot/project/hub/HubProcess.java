package it.unimore.iot.project.hub;

import it.unimore.iot.project.hub.http.services.AppService;

/**
 * A class containing an AppService singleton instantiation and the service starting function
 *
 * @see AppService
 *
 * @author Sandonini Mirco
 */
public class HubProcess {
    private static HubProcess instance = null;

    private AppService service = null;

    private HubProcess() {
        this.service = new AppService();
    }

    public static HubProcess getInstance() {
        if (instance == null) {
            instance = new HubProcess();
        }

        return instance;
    }

    /**
     *
     * @param args the arguments to specify the server configuration. If none are passed it automatically tries to read the configuration.yml file
     * @throws Exception if something goes wrong
     */
    public void start(String[] args) throws Exception {
        service.run("server", args.length > 0 ? args[0] : "configuration.yml");
    }

    public static void main(String[] args) {
        try {
            HubProcess.getInstance().start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
