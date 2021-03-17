package com.project.tmc.Models.LatLongData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatLongResult {
    @SerializedName("device")
    @Expose
    public Device device;
    @SerializedName("gps")
    @Expose
    public Gps gps;

    public Device getDevice() {
        return device;
    }

    public Gps getGps() {
        return gps;
    }
}
