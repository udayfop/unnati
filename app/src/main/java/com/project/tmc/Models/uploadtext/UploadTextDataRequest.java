package com.project.tmc.Models.uploadtext;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.SabhaHeader.SabhaHeaderRequest;
import com.project.tmc.Models.SabhaHeader.SabhaLineRequest;

import java.io.Serializable;
import java.util.ArrayList;

public class UploadTextDataRequest implements Serializable {
    @SerializedName("SabhaHeader")
    @Expose
    private SabhaHeaderRequest sabhaHeaderRequest;
    @SerializedName("SabhaLine")
    @Expose
    private ArrayList<SabhaLineRequest> sabhaLineRequests;

    public UploadTextDataRequest(SabhaHeaderRequest sabhaHeaderRequest, ArrayList<SabhaLineRequest> sabhaLineRequests) {
        this.sabhaHeaderRequest = sabhaHeaderRequest;
        this.sabhaLineRequests = sabhaLineRequests;
    }
}
