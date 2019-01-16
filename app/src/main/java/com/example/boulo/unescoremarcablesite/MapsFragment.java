package com.example.boulo.unescoremarcablesite;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boulo.unescoremarcablesite.database.PlaceSavedModel;
import com.example.boulo.unescoremarcablesite.model.RssFeedModel;
import com.example.boulo.unescoremarcablesite.utils.FetchRSS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mapView;
    private GoogleMap mMap ;
    FetchRSS fetch ;
    private FusedLocationProviderClient mFusedLocationClient;
    private MainActivity main ;
    // Declare a variable for the cluster manager.
    private ClusterManager<Markers> mClusterManager;
    private static ArrayList<RssFeedModel> geo ;

    public  MapsFragment() {
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity() ;
        fetch = FetchRSS.getInstance(main) ;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(main);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_maps, container,false) ;
        mapView = (MapView) viewGroup.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        //initialisation de la map
        try {
            MapsInitializer.initialize(main.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return viewGroup;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(main, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            }
                        }
                    });
        } catch (SecurityException e) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(49,2)));
        }


        geo = fetch.getRssList() ;
//crée les clusters de markers
        setUpClusterer(geo);
        mMap.setOnInfoWindowClickListener(this) ;
    }



    private void setUpClusterer(ArrayList<RssFeedModel> geo) {
        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Markers>(main, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        if (mMap!=null) {
            for (int i = 0; i < geo.size(); i++) {
                RssFeedModel items = geo.get(i) ;
                addItems(items.latitude, items.longitude, items.title) ;
            }
        }
        if (!main.isConnected()) {
            for (int i = 0 ; i <main.getPlaceSaved().size() ; i++) {
                    PlaceSavedModel items = main.getPlaceSaved().get(i) ;
                    addItems(items.getLat(), items.getLongitude(), items.getTitle()) ;
            }
        }
    }

    private void addItems(double lat, double lng, String title) {

            Markers newMarker = new Markers(lat, lng, title, null);
            mClusterManager.addItem(newMarker);
    }

    //callback du main quand le flux rss est obtenu
    public void addMarker(ArrayList<RssFeedModel> geo) {
        if (this.geo == null) {
            this.geo = geo ;
            if (mMap != null)
            setUpClusterer(geo);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        main.showSiteDetails(marker.getTitle());
    }
}
