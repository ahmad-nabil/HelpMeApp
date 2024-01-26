package com.ahmad.helpmeapp.register;

import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.databinding.FragmentMaphelperBinding;
import com.ahmad.helpmeapp.gecodingAdapter.gecoding;
import com.ahmad.helpmeapp.gecodingAdapter.gecodingListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MapHelper extends DialogFragment implements PassLocationData {
    FragmentMaphelperBinding maphelperBinding;
    PassLocationData DataPass;
    MapView mapView;
    double longitude, latitude;
    String Address;
    MyLocationNewOverlay myLocationNewOverlay;


    //interface to send data to signup helper class
    public void setDataPass(PassLocationData dataPass) {
        this.DataPass = dataPass;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maphelperBinding = FragmentMaphelperBinding.inflate(getLayoutInflater());
        mapView = maphelperBinding.map;
        //initialize open street map
        InitializeMap();
        return maphelperBinding.getRoot();
    }

    private void InitializeMap() {
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(5d);
        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
        myLocationNewOverlay.enableMyLocation();
        Marker marker = new Marker(mapView);
        marker.setIcon(getActivity().getDrawable(R.drawable.baseline_location_on_red));
        myLocationNewOverlay.mMyLocationProvider.startLocationProvider(new IMyLocationConsumer() {
            @Override
            public void onLocationChanged(Location location, IMyLocationProvider source) {
                if (location != null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    // Set the initial map view to that location
                    GeoPoint point = new GeoPoint(latitude, longitude);
                    mapController.animateTo(point);
                    mapController.setCenter(point);
                    marker.setPosition(point);
                    mapView.getOverlays().add(marker);
                    GecoderLocation(latitude, longitude);
                }
                myLocationNewOverlay.disableMyLocation();
            }
        });


        //listener to take input if user want change his location
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                mapView.getOverlays().remove(marker);
                mapView.getOverlays().remove(myLocationNewOverlay);
                marker.setPosition(p);
                longitude = p.getLongitude();
                latitude = p.getLatitude();
                mapView.getOverlays().add(marker);
                GecoderLocation(latitude, longitude);
                return true;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        mapView.getOverlays().add(mapEventsOverlay);
    }

//check location Address using mapbox API
    private void GecoderLocation(double latitude, double longitude) {
        if (getActivity() != null) {
            gecoding.getGeocoding(requireContext(), String.valueOf(latitude), String.valueOf(longitude), new gecodingListener() {
                @Override
                public void FullAddressName(String FullAddress) {
                    maphelperBinding.selectLocationButton.setText(FullAddress);
                    Address = FullAddress;
                    //send data and dismiss  dialog
                    maphelperBinding.selectLocationButton.setOnClickListener(v -> {
                        sendData(latitude, longitude, FullAddress);
                    });
                }

                @Override
                public void cityName(String cityName) {

                }

                @Override
                public void pointofinterest(String Poi) {

                }


                @Override
                public void Error(String errorMessage) {

                }
            });
        }

    }

    private void sendData(double latitude, double longitude, String poi) {
        if (DataPass != null) {
            DataPass.onDataPass(longitude, latitude, poi);
            dismiss();
        }
    }


    @Override
    public void onDataPass(double longitude, double latitude, String data) {

    }

}

