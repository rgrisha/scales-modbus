package model;

import java.util.Objects;

public class MasterInfo {

    @ConfigProperty
    private String name;

    @ConfigProperty(nameInFile = "ip")
    private String ipAddress;

    @ConfigProperty
    private int port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort(String portVal) {
        this.port = Integer.parseInt(portVal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterInfo that = (MasterInfo) o;
        return port == that.port &&
                name.equals(that.name) &&
                ipAddress.equals(that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress, port);
    }

    @Override
    public String toString() {
        return "MasterInfo{" +
                "name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }
}
