package com.project.tmc.fragments.tab_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.project.tmc.Clustering.MultiDrawable;
import com.project.tmc.Clustering.MyItem;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.LatLongData.LatLongResponse;
import com.project.tmc.Models.LatLongData.LatLongResult;
import com.project.tmc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.activities.VehiclesOnMapActivity;
import com.project.tmc.chart_models.DayAxisValueFormatter;
import com.project.tmc.chart_models.Fill;
import com.project.tmc.chart_models.MyAxisValueFormatter;
import com.project.tmc.chart_models.XYMarkerView;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import static com.project.tmc.Extra.Prefrences.getPreference;
import static com.project.tmc.Extra.Prefrences.getPreference_boolean;
import static com.project.tmc.Extra.Prefrences.getPreference_int;

public class CampaignFragment extends Fragment implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    private BarChart barChart;
    private GoogleMap mMap;
    private PieChart pieChart;
    private TextView date_time,operational,non_operational;
    private ImageView party_logo;
    private ArrayList<LatLongResult> latLongResults;
    private ClusterManager<MyItem> mClusterManager;
    private LatLngBounds.Builder builder;
    ImageView img_fullscreen;

    public CampaignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_campaign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date_time = view.findViewById(R.id.date_time);
        party_logo = view.findViewById(R.id.party_logo);
        operational = view.findViewById(R.id.operational);
        non_operational = view.findViewById(R.id.non_operational);
        latLongResults = new ArrayList<>();
        img_fullscreen = view.findViewById(R.id.img_fullscreen);

        if (getPreference_boolean(getContext(), Constants.VERIFIED)) {

            party_logo.setImageResource(R.drawable.logo_unnati);
        }

//        if (CommonUtils.differenceSecon())


        date_time.setText(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date()));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initBarChart(view);
        initPieChart(view);

        img_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VehiclesOnMapActivity.class));
            }
        });

    }


    private void setData(int count, float range) {

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(1, 16, "#FF9800"));
        values.add(new BarEntry(2, 10, "#FF9800"));
        values.add(new BarEntry(3, 12, "#FF9800"));
        values.add(new BarEntry(4, 10, "#FF9800"));
        values.add(new BarEntry(5, 35, "#FF9800"));
        values.add(new BarEntry(6, 21, "#FF9800"));
        values.add(new BarEntry(7, 30, "#FF9800"));


        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setColor(Color.parseColor("#FF9800"));
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2021");

            set1.setDrawIcons(false);
            set1.setColor(Color.parseColor("#FF9800"));
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.3f);

            barChart.setData(data);

        }
    }


    void initBarChart(View view) {
        barChart = (BarChart) view.findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(10);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(true);
        DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(barChart);
        barChart.setMarker(mv);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
//        barChart.getXAxis().setEnabled(false);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        setData(5, 50);
    }

    void initPieChart(View view) {
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.RED);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
//        pieChart.setOnChartValueSelectedListener(getActivity());
        pieChart.animateY(1400, Easing.EaseInOutQuad);

        pieChart.getLegend().setEnabled(false);

        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setHoleRadius(0.0f);
        setPieChartData(10, 5);
    }

    private void setPieChartData(int running, int stopped) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if (running > 0) {
            entries.add(new PieEntry(running, ""));
            colors.add(Color.parseColor("#FF03DAC5"));
        }
        if (stopped > 0) {
            entries.add(new PieEntry(stopped, ""));
            colors.add(Color.parseColor("#FF9800"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(0f);

        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        getDeviceGps();

    }

    void getDeviceGps() {
        if (CommonUtils.isNetworkAvailable(getContext())) {
            ApplicationActivity.getApiUtility().devicGps(getContext(), "devices/getdevices/" + getPreference(getContext(), Constants.MOBILE), true, new APIUtility.APIResponseListener<LatLongResponse>() {
                @Override
                public void onReceiveResponse(LatLongResponse response) {
                    Toast.makeText(getContext(), "Succes", Toast.LENGTH_SHORT).show();
                    latLongResults = response.getResult();

                    setUpClusterer();
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            try {
                                LatLngBounds bounds = builder.build();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onReceiveErrorResponse(ErrorResponse errorResponse) {
                    Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReceiveFailureResponse(String response) {
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setUpClusterer() {
        mClusterManager = new ClusterManager<MyItem>(getContext(), mMap);
        mClusterManager.setRenderer(new PersonRenderer(getContext()));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        addItems();
        mClusterManager.cluster();
    }

    private void addItems() {
        builder = new LatLngBounds.Builder();
        for (int i = 0; i < latLongResults.size(); i++) {
            if (latLongResults.get(i).getGps() != null) {
                if (latLongResults.get(i).getGps().getVehicleStatus() == 21)   // stopped
                {
                    mClusterManager.addItem(new MyItem(new LatLng(latLongResults.get(i).getGps().getLatitude(),
                            latLongResults.get(i).getGps().getLongitude()), String.valueOf(latLongResults.get(i).getDevice().getId()), R.drawable.map_red));
                } else if (latLongResults.get(i).getGps().getVehicleStatus() == 22)    // dormant
                {
                    mClusterManager.addItem(new MyItem(new LatLng(latLongResults.get(i).getGps().getLatitude(),
                            latLongResults.get(i).getGps().getLongitude()), String.valueOf(latLongResults.get(i).getDevice().getId()), R.drawable.map_yellow));
                } else if (latLongResults.get(i).getGps().getVehicleStatus() == 23)      // running
                {
                    mClusterManager.addItem(new MyItem(new LatLng(latLongResults.get(i).getGps().getLatitude(),
                            latLongResults.get(i).getGps().getLongitude()), String.valueOf(latLongResults.get(i).getDevice().getId()), R.drawable.map_green));
                } else {                   // inactive
                    mClusterManager.addItem(new MyItem(new LatLng(latLongResults.get(i).getGps().getLatitude(),
                            latLongResults.get(i).getGps().getLongitude()), String.valueOf(latLongResults.get(i).getDevice().getId()), R.drawable.map_blue));
                }

                builder.include(new LatLng(latLongResults.get(i).getGps().getLatitude(),
                        latLongResults.get(i).getGps().getLongitude()));
            }


        }
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                try {
                    LatLngBounds bounds = builder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        for (int i = 0; latLongResults.size() > i; i++) {
            // Log.d("Dashboard",String.valueOf(myItem.getTitle()+" "+dashboards.get(i).getDevice().getId()));
            if (String.valueOf(latLongResults.get(i).getDevice().getId()).equals(myItem.getTitle())) {// Log.d("Dashboard1",String.valueOf(myItem.getTitle()+" "+dashboards.get(i).getDevice().getId()));
                //Toast.makeText(AllVehicleMapActivity.this,String.valueOf(myItem.getTitle()+" "+dashboards.get(i).getDevice().getId()),Toast.LENGTH_SHORT).show();
//                // dashboards.get(0);
//                setDataLiveTrack(dashboards.get(i));
            }

        }

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {

    }

    private class PersonRenderer extends DefaultClusterRenderer<MyItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        private PersonRenderer(Context context) {
            super(context, mMap, mClusterManager);
            mIconGenerator.setBackground(null);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
            mImageView = new ImageView(context);
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            ;
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            ;
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem person, MarkerOptions markerOptions) {
            mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();

            //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.title);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title("");

        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (MyItem p : cluster.getItems()) {
                if (profilePhotos.size() == 4)
                    break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);

            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);
            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            if (cluster.getSize() >= 1 && cluster.getSize() < 3) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(getActivity(),
                        R.drawable.blue_cluster, String.valueOf(cluster.getSize()))));
            } else if (cluster.getSize() >= 3 && cluster.getSize() < 8) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(getActivity(), R.drawable.yellow_cluster,
                        String.valueOf(cluster.getSize()))));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(getContext(),
                        R.drawable.red_cluster, String.valueOf(cluster.getSize()))));
            }

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() > 1;
        }
    }

    public Bitmap createCustomMarker(Context context, int resource, String _name) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_cluster_layout_main, null);
        ImageView markerImage = (ImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
}