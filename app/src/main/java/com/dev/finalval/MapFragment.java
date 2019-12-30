package com.dev.finalval;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment implements LocationListener
        ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{


    GoogleMap googleMap;
    LatLng latLngCurrent;
    Boolean inicio=true;

    GoogleApiClient client;
    LocationRequest request;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMyLocationEnabled(true);

                client = new GoogleApiClient.Builder(getContext())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(MapFragment.this)
                        .addOnConnectionFailedListener(MapFragment.this)
                        .build();

                client.connect();

            }
        });

        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null){

        }else {
            latLngCurrent = new LatLng(location.getLatitude(),location.getLongitude());
            if(inicio){
                //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngCurrent,15);
                //googleMap.animateCamera(update);

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(latLngCurrent)
                        .zoom(17)
                        .build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                inicio=false;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = new LocationRequest().create();
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(client,request,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
