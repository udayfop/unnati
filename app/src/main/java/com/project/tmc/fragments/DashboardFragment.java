package com.project.tmc.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.fragments.tab_fragments.AverageFragment;
import com.project.tmc.fragments.tab_fragments.CampaignFragment;
import com.project.tmc.fragments.tab_fragments.TillDateFragment;
import com.project.tmc.fragments.tab_fragments.TodayFragment;
import com.project.tmc.utils.ApplicationActivity;
public class DashboardFragment extends Fragment {
    private FloatingActionButton floatIcon;
    private TabLayout tabBar;
    private APIUtility apiUtility;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_adddashboard, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiUtility = ApplicationActivity.getApiUtility();


        floatIcon = (FloatingActionButton) view.findViewById(R.id.floatIcon);
        tabBar = (TabLayout) view.findViewById(R.id.tabBar);


        tabBar.addTab(tabBar.newTab().setText("Campaign"));
        tabBar.addTab(tabBar.newTab().setText("Today"));
        tabBar.addTab(tabBar.newTab().setText("Till Date"));
        tabBar.addTab(tabBar.newTab().setText("Average"));
        tabBar.getTabAt(0).select();
        replaceFragment(new CampaignFragment());
        tabBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new CampaignFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new TodayFragment());
                } else if (tab.getPosition() == 3) {
                    replaceFragment(new TillDateFragment());
                } else if (tab.getPosition() == 4) {
                    replaceFragment(new AverageFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        floatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    public void replaceFragment(Fragment destFragment) {
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rl_fragment_container, destFragment);
        fragmentTransaction.commit();
    }

}