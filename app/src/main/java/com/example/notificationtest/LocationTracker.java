package com.example.notificationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationTracker extends AppCompatActivity implements
    FetchAddressTask.OnTaskCompleted{

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "Location";
    private Button button_location;
    private  Button map_location;
    private ImageView panicImage;
    private String loc;

    private Location mLastLocation;
    private TextView textview_location;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_location_tracker);

        button_location = findViewById(R.id.button_location);
        textview_location = findViewById(R.id.textview_location);
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        map_location = findViewById(R.id.map_location);


        //initialize the fused location client provider
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        panicImage = findViewById(R.id.panicImage);
        panicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLocation();
                sendNotification(loc);
            }
        });
        setNotificationButtonState(true, false, false);
        createNotificationChannel();

    }

    public void getLocation(){

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        }else{
           // Log.d(TAG,"getLocation: permissions granted");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                //mLastLocation = location;
                                new FetchAddressTask(
                                        LocationTracker.this
                                        ,LocationTracker.this)
                                        .execute(location);

                            }else{
                                textview_location.setText(R.string.no_location);
                            }
                        }
                    }
            );

        }

        //show some loading text while the
        // FetchAddressTask runs in the background
         textview_location.setText(getString(R.string.address_text,
                getString(R.string.loading),
                System.currentTimeMillis()));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
     @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case REQUEST_LOCATION_PERMISSION:
                //if a permission is granted, get the location,
                // otherwise show a message
                if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else{
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        //update our UI
        textview_location.setText(getString(R.string.address_text,result,System.currentTimeMillis()));

        loc = textview_location.getText().toString();

    }

    public void mapLocation(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void sendNotification(String location) {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(location);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        setNotificationButtonState(false, true, true);
    }
    private NotificationCompat.Builder getNotificationBuilder(String location){

        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("PANIC ALERT!!!")
                .setContentText(location)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp);

        return notifyBuilder;
    }

    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    void setNotificationButtonState(Boolean isNotifyEnabled,
                                    Boolean isUpdateEnabled,
                                    Boolean isCancelEnabled) {
      //  panicImage.setEnabled(isNotifyEnabled);
        //.setEnabled(isCancelEnabled);
    }




}
