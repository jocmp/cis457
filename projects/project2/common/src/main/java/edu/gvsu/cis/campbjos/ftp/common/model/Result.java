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

    private final SimpleStringProperty filenameProperty;
    private final SimpleStringProperty hostnameProperty;
    private final SimpleStringProperty speedProperty;

    public Result(Host host, String filename) {
        this.filename = filename;
        this.host = new Host.Builder()
                .setIp(host.ip)
                .setPort(host.port)
                .setHostname(host.hostname)
                .setSpeed(host.speed)
                .setUsername(host.username).createHost();
        filenameProperty = new SimpleStringProperty(this.filename);
        speedProperty = new SimpleStringProperty(this.host.speed);
        hostnameProperty = new SimpleStringProperty(this.host.hostname);
    }

    public String getSpeed() {
        return speedProperty.get();
    }

    public String getHostname() {
        return hostnameProperty.get();
    }

    public String getFilename() {
        return filenameProperty.get();
    }

    @Override
    public String toString() {
        return filename;
    }
}
