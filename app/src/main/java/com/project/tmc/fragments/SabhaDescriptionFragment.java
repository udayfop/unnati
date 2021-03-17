package com.project.tmc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.snackbar.Snackbar;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Interfaces.ReplaceFragment;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.sabha.SabhaRequest;
import com.project.tmc.Models.sabha.SabhaResponse;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import java.net.URLEncoder;
import java.util.logging.Logger;

import static com.project.tmc.Extra.Prefrences.getPreference;
import static com.project.tmc.Extra.Prefrences.getPreference_boolean;

public class SabhaDescriptionFragment extends Fragment {

    public EditText sabha_name, sabha_comments;
    public Button save_sabha;
    public ImageView party_logo;
    private APIUtility apiUtility;
    private ScrollView fadditems;
    private ReplaceFragment replaceFragment;
    public SabhaDescriptionFragment(){}

    public SabhaDescriptionFragment(ReplaceFragment replaceFragment) {
        this.replaceFragment = replaceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sabha_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiUtility = ApplicationActivity.getApiUtility();

        sabha_name = (EditText) view.findViewById(R.id.sabha_name);
        sabha_comments = (EditText) view.findViewById(R.id.sabha_comments);
        save_sabha = (Button) view.findViewById(R.id.save_sabha);
        fadditems = (ScrollView) view.findViewById(R.id.fadditems);

//        if (getPreference_boolean(getContext(),Constants.VERIFIED))
//        {
//
//            party_logo.setImageResource(R.drawable.logo_unnati);
//        }


        save_sabha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean click = true;
                if (sabha_name.getText().toString().isEmpty()) {
                    sabha_name.setError("Invalid Sabha Name");
                    click = false;
                }
                if (sabha_comments.getText().toString().isEmpty()) {
                    sabha_comments.setError("Enter Comment");
                    click = false;
                }
                if (click) {
                    sabha(new SabhaRequest(null,
                            getPreference(getContext(), Constants.MOBILE),
                            sabha_name.getText().toString(),
                            TextUtils.htmlEncode(sabha_comments.getText().toString())));

                }
            }
        });
    }


    //converter
    public String encodeStringInUTF16(String stringText) {

        String utfEncodedString = null;
        try {
            utfEncodedString = URLEncoder.encode(stringText, "utf-16");
            Log.e("lksjfkla", String.valueOf(utfEncodedString));

        } catch (Exception e) {
            Log.e("lksjfkla", String.valueOf(e.getMessage()));
        }
        return utfEncodedString;
    }

    void sabha(SabhaRequest sabhaRequest) {
        apiUtility.sabha(getContext(), sabhaRequest, true, new APIUtility.APIResponseListener<SabhaResponse>() {
            @Override
            public void onReceiveResponse(SabhaResponse response) {
                if (response != null) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    replaceFragment.replace(1);
                }
            }

            @Override
            public void onReceiveErrorResponse(ErrorResponse errorResponse) {
                if (errorResponse.getStatusCode() == 406) {
                    CommonUtils.alert(getContext(), CommonUtils.errorMessageArray(errorResponse.getErrorMessage().getMessageList()));
                } else {
                    Snackbar.make(fadditems, "An Error Occurred!", Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onReceiveFailureResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        });
    }
}