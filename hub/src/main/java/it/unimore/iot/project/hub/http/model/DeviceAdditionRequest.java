package it.unimore.iot.project.hub.http.model;

public class DeviceAdditionRequest {
    private String name;
    private String address;
    private int port;

    private boolean postSupport;

    private boolean putSupport;

    public DeviceAdditionRequest() {
    }

    public DeviceAdditionRequest(String name, String address, int port) {
        this(name, address, port, false, false);
    }

    public DeviceAdditionRequest(String name, String address, int port, boolean postSupport, boolean putSupport) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.postSupport = postSupport;
        this.putSupport = putSupport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean hasPostSupport() {
        return postSupport;
    }

    public void setPostSupport(boolean postSupport) {
        this.postSupport = postSupport;
    }

    public boolean hasPutSupport() {
        return putSupport;
    }

    public void setPutSupport(boolean putSupport) {
        this.putSupport = putSupport;
    }
}
