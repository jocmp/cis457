package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Host {

    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("port")
    @Expose
    private Integer port;
    @SerializedName("hostname")
    private String hostname;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
