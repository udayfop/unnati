package com.project.tmc.Models.SabhaHeader;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SabhaHeaderRequest implements Serializable {

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("SabhaId")
    @Expose
    private Integer sabhaId;
    @SerializedName("Comment")
    @Expose
    private String comment;
    @SerializedName("TotalImages")
    @Expose
    private Integer totalImages;
    @SerializedName("TotalReceivedImage")
    @Expose
    private Integer totalReceivedImage;

    public SabhaHeaderRequest(String mobileNo, Integer sabhaId, String comment, Integer totalImages, Integer totalReceivedImage) {
        this.mobileNo = mobileNo;
        this.sabhaId = sabhaId;
        this.comment = comment;
        this.totalImages = totalImages;
        this.totalReceivedImage = totalReceivedImage;
    }
}
