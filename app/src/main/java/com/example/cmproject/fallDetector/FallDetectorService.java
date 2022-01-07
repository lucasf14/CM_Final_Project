package com.example.cmproject.fallDetector;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.cmproject.dbHandler.DatabaseHandler;
import com.example.cmproject.objects.Alarm;
import com.example.cmproject.objects.Contact;

import java.util.List;

public class FallDetectorService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    //GPS
    double latitude, longitude;
    LocationManager locationManager;
    LocationListener locationListener;
    //SMS Variables
    SmsManager smsManager = SmsManager.getDefault();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("Create FallDetector");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        latitude = locationManager.getLastKnownLocation(locationProvider).getLatitude();
        longitude = locationManager.getLastKnownLocation(locationProvider).getLongitude();

        //Init Sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this,
                senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double loX = event.values[0];
            double loY = event.values[1];
            double loZ = event.values[2];

            double loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                    + Math.pow(loY, 2)
                    + Math.pow(loZ, 2));

            DecimalFormat precision = new DecimalFormat("0.00");
            double ldAccRound = Double.parseDouble(precision.format(loAccelerationReader));

            if(ldAccRound<2.0)
            {
                //Toast.makeText(this, "Fall detected", Toast.LENGTH_SHORT).show();
                sendMessage();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void sendMessage(){
        List<Contact> contactList = (new DatabaseHandler(this).getContacts());
        if(contactList.size() == 1){
            Contact contact = contactList.get(0);
            String textMsg = "Sensed Danger here => " + "http://maps.google.com/?q=<" + String.valueOf(latitude) + ">,<" + String.valueOf(longitude) + ">";
            System.out.println("Sending-MSG: " + textMsg + " to number" + contact.getContact());
            smsManager.sendTextMessage(contact.getContact(), null, textMsg, null, null);
            //calls Number
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getContact()));
            startActivity(intent);
        }
    }
}
