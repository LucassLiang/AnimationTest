package mvvm.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.lucas.animationtest.databinding.ActivityBlueToothBinding;
import com.tbruyelle.rxpermissions.RxPermissions;

import mvvm.adapter.BlueToothAdapter;
import rx.functions.Action1;

/**
 * Created by lucas on 2016/11/28.
 */

public class BlueToothVModel {
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_LOCATION = 1;
    private ActivityBlueToothBinding binding;
    private RxPermissions rxPermissions;
    private BlueToothAdapter deviceAdapter;
    private BluetoothAdapter mAdapter;
    private BroadcastReceiver mReceiver;
    private Context context;

    public BlueToothVModel(ActivityBlueToothBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
    }

    public void onCreate() {
        rxPermissions = new RxPermissions((Activity) context);
        initBlueToothAdapter();
        initRecyclerView();
        initReceiver();
    }

    private void initBlueToothAdapter() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            binding.switchOpen.setEnabled(false);
            Toast.makeText(context, "Your device don't support Blue Tooth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mAdapter.isEnabled()) {
            binding.switchOpen.setChecked(true);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvDevice.setLayoutManager(layoutManager);

        deviceAdapter = new BlueToothAdapter(context);
        binding.rvDevice.setAdapter(deviceAdapter);
    }

    private void initReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    BlueToothItemVModel itemVModel = new BlueToothItemVModel(device);
                    deviceAdapter.add(itemVModel);
                    deviceAdapter.notifyItemInserted(deviceAdapter.size() - 1);
                }
            }
        };
        checkLocationPermission();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, intentFilter);
    }

    private void checkLocationPermission() {
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            registerReceiver();
                        } else {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_LOCATION);
                        }
                    }
                });
    }

    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerReceiver();
                } else {
                    binding.switchOpen.setEnabled(false);
                }
                break;
        }
    }

    public void controlBlueTooth(View view) {
        if (!mAdapter.isEnabled()) {
            Intent requestOpen = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(requestOpen, REQUEST_CODE);
        } else {
            mAdapter.disable();
            mAdapter.cancelDiscovery();
            binding.switchOpen.setChecked(false);
        }
        deviceAdapter.clear();
        deviceAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    mAdapter.enable();
                    //discover device by blue tooth
                    mAdapter.startDiscovery();
                    break;
            }
        } else {
            binding.switchOpen.setChecked(false);
        }
    }

    public void onDestroy() {
        if (binding.switchOpen.isEnabled()) {
            context.unregisterReceiver(mReceiver);
        }
    }

    public BluetoothAdapter getmAdapter() {
        return mAdapter;
    }
}
