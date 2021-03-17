package com.project.tmc.Models.uploadtext;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;
import com.project.tmc.Models.sabha.SabhaResult;

import java.io.Serializable;

public class UploadImageUrlResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private SabhaResult result;

    public UploadImageUrlResponse(String version, Integer statusCode, String message, SabhaResult result) {
        super(version, statusCode, message);
        this.result = result;
    }

    public SabhaResult getResult() {
        return result;
    }
}
