package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static java.lang.String.format;

public class Host {

    @SerializedName("ip")
    @Expose
    public String ip;
    @SerializedName("port")
    @Expose
    public Integer port;
    @SerializedName("hostname")
    public String hostname;
    @SerializedName("speed")
    public String speed;

    public Host(String ip, Integer port, String hostname, String speed) {
        this.ip = ip;
        this.port = port;
        this.hostname = hostname;
        this.speed = speed;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        Host host = (Host) other;

        if (ip != null ? !ip.equals(host.ip) : host.ip != null)
            return false;
        if (port != null ? !port.equals(host.port) : host.port != null)
            return false;
        return hostname != null ? hostname.equals(host.hostname) : host.hostname == null;

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (hostname != null ? hostname.hashCode() : 0);
        return result;
    }

    public String getHostname() {
        return format("%s/%s", hostname, ip);
    }

    @Override
    public String toString() {
        return getHostname();
    }
}
