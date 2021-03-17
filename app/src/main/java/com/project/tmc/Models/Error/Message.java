package com.project.tmc.Models.Error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {
    @SerializedName("propertyName")
    @Expose
    private String propertyName;

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public Message(String propertyName, String errorMessage) {
        this.propertyName = propertyName;
        this.errorMessage = errorMessage;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
