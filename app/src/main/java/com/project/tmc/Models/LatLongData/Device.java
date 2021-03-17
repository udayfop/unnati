package com.project.tmc.Models.LatLongData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.tmc.Models.sabha.ResponseStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class Device implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("deviceId")
    @Expose
    public String deviceId;
    @SerializedName("vehicleNo")
    @Expose
    public String vehicleNo;
    @SerializedName("doorEnable")
    @Expose
    public Boolean doorEnable;
    @SerializedName("acEnable")
    @Expose
    public Boolean acEnable;
    @SerializedName("deviceType")
    @Expose
    public Integer deviceType;
    @SerializedName("deviceStatus")
    @Expose
    public Integer deviceStatus;
    @SerializedName("vehicleTypeId")
    @Expose
    public Integer vehicleTypeId;
    @SerializedName("overSpeedLimit")
    @Expose
    public Integer overSpeedLimit;
    @SerializedName("simPhoneNo")
    @Expose
    public String simPhoneNo;

    public Integer getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public Boolean getDoorEnable() {
        return doorEnable;
    }

    public Boolean getAcEnable() {
        return acEnable;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public Integer getVehicleTypeId() {
        return vehicleTypeId;
    }

    public Integer getOverSpeedLimit() {
        return overSpeedLimit;
    }

    public String getSimPhoneNo() {
        return simPhoneNo;
    }
}
