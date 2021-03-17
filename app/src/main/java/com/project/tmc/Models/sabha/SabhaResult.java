package com.project.tmc.Models.sabha;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SabhaResult implements Serializable {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
