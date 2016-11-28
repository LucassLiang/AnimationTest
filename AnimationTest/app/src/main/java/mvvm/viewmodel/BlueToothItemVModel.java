package mvvm.viewmodel;

import android.bluetooth.BluetoothDevice;

/**
 * Created by lucas on 2016/11/28.
 */

public class BlueToothItemVModel {
    private BluetoothDevice device;

    public BlueToothItemVModel(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
