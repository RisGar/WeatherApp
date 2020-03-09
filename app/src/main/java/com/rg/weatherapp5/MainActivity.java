package com.rg.weatherapp5;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataBlock;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.thbs.skycons.library.CloudFogView;
import com.thbs.skycons.library.CloudMoonView;
import com.thbs.skycons.library.CloudRainView;
import com.thbs.skycons.library.CloudSnowView;
import com.thbs.skycons.library.CloudSunView;
import com.thbs.skycons.library.CloudThunderView;
import com.thbs.skycons.library.CloudView;
import com.thbs.skycons.library.MoonView;
import com.thbs.skycons.library.SunView;
import com.thbs.skycons.library.WindView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WeatherActivity";

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    String lattitude,longitude;
    double latti;
    double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loadWeather();

    }

    @Override
    protected void onRestart() {

        super.onRestart();

        loadWeather();

    }

    public void loadWeather() {

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        ForecastApi.create("b69c7c636f94e685ae0f6f5806e5c2bb");

        final RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        //request.setLat("53.6647895");
        //request.setLng("10.1214057");
        request.setLat(lattitude);
        request.setLng(longitude);
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.ENGLISH);

        weather.getWeather(request, new Callback<WeatherResponse>() {

            @Override
            public void success(WeatherResponse weatherResponse, Response response) {

                Log.d(MainActivity.TAG, "Success ");

                Log.d(MainActivity.TAG, "Current Temperature: " + weatherResponse.getCurrently().getTemperature());
                TextView currentTemp = (TextView) findViewById(R.id.currentTemp);
                String currentTempFormatted = String.format(Locale.ENGLISH, "%.1f", weatherResponse.getCurrently().getTemperature()) + "°C";
                currentTemp.setText(currentTempFormatted);

                DataPoint dataPoint = weatherResponse.getDaily().getData().get(1);
                double tomorrowTemperature = dataPoint.getTemperatureMin() + dataPoint.getTemperatureMax() / 2;
                Log.d(MainActivity.TAG, "Temperature Tomorrow: " + tomorrowTemperature);
                TextView tomorrowTemp = (TextView) findViewById(R.id.tomorrowTemp);
                String tomorrowtTempFormatted = String.format(Locale.ENGLISH, "Tomorrow: " + "%.1f", tomorrowTemperature) + "°C";
                tomorrowTemp.setText(tomorrowtTempFormatted);

                Log.d(MainActivity.TAG, "Current Summary: " + weatherResponse.getCurrently().getSummary());
                TextView currentSummary = (TextView) findViewById(R.id.currentSummary);
                String currentSummaryFormatted =  weatherResponse.getCurrently().getSummary();
                currentSummary.setText(currentSummaryFormatted);

                getLocationName();

                Log.d(MainActivity.TAG, "Current Icon: " + weatherResponse.getCurrently().getIcon());

                if(weatherResponse.getCurrently().getIcon().equals("cloudy")){

                    cloudView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("partly-cloudy-day")){

                    cloudSunView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("partly-cloudy-night")){

                    cloudMoonView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("wind")){

                    windView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("clear-day")){

                    sunView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("clear-night")){

                    moonView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("rain")){

                    cloudRainView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("snow")){

                    cloudSnowView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("sleet")){

                    cloudSnowView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("fog")){

                    cloudFogView();

                }

                else if(weatherResponse.getCurrently().getIcon().equals("thunderstorm")){

                    cloudThunderView();

                }

                else{

                    cloudView();

                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {

                Log.d(MainActivity.TAG, "Error while calling: " + retrofitError.getUrl());

            }
        });

        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        navigationView.setNavigationItemSelectedListener(this);


    }

    public void cloudThunderView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudThunderView cloudThunderView = new CloudThunderView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudThunderView);


    }

    public void cloudMoonView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudMoonView cloudMoonView = new CloudMoonView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudMoonView);


    }

    public void cloudFogView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudFogView cloudFogView = new CloudFogView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudFogView);


    }

    public void cloudSnowView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudSnowView cloudSnowView = new CloudSnowView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudSnowView);

    }

    public void cloudRainView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudRainView cloudRainView = new CloudRainView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudRainView);


    }

    public void moonView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        MoonView moonView = new MoonView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(moonView);

    }

    public void sunView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        SunView sunView = new SunView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(sunView);


    }

    public void cloudSunView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudSunView cloudSunView = new CloudSunView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudSunView);

    }

    public void cloudView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        CloudView cloudView = new CloudView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(cloudView);

    }

    public void windView() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.icon);
        //Using these params, you can control view attributes
        //attributes include boolean isStatic,boolean isAnimated, int strokeColor , int backgroundColor
        WindView windView = new WindView(this,true,false, Color.parseColor("#FFFFFF"),Color.parseColor("#00FFFFFF"));

        layout.addView(windView);

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location1 != null) {

                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            }

            else if (location != null) {

                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            }

            else  if (location2 != null) {

                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            }

            else {

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void getLocationName() {

        TextView location = (TextView) findViewById(R.id.location);

        try {

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.ENGLISH);
            List<Address> addresses = geo.getFromLocation(latti, longi, 1);
            if (addresses.isEmpty()) {
                location.setText("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {
                    location.setText(addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName());
                }
            }
        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int id) {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int id) {

                        dialog.cancel();

                    }
                });

        final AlertDialog alert = builder.create();

        alert.show();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {

            super.onBackPressed();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            // NavigationGallery
        } else if (id == R.id.nav_slideshow) {
            // NavigationSlideshow
        } else if (id == R.id.nav_tools) {
            // NavigationTools
        } else if (id == R.id.nav_share) {
            // NavigationShare
        } else if (id == R.id.nav_send) {
            // NavigationSend
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
