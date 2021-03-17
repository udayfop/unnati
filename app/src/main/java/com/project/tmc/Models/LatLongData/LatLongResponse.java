package com.project.tmc.Models.LatLongData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class LatLongResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private ArrayList<LatLongResult> result;

    public LatLongResponse(String version, Integer statusCode, String message, ArrayList<LatLongResult> result) {
        super(version, statusCode, message);
        this.result = result;
    }
    public ArrayList<LatLongResult> getResult() {
        return result;
    }
}
