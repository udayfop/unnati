package com.project.tmc.Clustering;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.project.tmc.Models.LatLongData.Gps;

import java.util.ArrayList;

public class MarkerClusterRenderer extends DefaultClusterRenderer<MyItem> {
    private ArrayList<Gps> dashboardArrayList = new ArrayList<>();

    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager, ArrayList<Gps> dashboards)
    {
        super(context, map, clusterManager);
        dashboardArrayList = dashboards;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
      }

    @Override
    protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }
}
