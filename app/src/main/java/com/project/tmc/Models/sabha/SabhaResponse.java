package com.project.tmc.Models.sabha;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SabhaResponse extends ResponseStatus implements Serializable {

@SerializedName("Result")
    @Expose
    private SabhaResult result;

    public SabhaResponse(String version, Integer statusCode, String message, SabhaResult result) {
        super(version, statusCode, message);
        this.result = result;
    }

    public SabhaResult getResult() {
        return result;
    }
}
