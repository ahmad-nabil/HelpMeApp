package com.ahmad.helpmeapp.HelperSection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.databinding.FragmentLocationaskerBinding;
import com.ahmad.helpmeapp.gecodingAdapter.gecoding;
import com.ahmad.helpmeapp.gecodingAdapter.gecodingListener;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


public class LocationAskerFragment extends Fragment {
    FragmentLocationaskerBinding locationaskerBinding;
    //add longitude and latitude to arguments bundle
   public static LocationAskerFragment newInstance(double longitude,double latitude){
       LocationAskerFragment locationAskerFragment=new LocationAskerFragment();
       Bundle valuesLocation=new Bundle();
       valuesLocation.putDouble("lng",longitude);
       valuesLocation.putDouble("lat",latitude);
       locationAskerFragment.setArguments(valuesLocation);
       return locationAskerFragment;
   }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       locationaskerBinding=FragmentLocationaskerBinding.inflate(getLayoutInflater());
//check arguments
        if (getArguments()!=null){
           double longitude=getArguments().getDouble("lng");
           double latitude=getArguments().getDouble("lat");
showLocation(longitude,latitude);
ShowAddress(longitude,latitude);
        }
        return locationaskerBinding.getRoot();
    }

//initialize open street map
    private void showLocation(double longitude, double latitude) {
        MapView mapView=locationaskerBinding.locationOsm;
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        IGeoPoint geoPoint=new GeoPoint(latitude,longitude);
        IMapController mapController=mapView.getController();
        mapController.setCenter(geoPoint);
        mapController.setZoom(15d);
        ArrayList < OverlayItem>  overlayItems=new ArrayList<>();
        OverlayItem overlayItem=new OverlayItem("Location Asker","",geoPoint);
       overlayItem.setMarker(getResources().getDrawable(R.drawable.baseline_location_on_red));
        overlayItems.add(overlayItem);
        ItemizedIconOverlay <OverlayItem>iconOverlay=new ItemizedIconOverlay<>(overlayItems,null,requireContext());
        mapView.getOverlays().add(iconOverlay);
        mapView.invalidate();
    }
    //show address using mapbox API
    private void ShowAddress(double longitude, double latitude) {
        gecoding.getGeocoding(requireContext(), String.valueOf(latitude), String.valueOf(longitude), new gecodingListener() {
            @Override
            public void FullAddressName(String FullAddress) {
                locationaskerBinding.addressAsker.setText(FullAddress);}

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