package com.project.tmc.Models.sabha;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SabhaRequest implements Serializable {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("SabhaName")
    @Expose
    private String sabhaName;
    @SerializedName("Description")
    @Expose
    private String description;

    public SabhaRequest(Integer id, String mobileNo, String sabhaName, String description) {
        this.id = id;
        this.mobileNo = mobileNo;
        this.sabhaName = sabhaName;
        this.description = description;
    }
}
