package com.project.tmc.Models.SabhaHeader;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SabhaLineRequest implements Serializable {

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public SabhaLineRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
