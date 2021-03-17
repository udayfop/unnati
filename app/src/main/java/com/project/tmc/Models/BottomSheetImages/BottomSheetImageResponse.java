package com.project.tmc.Models.BottomSheetImages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.BottomSheet.BottomSheetResult;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class BottomSheetImageResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private ArrayList<BottomSheetImageResult> result;

    public BottomSheetImageResponse(String version, Integer statusCode, String message, ArrayList<BottomSheetImageResult> result) {
        super(version, statusCode, message);
        this.result = result;
    }
    public ArrayList<BottomSheetImageResult> getResult() {
        return result;
    }
}
