package com.project.tmc.Models.MobileVerification;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.Theme.ThemeResult;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;

public class MobileVerificationResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private MobileVerificationResult result;

    public MobileVerificationResponse(String version, Integer statusCode, String message, MobileVerificationResult result) {
        super(version, statusCode, message);
        this.result = result;
    }

    public MobileVerificationResult getResult() {
        return result;
    }
}
