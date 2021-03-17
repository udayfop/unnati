package com.project.tmc.Models.BottomSheet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class BottomSheetResponse extends ResponseStatus implements Serializable {
    @SerializedName("Result")
    @Expose
    private ArrayList<BottomSheetResult> result;

    public BottomSheetResponse(String version, Integer statusCode, String message,ArrayList<BottomSheetResult> result) {
        super(version, statusCode, message);
        this.result = result;
    }
    public ArrayList<BottomSheetResult> getResult() {
        return result;
    }
}
