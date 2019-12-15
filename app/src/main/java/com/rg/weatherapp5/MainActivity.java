package com.rg.weatherapp5;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "WeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ForecastApi.create("b69c7c636f94e685ae0f6f5806e5c2bb");

        final RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat("53.6647895");
        request.setLng("10.1214057");
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.ENGLISH);
        //request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {

            @Override
            public void success(WeatherResponse weatherResponse, Response response) {

                Log.d(MainActivity.TAG, "Success ");

                Log.d(MainActivity.TAG, "Current Temperature: " + weatherResponse.getCurrently().getTemperature());


                TextView currentTemp = (TextView) findViewById(R.id.currentTemp);
                String currentTempFormatted = String.format("%.1f", weatherResponse.getCurrently().getTemperature()) + "°C";
                currentTemp.setText(currentTempFormatted);

                Log.d(MainActivity.TAG, "Current Summary: " + weatherResponse.getCurrently().getSummary());

                TextView currentSummary = (TextView) findViewById(R.id.currentSummary);
                String currentSummaryFormatted =  weatherResponse.getCurrently().getSummary();
                currentSummary.setText(currentSummaryFormatted);

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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class TAG {
    }
}
