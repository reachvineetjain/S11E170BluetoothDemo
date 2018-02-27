package com.nehvin.s11e170bluetoothdemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter BA;
    int REQUEST_ENABLE_BT = 15;
    public static String TAG = MainActivity.class.getSimpleName();
    ListView pairedDeviceList;
    Button btOnOff;
    boolean btFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pairedDeviceList = (ListView)findViewById(R.id.pairedDeviceListView);
        btOnOff = (Button) findViewById(R.id.buttonOff);

        BA = BluetoothAdapter.getDefaultAdapter();

        if(BA.isEnabled()){
            btFlag = true;
            Toast.makeText(getApplicationContext(), "Bluetooth is on", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, REQUEST_ENABLE_BT);

//            BA.enable();

//            if(BA.isEnabled()){
//                Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "Please turn on bluetooth" , Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: "+ requestCode);
        Log.i(TAG, "onActivityResult: "+ resultCode);

        if(REQUEST_ENABLE_BT == requestCode && Activity.RESULT_OK == resultCode)
        {
            btFlag = true;
            Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please turn on bluetooth manually" , Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOffBluetooth(View view) {

        if(BA != null && BA.isEnabled() && btFlag)
        {
            BA.disable();
            btFlag = false;
            btOnOff.setText("Turn Bluetooth On");
            Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_LONG).show();
        }
        else
        { // BA.isEnabled()
            if(BA != null && !btFlag){
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, REQUEST_ENABLE_BT);
                btFlag = true;
                btOnOff.setText("Turn Bluetooth Off");
            }
        }
    }

    public void viewPairedDevices(View view) {

        Set<BluetoothDevice> btDeviceList = BA.getBondedDevices();
        ArrayList<String> listOfDevices = new ArrayList<>(btDeviceList.size());

        if(btDeviceList.size() > 0){
            for (BluetoothDevice device : btDeviceList){
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                Log.i(TAG, "viewPairedDevices: "+ deviceName);
                Log.i(TAG, "viewPairedDevices: "+ deviceAddress);
                listOfDevices.add(deviceName + "\n" + deviceAddress);
            }
        }
        if(listOfDevices.size() > 0)
        {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfDevices);
            pairedDeviceList.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this, "No Paired Devices", Toast.LENGTH_LONG).show();
        }
    }

    public void findDiscoverableDevices(View view) {

        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(i);
    }
}