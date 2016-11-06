package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.SimpleStringProperty;

public class Result {

    @SerializedName("speed")
    @Expose
    public String speed;
    @SerializedName("hostname")
    @Expose
    public String hostname;

    @SerializedName("filename")
    @Expose
    public String filename;

    private final SimpleStringProperty filenameProperty;
    private final SimpleStringProperty hostnameProperty;
    private final SimpleStringProperty speedProperty;

    public Result(Host host, String filename) {
        this.speed = host.speed;
        this.filename = filename;
        this.hostname = host.getHostname();

        filenameProperty = new SimpleStringProperty(this.filename);
        speedProperty = new SimpleStringProperty(speed);
        hostnameProperty = new SimpleStringProperty(hostname);
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
