package com.project.tmc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.Theme.ThemeResponse;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import static com.project.tmc.Extra.Prefrences.getPreference_boolean;
import static com.project.tmc.Extra.Prefrences.getPreference_int;
import static com.project.tmc.Extra.Prefrences.setPreference_int;

public class SplashActivity extends AppCompatActivity {
    private APIUtility apiUtility;
    ImageView party_logo;
    TextView party_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        party_logo =findViewById(R.id.party_logo) ;
        party_name =findViewById(R.id.party_name) ;
        setTheme();
        checkThem();
    }


    void checkThem()
    {
        if (CommonUtils.isNetworkAvailable(SplashActivity.this)) {
            ApplicationActivity.getApiUtility().theme(SplashActivity.this, "MobileVerification/theme",
                    true, new APIUtility.APIResponseListener<ThemeResponse>() {
                        @Override
                        public void onReceiveResponse(ThemeResponse response) {
                            if (response != null) {
                                setPreference_int(SplashActivity.this, Constants.THEME, response.getResult().getTheme());

                                runHandler();
                            }
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {

                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {

                        }
                    });
        }
        else {
            Toast.makeText(SplashActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }


    void setTheme()
    {

            party_logo.setImageResource(R.drawable.logo_unnati);
            party_name.setText(getString(R.string.app_name));

    }

    void runHandler()
    {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (getPreference_boolean(SplashActivity.this, Constants.VERIFIED)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, OtpValidationActivity.class));
                }
                Log.e("hgahgoiao", String.valueOf(getPreference_boolean(SplashActivity.this, Constants.VERIFIED)));

                finish();
            }
        }, 3000);
    }
}