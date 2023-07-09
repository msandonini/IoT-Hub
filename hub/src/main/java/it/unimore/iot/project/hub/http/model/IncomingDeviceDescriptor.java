package it.unimore.iot.project.hub.http.models;

public class IncomingDeviceDescriptor {
    private String name;
    private String address;
    private int port;

    public IncomingDeviceDescriptor() {
    }

    public IncomingDeviceDescriptor(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
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
}
