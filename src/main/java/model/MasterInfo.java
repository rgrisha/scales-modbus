package model;

import java.util.Objects;

public class MasterInfo {

    @ConfigProperty
    private String name;

    @ConfigProperty(nameInFile = "addr")
    private String address;

    @ConfigProperty
    private int port;

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
                address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, port);
    }

    @Override
    public String toString() {
        return "MasterInfo{" +
                "name='" + name + '\'' +
                ", addr='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
