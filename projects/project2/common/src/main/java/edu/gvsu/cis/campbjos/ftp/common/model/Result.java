package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("hostname")
    @Expose
    private Host host;
    @SerializedName("filename")
    @Expose
    private String filename;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Host getHostname() {
        return host;
    }

    public void setHostname(Host host) {
        this.host = host;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
