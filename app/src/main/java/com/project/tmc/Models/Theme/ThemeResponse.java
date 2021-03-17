package com.project.tmc.Models.Theme;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;

public class ThemeResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private ThemeResult result;

    public ThemeResponse(String version, Integer statusCode, String message, ThemeResult result) {
        super(version, statusCode, message);
        this.result = result;
    }

    public ThemeResult getResult() {
        return result;
    }
}
