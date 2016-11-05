package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public Result(Host host, String filename) {
        this.speed = host.speed;
        this.filename = filename;
        this.hostname = host.hostname;
    }
}
