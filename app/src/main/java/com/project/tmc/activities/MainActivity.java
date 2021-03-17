package com.project.tmc.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Interfaces.ReplaceFragment;
import com.project.tmc.R;
import com.project.tmc.fragments.ContactusFragment;
import com.project.tmc.fragments.DashboardFragment;
import com.project.tmc.fragments.RathReportFragment;
import com.project.tmc.fragments.ReasonReportFragment;
import com.project.tmc.fragments.SabhaDescriptionFragment;
import com.project.tmc.fragments.SabhaListFragment;
import com.project.tmc.fragments.SummaryFragment;

import static com.project.tmc.Extra.Prefrences.getPreference_boolean;
import static com.project.tmc.Extra.Prefrences.getPreference_int;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReplaceFragment {
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private TextView tool_title,party_name;
    private ImageView party_logo;
    private Button add_sabha_btn;
    private ReplaceFragment replaceFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOverflowIcon(resize(getDrawable(R.drawable.logo_bjp)));
        tool_title = (TextView) findViewById(R.id.tool_title);

        add_sabha_btn = (Button) findViewById(R.id.add_sabha_btn);
        replaceFragment = (ReplaceFragment)this;
        add_sabha_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SabhaDescriptionFragment(replaceFragment));
            }
        });



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        party_logo = view. findViewById(R.id.party_logo);
        party_name = view. findViewById(R.id.party_name);


        if (getPreference_boolean(this,Constants.VERIFIED))
        {

            party_logo.setImageResource(R.drawable.logo_unnati);
            party_name.setText(getString(R.string.app_name));
        }

        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setBackgroundColor(getColor(R.color.browser_actions_bg_grey));

        replaceFragment(new DashboardFragment());
        tool_title.setText("Dashboard");
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 30, 30, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    public void replaceFragment(Fragment destFragment) {
        invalidateOptionsMenu();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, destFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {

        if (!tool_title.getText().toString().equals("Dashboard")) {

            replaceFragment(new DashboardFragment());
            tool_title.setText("Dashboard");
            drawer.close();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to close this application ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle(getString(R.string.logout));

            alert.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_sabha:
                replaceFragment(new SabhaListFragment());
                tool_title.setText(getString(R.string.sabha_report));
                break;
            case R.id.nav_rathreport:
                replaceFragment(new RathReportFragment());
                tool_title.setText(getString(R.string.rath_report));
                break;
            case R.id.nav_reasonreport:
                replaceFragment(new ReasonReportFragment());
                tool_title.setText(getString(R.string.reason_report));
                break;

            case R.id.nav_summary:
                replaceFragment(new SummaryFragment());
                tool_title.setText(getString(R.string.summary));
                break;
            case R.id.nav_contact:
                replaceFragment(new ContactusFragment());
                tool_title.setText(getString(R.string.contact_us));
                break;

            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(new Intent(MainActivity.this, OtpValidationActivity.class));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle(getString(R.string.logout));
                alert.show();
                break;
            default:
                replaceFragment(new DashboardFragment());
                tool_title.setText("Dashboard");

        }


        if (tool_title.getText().toString().equals("Sabha Report"))
        {
            add_sabha_btn.setVisibility(View.VISIBLE);
        }
        else {
            add_sabha_btn.setVisibility(View.INVISIBLE);
        }
        drawer.close();
        return true;
    }

    @Override
    public void replace(int position) {
switch (position)
{
    case 1:
        replaceFragment(new SabhaListFragment());
        break;
}
    }
}