package it.unimore.iot.project.hub.http.persistence;

import it.unimore.iot.project.hub.coap.model.DeviceDescriptor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

public class DeviceManager {
    private static final String DATA_STORE_PATH = "data.csv";

    private static DeviceManager instance = null;
    private HashMap<String, DeviceDescriptor> deviceMap;

    public static DeviceManager instantiate() {
        return getInstance();
    }

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }

        return instance;
    }

    private DeviceManager() {
        this.deviceMap = new HashMap<>();

        Executors.newSingleThreadExecutor().submit(this::loadSavedData);
    }

    private void loadSavedData() {
        System.out.println("[DEVICE MANAGER] - [STATUS] Loading stored data");

        try(BufferedReader reader = new BufferedReader(new FileReader(new File(DeviceManager.class.getClassLoader().getResource(DATA_STORE_PATH).toURI())))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");

                if (split.length == 3) {
                    addDeviceWithoutStoring(new DeviceDescriptor(split[0], split[1], Integer.parseInt(split[2])));
                }
            }
        } catch (Exception e) {
            System.out.println("[DEVICE MANAGER] - [STATUS] Error in loading stored data");
            e.printStackTrace();
        }
    }

    public Map<String, DeviceDescriptor> getDeviceMap() {
        return deviceMap;
    }

    public DeviceDescriptor getDevice(String deviceName) {
        return this.deviceMap.get(deviceName);
    }

    public String addDevice(DeviceDescriptor device) {
        String name = addDeviceWithoutStoring(device);

        storeDevice(device);

        return name;
    }

    private String addDeviceWithoutStoring(DeviceDescriptor device) {
        System.out.println("[ADDING DEVICE]");

        if (this.deviceMap.containsValue(device)) {
            return deviceMap.entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(entry.getValue(), device))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .toString();
        }

        StringBuilder name = new StringBuilder(device.getName());
        if (this.deviceMap.containsKey(name.toString())) {
            int n = 1;
            boolean accept = false;
            while (!accept) {
                if (!this.deviceMap.containsKey(name + "-" + n)) {
                    name.append("-").append(n);
                    accept = true;
                }
                n ++;
            }
        }

        this.deviceMap.put(name.toString(), device);

        System.out.println("[ADDED DEVICE] - [NAME] " + name);

        return name.toString();
    }

    private void storeDevice(DeviceDescriptor device) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(DeviceManager.class.getClassLoader().getResource(DATA_STORE_PATH).toURI()), true))) {
            writer.write(String.format("%s;%s;%d", device.getName(), device.getAddress(), device.getPort()));
            writer.newLine();
        } catch (Exception e) {
            System.out.println("[ERROR] - [DESCRIPTION] Failed to store device");
            e.printStackTrace();
        }
    }

    public boolean removeDevice(String deviceName) {
        boolean result = this.deviceMap.remove(deviceName) != null;

        if (result) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(DeviceManager.class.getClassLoader().getResource(DATA_STORE_PATH).toURI())))) {
                for (DeviceDescriptor device: this.deviceMap.values()) {
                    writer.write(String.format("%s;%s;%d", device.getName(), device.getAddress(), device.getPort()));
                    writer.newLine();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] - [DESCRIPTION] Failed to store updated map");
            }
        }

        return result;
    }
}
