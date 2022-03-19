package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    String Latitude;
    String Longitude;

    String CITY;
    String API="c4d36921ee2783e189b8feb168a10a50";
    // String Url ="https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=c4d36921ee2783e189b8feb168a10a50";
    //String Current_Api="https://api.openweathermap.org/data/2.5/weather";
    String currentApi="https://api.openweathermap.org/data/2.5/weather?lat="+Latitude+"&lon="+Longitude+"&appid="+API+"";
    ImageView search;
    EditText etCity;
    TextView city, country, Temp, Forecast, time, hum, minTemp, maxTemp, sunRise, sunSet;

    LocationManager locManager;
    LocationListener locListener;
    RelativeLayout customBackground;






    String location_provider = LocationManager.GPS_PROVIDER;

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        customBackground =(RelativeLayout) findViewById(R.id.customBg);

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

        //customBackground.setBackground(Drawable.createFromPath(wIcon));


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CITY = etCity.getText().toString();
                new weatherTask().execute();
               // customBackground.setBackgroundResource(resourceID);




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

            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid="+API+"");

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
                //String fCode = weather.getString("icon");
                //int bgCode=Integer.parseInt(fCode);
                //wCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");



                double temp =jsonObject.getJSONObject("main").getDouble("temp");
                int roundTemp=(int)Math.rint(temp);
                String forecast = weather.getString("description");
                Long lastUpdatedTime = jsonObject.getLong("dt");
                String lastUpdatedTimeText = "Last Updated at:" + new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH).format(new Date(lastUpdatedTime * 1000));
                String Humidity = main.getString("humidity");
                //String min_temp = main.getString("temp_min");
                //wIcon =updateWeatherIcon(wCondition);
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

               // wIcon =updateWeatherBg(bgCode);

                //wIcon=updateWeatherBg(wCondition);





            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForUserLocation();
    }

    public void getWeatherForUserLocation() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locListener = new LocationListener()
        LocationListener locListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {







                RequestParams params =new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",API );
                letsDoSomeWorkings(params);

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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        locManager.requestLocationUpdates(location_provider, MIN_TIME, MIN_DISTANCE, locListener);

    }
    private void letsDoSomeWorkings(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(currentApi,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this,"Data get Successful",Toast.LENGTH_SHORT).show();
                weatherData WeatherD= weatherData.fromJson(response);
                updateUi(WeatherD);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Location Get Successful",Toast.LENGTH_SHORT).show();
                getWeatherForUserLocation();
            }
            else{
                Toast.makeText(MainActivity.this, "Location Services not Granted", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updateUi(weatherData weather){
        city.setText(weather.getwCity());
        country.setText(weather.getwCountry());
        Temp.setText(weather.getwTemp());
        Forecast.setText(weather.getwForecast());
        time.setText(weather.getwTime());
        hum.setText(weather.getwHum());
        minTemp.setText(weather.getwMinTemp());
        maxTemp.setText(weather.getwMaxTemp());
        sunRise.setText(weather.getwSunRise());
        sunSet.setText(weather.getwSunSet());
        int resourceID=getResources().getIdentifier(weather.getwIcon(),"drawable",getPackageName());
        customBackground.setBackgroundResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locManager!=null){
            locManager.removeUpdates(locListener);
        }
    }
}

