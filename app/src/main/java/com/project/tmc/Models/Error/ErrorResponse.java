package com.project.tmc.Models.Error;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

    @SerializedName("StatusCode")
    private int StatusCode;
    @SerializedName("Message")
    private String Message;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String asynMessage;
    @SerializedName("Error")
    private ErrorMessage errorMessage;


    public ErrorResponse(int statusCode, String message, int code, String asynMessage, ErrorMessage errorMessage) {
        StatusCode = statusCode;
        Message = message;
        this.code = code;
        this.asynMessage = asynMessage;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getAsynMessage() {
        return asynMessage;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public String getMessage() {
        return Message;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

}
