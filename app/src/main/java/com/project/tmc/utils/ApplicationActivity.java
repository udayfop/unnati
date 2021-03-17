package com.project.tmc.utils;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.project.tmc.Retrofit.APIUtility;

public class ApplicationActivity extends Application {

    private static APIUtility apiUtility;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        apiUtility = new APIUtility(getApplicationContext());
        ApplicationActivity.context = getApplicationContext();
       // BlurKit.init(this);

        FirebaseApp.initializeApp(this);

      /*  CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/segoeuiregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

    }

    public static Context getContext(){
        return ApplicationActivity.context;
    }

    public static APIUtility getApiUtility() {
        return apiUtility;
    }
}
