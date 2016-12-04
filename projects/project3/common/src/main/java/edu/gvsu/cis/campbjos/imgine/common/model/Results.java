package edu.gvsu.cis.campbjos.imgine.common.model;

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

    public List<Result> list() {
        return resultList;
    }

    public void clear() {
        resultList.clear();
    }

    public void addAll(Results results) {
        if (results == null) {
            return;
        }
        resultList.addAll(results.list());
    }
}
