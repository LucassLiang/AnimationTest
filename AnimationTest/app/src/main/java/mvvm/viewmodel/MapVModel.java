package mvvm.viewmodel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMapBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mvvm.adapter.MarkInfoAdapter;

/**
 * Created by lucas on 2016/11/28.
 */

public class MapVModel implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    public static final int LOCATION_REQUEST = 2;
    public static final int MY_LOCATION_REQUEST = 3;

    private MarkInfoAdapter markAdapter;
    private ActivityMapBinding binding;
    private FragmentActivity context;
    private GoogleMap mMap;

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
    public void onMapClick(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        mMap.clear();
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
}
