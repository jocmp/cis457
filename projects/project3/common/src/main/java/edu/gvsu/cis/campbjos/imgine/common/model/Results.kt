package edu.gvsu.cis.campbjos.imgine.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Results {

    @SerializedName("results")
    @Expose
    private val resultList: MutableList<Result>

    init {
        resultList = ArrayList<Result>()
    }

    fun addResult(result: Result) {
        resultList.add(result)
    }

    fun list(): List<Result> {
        return resultList
    }

    fun clear() {
        resultList.clear()
    }

    fun addAll(results: Results?) {
        if (results == null) {
            return
        }
        resultList.addAll(results.list())
    }

}
