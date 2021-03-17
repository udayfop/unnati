package com.project.tmc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.MobileVerification.MobileVerificationResponse;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.tmc.Extra.Prefrences.getPreference_boolean;
import static com.project.tmc.Extra.Prefrences.getPreference_int;
import static com.project.tmc.Extra.Prefrences.setPreference;

public class OtpValidationActivity extends AppCompatActivity {

    private SmsVerifyCatcher smsVerifyCatcher;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    EditText name, phone_number, enter_otp_text;
    ImageView party_logo;
    TextView verified, login, not_registered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        setContentView(R.layout.activity_otp_validation);
        getSupportActionBar().hide();

        name = (EditText) findViewById(R.id.name);
        party_logo = (ImageView) findViewById(R.id.party_logo);
        phone_number = (EditText) findViewById(R.id.phone_number);
        login = (TextView) findViewById(R.id.login);
        not_registered = (TextView) findViewById(R.id.not_registered);

        if (getPreference_boolean(this, Constants.VERIFIED)) {

            party_logo.setImageResource(R.drawable.logo_unnati);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_number.getText().toString().length() < 10) {
                    phone_number.setError("Please enter Valid phone number");
                    not_registered.setVisibility(View.VISIBLE);
                } else {

                    mobileVerification();

//                    testPhoneAutoRetrieve(phone_number.getText().toString());
                }
            }
        });


//        intialisefirebase();
    }


    void intialisefirebase() {
        FirebaseApp.initializeApp(this);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:392366207583:android:a6cfa1a07b85a6c80ba1ea") // Required for Analytics.
                .setApiKey("YOUR_API_KEY")
                .setDatabaseUrl("YOUR_DATABASE_URL")
                .build();
        FirebaseApp.initializeApp(this, options, "secondary");
        firebaseAuth = FirebaseAuth.getInstance();

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);
                dialog.cancel();

                Log.e("ahgllalhfh", code);

                if (code != null) {
                    verified.setText("Verified");
                    verified.setTextColor(Color.GREEN);
                    verified.setVisibility(View.VISIBLE);
                    setPreference(OtpValidationActivity.this, Constants.VERIFIED, true);
                    if (name.getText().toString().isEmpty() || !CommonUtils.isNameValid(name.getText().toString())) {
                        name.setError("Enter Your Name");
                    } else {
                        setPreference(OtpValidationActivity.this, Constants.NAME, name.getText().toString());
                        setPreference(OtpValidationActivity.this, Constants.MOBILE, phone_number.getText().toString());
                        startActivity(new Intent(OtpValidationActivity.this, MainActivity.class));
                        finish();
                    }

                } else {
                    verified.setText("Not Verified");
                    verified.setTextColor(Color.RED);
                    verified.setVisibility(View.VISIBLE);
                    phone_number.setError("Mobile Number Not Verified");
                    setPreference(OtpValidationActivity.this, Constants.VERIFIED, true);
                }
            }
        });
    }

    public void testPhoneAutoRetrieve(String phone) {
        String phoneNumber = "+91" + phone;
        String smsCode = "123456";
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


    @Override
    protected void onStart() {
        super.onStart();
//        smsVerifyCatcher.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void mobileVerification() {
        if (CommonUtils.isNetworkAvailable(OtpValidationActivity.this)) {
            ApplicationActivity.getApiUtility().mobileVerification(OtpValidationActivity.this,
                    "MobileVerification/" + phone_number.getText().toString(),
                    true, new APIUtility.APIResponseListener<MobileVerificationResponse>() {
                        @Override
                        public void onReceiveResponse(MobileVerificationResponse response) {
                            if (response != null) {
                                setPreference(OtpValidationActivity.this, Constants.VERIFIED, true);

                                setPreference(OtpValidationActivity.this, Constants.NAME, name.getText().toString());
                                setPreference(OtpValidationActivity.this, Constants.MOBILE, phone_number.getText().toString());
                                startActivity(new Intent(OtpValidationActivity.this, MainActivity.class));
                                finish();

                            } else {
                                CommonUtils.alert(OtpValidationActivity.this, "Your mobile number not registered");
                                setPreference(OtpValidationActivity.this, Constants.VERIFIED, false);
                            }
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {
                            Toast.makeText(OtpValidationActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                            setPreference(OtpValidationActivity.this, Constants.VERIFIED, false);
                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {
                            setPreference(OtpValidationActivity.this, Constants.VERIFIED, false);
                        }
                    });
        } else {
            Toast.makeText(OtpValidationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
}