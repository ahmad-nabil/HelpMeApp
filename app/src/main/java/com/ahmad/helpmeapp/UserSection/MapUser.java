package com.ahmad.helpmeapp.UserSection;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.customData.CustomLocationHelper;
import com.ahmad.helpmeapp.customData.CustomSpinnerList;
import com.ahmad.helpmeapp.databinding.ActivityMainMenuUserBinding;
import com.ahmad.helpmeapp.databinding.DialogeSearchBinding;
import com.ahmad.helpmeapp.gecodingAdapter.gecoding;
import com.ahmad.helpmeapp.gecodingAdapter.gecodingListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import java.util.ArrayList;


public class MapUser {
    ActivityMainMenuUserBinding binding;
    Activity activityUser;
    MapView mapView;
    LocationComponentPlugin locationComponentPlugin;
    ArrayList<CustomSpinnerList> spinnerLists = new ArrayList<>();
    CustomSpinnerAdapterLocation spinnerAdapterLocation;
    ArrayList<CustomLocationHelper> LocationHelpers = new ArrayList<>();
    FirebaseFirestore FireStoreDb;
    String RegionUser = ";";
    Dialog DialogCustom;
    DialogeSearchBinding searchBinding;

    public MapUser(ActivityMainMenuUserBinding binding, Activity activityUser) {
        this.binding = binding;
        this.activityUser = activityUser;
        DialogCustom = new Dialog(activityUser);
        searchBinding = DialogeSearchBinding.inflate(activityUser.getLayoutInflater());
    }

    public void initializeUI() {
        FireStoreDb = FirebaseFirestore.getInstance();
        mapView = binding.mapviewUser;
        getLocationHelperFromDB();
        mapView.getMapboxMap().loadStyle("mapbox://styles/ahmadnabils/clqeku91700g401pj8rv4fz43", new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().build());
                locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D(ImageHolder.from(R.drawable.baseline_location_on_red));
                locationComponentPlugin.setLocationPuck(locationPuck2D);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);


            }
        });

        spinnerLists.add(new CustomSpinnerList("Add your location", R.drawable.baseline_location_on_24));
        spinnerAdapterLocation = new CustomSpinnerAdapterLocation(activityUser, spinnerLists);
        binding.spinner.setAdapter(spinnerAdapterLocation);
        binding.spinner.setDropDownVerticalOffset(20);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    showCard();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getLocation() {

        String lat = String.valueOf(mapView.getMapboxMap().getCameraState().getCenter().latitude());
        String lng = String.valueOf(mapView.getMapboxMap().getCameraState().getCenter().longitude());
        locationComponentPlugin.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);

        gecoding.getGeocoding(activityUser, lat, lng, new gecodingListener() {
            @Override
            public void FullAddressName(String FullAddress) {
                spinnerLists.add(new CustomSpinnerList(FullAddress, R.drawable.baseline_location_on_red));
                spinnerAdapterLocation.notifyDataSetChanged();
            }


            @Override
            public void cityName(String cityName) {
                spinnerLists.add(new CustomSpinnerList(cityName, R.drawable.baseline_location_on_red));
                RegionUser = cityName;
                Toast.makeText(activityUser, cityName, Toast.LENGTH_SHORT).show();
                spinnerAdapterLocation.notifyDataSetChanged();
            }

            @Override
            public void pointofinterest(String Poi) {
                spinnerLists.add(new CustomSpinnerList(Poi, R.drawable.baseline_location_on_red));
                spinnerAdapterLocation.notifyDataSetChanged();
            }

            @Override
            public void Error(String errorMessage) {
            }
        });


    }
//show dialog search for nearest helper
    private void showCard() {
        DialogCustom.setContentView(searchBinding.getRoot());
        DialogCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogCustom.getWindow().setWindowAnimations(R.style.DialogAnimation);
        DialogCustom.show();
        double LongitudeUser = mapView.getMapboxMap().getCameraState().getCenter().longitude();
        double LatitudeUser = mapView.getMapboxMap().getCameraState().getCenter().latitude();
        CheckNearestHelper(LatitudeUser, LongitudeUser);

    }

//get all helper from Database
    public void getLocationHelperFromDB() {

        FireStoreDb.collection("helpers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot DataSnapshot : queryDocumentSnapshots) {
                    String NameHelper = DataSnapshot.getString("fullName");
                    String LevelExp = DataSnapshot.getString("levelExp");
                    String TypeExp = DataSnapshot.getString("typeExp");
                    String PhoneHelper = DataSnapshot.getString("phone");
                    String IdHelper = DataSnapshot.getId();
                    Double Longitude_Helper = DataSnapshot.getDouble("longitude");
                    Double Latitude_Helper = DataSnapshot.getDouble("latitude");
                    LocationHelpers.add(new CustomLocationHelper(NameHelper, PhoneHelper, LevelExp, TypeExp, Longitude_Helper, Latitude_Helper, IdHelper));

                }
            }
        });
    }
//get location user
    final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
            getLocation();
        }
    };
    final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };

    public void CheckNearestHelper(double latitude, double Longitude) {

        if (LocationHelpers.size() > 0) {
            float[] result = new float[1];
            CustomLocationHelper NearestHelper = null;
            float distance = 0;
            float MinDistance = Float.MAX_VALUE;
            for (CustomLocationHelper customLocationHelper : LocationHelpers) {
                Location.distanceBetween(customLocationHelper.getLatitude(), customLocationHelper.getLongitude(), latitude, Longitude, result);
                distance = result[0];
                if (distance < MinDistance) {
                    MinDistance = distance;
                    NearestHelper = customLocationHelper;

                }
            }
            if (NearestHelper != null) {
                UserBackend userBackend = new UserBackend(DialogCustom, activityUser, mapView, NearestHelper, (int) distance, RegionUser, Longitude, latitude);
                userBackend.checkAddress();

            }


        }
    }

}
