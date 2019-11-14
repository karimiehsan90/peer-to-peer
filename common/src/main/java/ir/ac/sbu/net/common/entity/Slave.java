package ir.ac.sbu.net.common.entity;

import java.util.List;
import java.util.Objects;

public class Slave {
    private List<String> files;
    private String ip;
    private int port;

    public Slave(String ip, int port, List<String> files) {
        this.ip = ip;
        this.port = port;
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slave slave = (Slave) o;
        return port == slave.port &&
                Objects.equals(ip, slave.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
