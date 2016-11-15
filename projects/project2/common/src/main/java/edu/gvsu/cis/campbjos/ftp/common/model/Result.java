package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.SimpleStringProperty;

public class Result {

    @SerializedName("filename")
    @Expose
    public String filename;
    @SerializedName("host")
    @Expose
    public Host host;
    @SerializedName("description")
    @Expose
    public String description;

    public Result(Host host, String filename) {
        this.host = new Host.Builder()
                .setIp(host.ip)
                .setPort(host.port)
                .setHostname(host.hostname)
                .setSpeed(host.speed)
                .setUsername(host.username).createHost();
    }

    public String getSpeed() {
        return new SimpleStringProperty(this.host.speed).get();
    }

    public String getHostname() {
        return new SimpleStringProperty(String.format("%s:%s", this.host.hostname, this.host.port)).get();
    }

    public String getFilename() {
        return new SimpleStringProperty(this.filename).get();
    }

    public boolean containsKeyword(final String keyword) {
        String lowercaseKeyword = keyword.toLowerCase();
        return filename.toLowerCase().contains(lowercaseKeyword) ||
                description.toLowerCase().contains(lowercaseKeyword);
    }

    @Override
    public String toString() {
        return filename;
    }
}
