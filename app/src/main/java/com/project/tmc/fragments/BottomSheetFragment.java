
package com.project.tmc.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.tmc.Adapters.BottomSheetAdapter;
import com.project.tmc.Adapters.BottomSheetImageAdapter;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.BottomSheet.BottomSheetResponse;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResponse;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import java.util.ArrayList;

import static com.project.tmc.Extra.Prefrences.getPreference;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private APIUtility apiUtility;
    private TextView sabha_name, sabha_comments, date_time, image_uploaded;
    private ImageView close_image;
    private RecyclerView recycler_images;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        sabha_name = view.findViewById(R.id.sabha_name);
        sabha_comments = view.findViewById(R.id.sabha_comments);
        date_time = view.findViewById(R.id.date_time);
        image_uploaded = view.findViewById(R.id.image_uploaded);
        recycler_images = (RecyclerView) view.findViewById(R.id.recycler_images);
        recycler_images.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_images.setHasFixedSize(true);
        apiUtility = ApplicationActivity.getApiUtility();
        bottomSheet();
    }



    private void bottomSheet() {
        if (CommonUtils.isNetworkAvailable(getContext())) {
            apiUtility.bottomSheet(getActivity(), "sabhadetail/Header/" + getPreference(getActivity(), Constants.MOBILE), true,
                    new APIUtility.APIResponseListener<BottomSheetResponse>() {
                        @Override
                        public void onReceiveResponse(BottomSheetResponse response) {
                            if (response != null) {
                                recycler_images.setAdapter(new BottomSheetAdapter(response.getResult(), getContext()));
                            } else {
                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {

                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {

                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
        }

    }



}
