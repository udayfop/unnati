package com.project.tmc.Models.sabhaList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SabhaListResult implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("sabhaName")
    @Expose
    private String sabhaName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("creationTime")
    @Expose
    private String creationTime;
    @SerializedName("updationTime")
    @Expose
    private String updationTime;

    public Integer getId() {
        return id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getSabhaName() {
        return sabhaName;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUpdationTime() {
        return updationTime;
    }
}
