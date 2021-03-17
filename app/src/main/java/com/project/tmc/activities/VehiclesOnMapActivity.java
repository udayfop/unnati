package com.project.tmc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
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
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import static com.project.tmc.Extra.Prefrences.getPreference;

public class VehiclesOnMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MyItem>,ClusterManager.OnClusterInfoWindowClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>,ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>{
    private GoogleMap mMap;
    private LatLngBounds.Builder builder;
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<LatLongResult> latLongResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_on_map);
        latLongResults = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
@Override
public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.getUiSettings().setZoomControlsEnabled(true);
    builder = new LatLngBounds.Builder();
    getDeviceGps();

    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }
        @Override
        public View getInfoContents(Marker arg0) {
            return null;
        }
    });
}

    void getDeviceGps()
    {
        if (CommonUtils.isNetworkAvailable(VehiclesOnMapActivity.this))
        {
            ApplicationActivity.getApiUtility().devicGps(VehiclesOnMapActivity.this,"devices/getdevices/"+getPreference(VehiclesOnMapActivity.this, Constants.MOBILE), true, new APIUtility.APIResponseListener<LatLongResponse>() {
                        @Override
                        public void onReceiveResponse(LatLongResponse response) {
                            latLongResults = response.getResult();

                            setUpClusterer();
                            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    try{
                                        LatLngBounds bounds = builder.build();
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {
                            Toast.makeText(VehiclesOnMapActivity.this,errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {
                            Toast.makeText(VehiclesOnMapActivity.this,response, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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

        for (int i=0; latLongResults.size()>i;i++){
            // Log.d("Dashboard",String.valueOf(myItem.getTitle()+" "+dashboards.get(i).getDevice().getId()));
            if (String.valueOf(latLongResults.get(i).getDevice().getId()).equals(myItem.getTitle()))
            {// Log.d("Dashboard1",String.valueOf(myItem.getTitle()+" "+dashboards.get(i).getDevice().getId()));
                //Toast.makeText(AllVehicleMapActivity.this,String.valueOf(myItem.getTitle()+" "+dashboards.get(i).getDevice().getId()),Toast.LENGTH_SHORT).show();
                // dashboards.get(0);
//                setDataLiveTrack(latLongResults.get(i));
            }

        }

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
    }

private class PersonRenderer extends DefaultClusterRenderer<MyItem> {
    private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
    private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private final int mDimension;

    private PersonRenderer() {
        super(getApplicationContext(), mMap, mClusterManager);
        mIconGenerator.setBackground(null);
        View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
        mImageView = new ImageView(getApplicationContext());
        mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
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
        if (cluster.getSize() >=1 && cluster.getSize()<3)
        {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(VehiclesOnMapActivity.this,
                    R.drawable.blue_cluster, String.valueOf(cluster.getSize()))));
        }
        else if (cluster.getSize() >=3 && cluster.getSize()<8)
        {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(VehiclesOnMapActivity.this, R.drawable.yellow_cluster,
                    String.valueOf(cluster.getSize()))));
        }
        else
        {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(VehiclesOnMapActivity.this,
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
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
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

    private void setUpClusterer()
    {
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mClusterManager.setRenderer(new PersonRenderer());
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

    private void addItems()
    {
        builder = new LatLngBounds.Builder();
        for (int i = 0; i < latLongResults.size(); i++)
        {
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
                try{
                    LatLngBounds bounds = builder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}