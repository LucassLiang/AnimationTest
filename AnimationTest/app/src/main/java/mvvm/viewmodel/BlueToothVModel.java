package mvvm.viewmodel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.lucas.animationtest.databinding.ActivityBlueToothBinding;

import mvvm.adapter.BlueToothAdapter;

/**
 * Created by lucas on 2016/11/28.
 */

public class BlueToothVModel {
    public static final int REQUEST_CODE = 1;
    private ActivityBlueToothBinding binding;
    private BlueToothAdapter deviceAdapter;
    private BluetoothAdapter mAdapter;
    private Context context;

    public BlueToothVModel(ActivityBlueToothBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
    }

    public void onCreate() {
        initBlueToothAdapter();
        initRecyclerView();
        initReceiver();
    }

    private void initBlueToothAdapter() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            binding.switchOpen.setEnabled(false);
            Toast.makeText(context, "Your device don't support Blue Tooth", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvDevice.setLayoutManager(layoutManager);

        deviceAdapter = new BlueToothAdapter(context);
        binding.rvDevice.setAdapter(deviceAdapter);
    }

    private void initReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
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
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver, intentFilter);
    }

    public void controlBlueTooth(View view) {
        if (!mAdapter.isEnabled()) {
            Intent requestOpen = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(requestOpen, REQUEST_CODE);
        } else {
            mAdapter.disable();
            mAdapter.cancelDiscovery();
            binding.switchOpen.setChecked(false);
            deviceAdapter.clear();
            deviceAdapter.notifyItemRangeRemoved(0, deviceAdapter.size());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    mAdapter.enable();
                    binding.switchOpen.setChecked(true);
                    //discover device by blue tooth
                    mAdapter.startDiscovery();
                    break;
            }
        }
    }
}
