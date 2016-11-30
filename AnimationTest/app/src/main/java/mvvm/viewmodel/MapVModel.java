package mvvm.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMapBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mvvm.adapter.MarkInfoAdapter;

/**
 * Created by lucas on 2016/11/28.
 */

public class MapVModel implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static final int LOCATION_REQUEST = 2;
    public static final int MY_LOCATION_REQUEST = 3;
    public static final int PLACE_PICK_REQUEST = 4;

    private MarkInfoAdapter markAdapter;
    private ActivityMapBinding binding;
    private FragmentActivity context;
    private GoogleMap mMap;
    private GoogleApiClient mClient;

    public MapVModel(FragmentActivity context, ActivityMapBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void onCreate() {
        if (checkPermission(LOCATION_REQUEST)) return;
        SupportMapFragment mapFragment = (SupportMapFragment) context.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        markAdapter = new MarkInfoAdapter();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setInfoWindowAdapter(markAdapter);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        if (checkPermission(MY_LOCATION_REQUEST)) return;
        googleMap.setMyLocationEnabled(true);
    }

    private boolean checkPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    requestCode);
            return true;
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(new LatLngBounds(latLng, latLng));
        try {
            context.startActivityForResult(builder.build(context), PLACE_PICK_REQUEST);
            context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICK_REQUEST:
                    mMap.clear();
                    Place place = PlacePicker.getPlace(context, data);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(place.getLatLng())
                            .title(place.getName().toString())
                            .snippet(place.getAddress().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    marker.showInfoWindow();
                    break;
            }
        }
    }

    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                SupportMapFragment mapFragment = (SupportMapFragment) context.getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_map);
                mapFragment.getMapAsync(this);
                break;
            case MY_LOCATION_REQUEST:
                if (checkPermission(MY_LOCATION_REQUEST)) return;
                mMap.setMyLocationEnabled(true);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "GoogleApiClient connection failed, please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
