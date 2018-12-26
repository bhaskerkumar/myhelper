package com.personal.user.myhelper;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by USER on 13-03-2018.
 */

public class MyService2 extends IntentService {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    public MyService2() {
        super(MyService.class.getSimpleName());
    }
    String number;
    StringBuilder msg=new StringBuilder();
    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String(number));
        smsIntent.putExtra("sms_body"  , msg.toString());

        try {
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(smsIntent);
            Log.i("Finished sending SMS...", msg.toString());
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onHandleIntent(Intent i) {
        if (i != null) {
             number = i.getStringExtra("number");
            System.out.println("Service 2 is Started and number is" + number + " ");
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                             latitude= location.getLatitude();
                             longitude= location.getLongitude();
                             msg.append("Latitude is-"+latitude+ " and Longitude is"+longitude+" ");
                             System.out.println(latitude);
                             System.out.println(longitude);
                             System.out.println(msg);
                             sendSMS();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
            }
            else
            {
                locationManager=(LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);
            }

        }

    }



}
