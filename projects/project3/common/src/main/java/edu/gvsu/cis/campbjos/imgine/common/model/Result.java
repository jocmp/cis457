package edu.gvsu.cis.campbjos.imgine.common.model;

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
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;

    public Result(Host host, String filename) {
        this.host = new Host.Builder()
                .setIp(host.ip)
                .setPort(host.port)
                .setUsername(host.username).createHost();
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
