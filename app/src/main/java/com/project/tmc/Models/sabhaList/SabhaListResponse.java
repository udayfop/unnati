package com.project.tmc.Models.sabhaList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class SabhaListResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private ArrayList<SabhaListResult> result;

    public SabhaListResponse(String version, Integer statusCode, String message, ArrayList<SabhaListResult> result) {
        super(version, statusCode, message);
        this.result = result;
    }

    public ArrayList<SabhaListResult> getResult() {
        return result;
    }
}
