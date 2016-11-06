package edu.gvsu.cis.campbjos.ftp.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Results {

    @SerializedName("results")
    @Expose
    private List<Result> resultList;

    public Results() {
        resultList = new ArrayList<>();
    }

    public void addResult(Result result) {
        resultList.add(result);
    }

    public void addResult(Host host, String filename) {
        resultList.add(new Result(host, filename));
    }

    public List<Result> list() {
        return resultList;
    }

}
