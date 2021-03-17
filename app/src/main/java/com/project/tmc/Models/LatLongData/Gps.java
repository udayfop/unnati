package com.project.tmc.Models.LatLongData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gps {
    @SerializedName("timeStamp")
    @Expose
    public String timeStamp;
    @SerializedName("latitude")
    @Expose
    public Double latitude;
    @SerializedName("longitude")
    @Expose
    public Double longitude;
    @SerializedName("heading")
    @Expose
    public Double heading;
    @SerializedName("speed")
    @Expose
    public Double speed;
    @SerializedName("satCount")
    @Expose
    public Integer satCount;
    @SerializedName("gpsStatus")
    @Expose
    public Integer gpsStatus;
    @SerializedName("vehicleStatus")
    @Expose
    public Integer vehicleStatus;
    @SerializedName("externalPower")
    @Expose
    public Integer externalPower;
    @SerializedName("mainVoltage")
    @Expose
    public Double mainVoltage;
    @SerializedName("batteryVoltage")
    @Expose
    public Double batteryVoltage;
    @SerializedName("panicStatus")
    @Expose
    public Integer panicStatus;
    @SerializedName("odoMeter")
    @Expose
    public Double odoMeter;
    @SerializedName("currentDayDistance")
    @Expose
    public Double currentDayDistance;
    @SerializedName("gsmStrength")
    @Expose
    public Integer gsmStrength;

    public String getTimeStamp() {
        return timeStamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getHeading() {
        return heading;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getSatCount() {
        return satCount;
    }

    public Integer getGpsStatus() {
        return gpsStatus;
    }

    public Integer getVehicleStatus() {
        return vehicleStatus;
    }

    public Integer getExternalPower() {
        return externalPower;
    }

    public Double getMainVoltage() {
        return mainVoltage;
    }

    public Double getBatteryVoltage() {
        return batteryVoltage;
    }

    public Integer getPanicStatus() {
        return panicStatus;
    }

    public Double getOdoMeter() {
        return odoMeter;
    }

    public Double getCurrentDayDistance() {
        return currentDayDistance;
    }

    public Integer getGsmStrength() {
        return gsmStrength;
    }
}
