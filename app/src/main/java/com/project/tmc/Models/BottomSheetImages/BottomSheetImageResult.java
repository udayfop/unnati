package com.project.tmc.Models.BottomSheetImages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BottomSheetImageResult {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("sabhaHeaderId")
    @Expose
    public Integer sabhaHeaderId;
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("creationTime")
    @Expose
    public String creationTime;
    @SerializedName("updationTime")
    @Expose
    public String updationTime;

    public Integer getId() {
        return id;
    }

    public Integer getSabhaHeaderId() {
        return sabhaHeaderId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUpdationTime() {
        return updationTime;
    }
}
