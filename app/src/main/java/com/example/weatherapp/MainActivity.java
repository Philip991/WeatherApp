package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String CITY;
    //String API="c4d36921ee2783e189b8feb168a10a50";
    // String Url ="https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=c4d36921ee2783e189b8feb168a10a50";
    ImageView search;
    EditText etCity;
    TextView city, country, Temp, Forecast, time, hum, minTemp, maxTemp, sunRise, sunSet;

    LocationManager locManager;
    LocationListener locListener;

    String location_provider = LocationManager.GPS_PROVIDER;

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        etCity = (EditText) findViewById(R.id.yourCity);
        search = (ImageView) findViewById(R.id.search);
        city = (TextView) findViewById(R.id.city_name);
        country = (TextView) findViewById(R.id.country_name);
        Temp = (TextView) findViewById(R.id.temp);
        Forecast = (TextView) findViewById(R.id.forecast);
        time = (TextView) findViewById(R.id.lastUpdatedTime);
        hum = (TextView) findViewById(R.id.humidity);
        minTemp = (TextView) findViewById(R.id.min_temp);
        maxTemp = (TextView) findViewById(R.id.max_temp);
        sunRise = (TextView) findViewById(R.id.sunrise);
        sunSet = (TextView) findViewById(R.id.sunset);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CITY = etCity.getText().toString();
                new weatherTask().execute();


            }
        });

    }


    class weatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=c4d36921ee2783e189b8feb168a10a50"
            );

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main = jsonObject.getJSONObject("main");
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObject.getJSONObject("sys");

                String city_name = jsonObject.getString("name");
                String country_name = sys.getString("country");
                //String temp = main.getString("temp");
                double temp =jsonObject.getJSONObject("main").getDouble("temp");
                int roundTemp=(int)Math.rint(temp);
                String forecast = weather.getString("description");
                Long lastUpdatedTime = jsonObject.getLong("dt");
                String lastUpdatedTimeText = "Last Updated at:" + new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH).format(new Date(lastUpdatedTime * 1000));
                String Humidity = main.getString("humidity");
                //String min_temp = main.getString("temp_min");
                double min_temp=jsonObject.getJSONObject("main").getDouble("temp_min");
                int roundMinTemp = (int)Math.rint(min_temp);
                //String max_temp = main.getString("temp_max");
                double max_temp = jsonObject.getJSONObject("main").getDouble("temp_max");
                int roundMaxTemp=(int)Math.rint(max_temp);
                Long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date(rise * 1000));
                Long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date(set * 1000));

                city.setText(city_name);
                country.setText(country_name);
                Temp.setText(roundTemp + "\u2103");
                Forecast.setText(forecast);
                time.setText(lastUpdatedTimeText);
                hum.setText(Humidity);
                minTemp.setText(roundMinTemp+ "\u2103");
                maxTemp.setText(roundMaxTemp + "\u2103");
                sunRise.setText(sunrise);
                sunSet.setText(sunset);


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getweatherforuserlocation();
    }

    public void getweatherforuserlocation() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(location_provider, MIN_TIME, MIN_DISTANCE, locListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Location Get Successful",Toast.LENGTH_SHORT).show();
                getweatherforuserlocation();
            }
            else{
                Toast.makeText(MainActivity.this, "Location Services not Granted", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
