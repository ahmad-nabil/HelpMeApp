package com.ahmad.helpmeapp.gecodingAdapter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class gecoding {
    static final String MAPBOX_API_KEY = "sk.eyJ1IjoiYWhtYWRuYWJpbHMiLCJhIjoiY2xyMmpyMDk2MGc3bzJpcWpjdTEzbjUzOCJ9.T2ngYFDeLvShmxaWjAA5RA";

    public static void getGeocoding(Context context, String Lat, String lng, final gecodingListener gecodingListener) {
        //Mapbox Geocoding API based on the \ latitude and longitude.
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + lng + "," + Lat + ".json?access_token=" + MAPBOX_API_KEY;
        //make an asynchronous HTTP GET request to the Mapbox Geocoding API
        RequestQueue queue = Volley.newRequestQueue(context);
        final StringBuilder ComBinePoiAndPlace = new StringBuilder();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //parsing json data from response
                    String FullAddress = response.getJSONArray("features").getJSONObject(0).getString("place_name");
                    gecodingListener.FullAddressName(FullAddress);
                    //extract Other address
                    JSONArray featuresArray = response.optJSONArray("features");
                    if (featuresArray != null && featuresArray.length() > 0) {
                        for (int x = 0; x < featuresArray.length(); x++) {
                            JSONObject feature = featuresArray.getJSONObject(x);
                            String poiName = feature.optString("text", "");
                            String poiCategory = feature.optJSONArray("place_type").optString(0, "");
                            if (poiCategory.equals("poi")) {
                                ComBinePoiAndPlace.append(" \t").append(poiName);

                            }
                            if (poiCategory.equals("region")) {
                                gecodingListener.cityName(poiName);

                            }

                            if (poiCategory.equals("place")) {
                                ComBinePoiAndPlace.append("\t").append(poiName);
                                gecodingListener.pointofinterest(ComBinePoiAndPlace.toString());

                            }


                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
}
