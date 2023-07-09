package it.unimore.iot.project.hub;

import it.unimore.iot.project.hub.http.services.AppService;

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
