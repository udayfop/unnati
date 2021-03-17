package com.project.tmc.Models.PhotoUpload;

import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;

public class PhotoUploadResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    private String result;

    public PhotoUploadResponse(String version, Integer statusCode, String message, String result) {
        super(version, statusCode, message);
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
