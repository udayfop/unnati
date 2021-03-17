package com.project.tmc.Models.BottomSheet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BottomSheetResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("sabhaId")
    @Expose
    private Integer sabhaId;
    @SerializedName("sabhaName")
    @Expose
    private String sabhaName;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("totalImages")
    @Expose
    private Integer totalImages;
    @SerializedName("totalReceivedImage")
    @Expose
    private Integer totalReceivedImage;
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

    public Integer getSabhaId() {
        return sabhaId;
    }

    public String getSabhaName() {
        return sabhaName;
    }

    public String getComment() {
        return comment;
    }

    public Integer getTotalImages() {
        return totalImages;
    }

    public Integer getTotalReceivedImage() {
        return totalReceivedImage;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUpdationTime() {
        return updationTime;
    }
}
