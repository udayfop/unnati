package com.project.tmc.Clustering;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem
{
    public final String title;
    public final int profilePhoto;
    public final LatLng mPosition;

    public MyItem(LatLng position, String title, int pictureResource){
        this.title = title;
        profilePhoto = pictureResource;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }


}