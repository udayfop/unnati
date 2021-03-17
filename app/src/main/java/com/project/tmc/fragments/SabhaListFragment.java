package com.project.tmc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tmc.Adapters.SabhaListAdapter;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.sabhaList.SabhaListResponse;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;

import static com.project.tmc.Extra.Prefrences.getPreference;

public class SabhaListFragment extends Fragment{

    public Button new_comments;
    private APIUtility apiUtility;
    private SabhaListAdapter sabhaListAdapter;
    private RecyclerView recyclerView;

    public SabhaListFragment()
    {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sabha_list, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        apiUtility = ApplicationActivity.getApiUtility();

        getSabhaList(getPreference(getContext(), Constants.MOBILE));
    }

    void getSabhaList(String mobile)
    {
        apiUtility.sabhaList(getContext(), "sabha/" + mobile, true, new APIUtility.APIResponseListener<SabhaListResponse>() {
            @Override
            public void onReceiveResponse(SabhaListResponse response) {
                sabhaListAdapter = new SabhaListAdapter(response.getResult());
                recyclerView.setAdapter(sabhaListAdapter);
            }

            @Override
            public void onReceiveErrorResponse(ErrorResponse errorResponse) {

            }

            @Override
            public void onReceiveFailureResponse(String response) {

            }
        });
    }


}