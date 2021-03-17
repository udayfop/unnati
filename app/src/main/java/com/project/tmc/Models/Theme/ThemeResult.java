package com.project.tmc.Models.Theme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ThemeResult implements Serializable {
    @SerializedName("theme")
    @Expose
    private Integer theme;

    public Integer getTheme() {
        return theme;
    }
}
