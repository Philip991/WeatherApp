package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {



    String CITY;
    String API="c4d36921ee2783e189b8feb168a10a50";
    //String Url ="https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid="+API+"";
    //String Current_Api="https://api.openweathermap.org/data/2.5/weather";
    //String currentApi="https://api.openweathermap.org/data/2.5/weather?lat=&lon=&appid=c4d36921ee2783e189b8feb168a10a50";
    String currentApi="http://api.openweathermap.org/data/2.5/weather?";
    ImageView search,navBtn;
    EditText etCity;
    TextView city, country, Temp, Forecast, time, hum, minTemp, maxTemp, sunRise, sunSet;

    LocationManager locManager;
    LocationListener locListener;
    RelativeLayout customBackground;
    String wIcon;
    int wCondition;






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

        navBtn=(ImageView)findViewById(R.id.navBtn);
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

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherForUserLocation();
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


               //wCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
                //wIcon=updateWeatherBg(wCondition);

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
                //int resourceID=getResources().getIdentifier(getwIcon(),"drawable",getPackageName());
                //customBackground.setBackgroundResource(resourceID);



               // wIcon =updateWeatherBg(bgCode);

                //wIcon=updateWeatherBg(wCondition);





            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }

  /* private String getwIcon() {
        return wIcon;
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForUserLocation();
    }


    public void getWeatherForUserLocation() {
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener(){
       // LocationListener locListener=new LocationListener()
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLatitude());

               /*String cityName = "";
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address>addresses;
                try {
                    addresses=gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(addresses.size()>0){
                        cityName=addresses.get(0).getLocality();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                RequestParams params =new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                //params.put("q",cityName);
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
                Toast.makeText(MainActivity.this,"Data get failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Location Get Successful",Toast.LENGTH_SHORT).show();
               // getWeatherForUserLocation();

            }
            else{
                Toast.makeText(MainActivity.this, "Location Services not Granted", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updateUi(weatherData weather){
        city.setText(weather.getwCity());
        country.setText(weather.getwCountry());
        Temp.setText(weather.getwTemp()+ "\u2103");
        Forecast.setText(weather.getwForecast());
        time.setText(weather.getwTime());
        hum.setText(weather.getwHum());
        minTemp.setText(weather.getwMinTemp()+ "\u2103");
        maxTemp.setText(weather.getwMaxTemp()+ "\u2103");
        sunRise.setText(weather.getwSunRise());
        sunSet.setText(weather.getwSunSet());
       // int resourceID=getResources().getIdentifier(weather.getwIcon(),"drawable",getPackageName());
        //customBackground.setBackgroundResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locManager!=null){
            locManager.removeUpdates(locListener);
        }
    }

  /* public static String updateWeatherBg(int condition) {
        if (condition == 800) {
            return "a01d";
        } else if (condition == 801) {
            return "a02d";
        } else if (condition == 802) {
            return "a03d";
        } else if (condition == 803 || condition == 804) {
            return "a04d";
        } else if (condition >= 300 && condition <= 321 || condition >= 520 && condition <= 531) {
            return "a09d";
        } else if (condition >= 500 && condition <= 504) {
            return "a10d";
        } else if (condition >= 200 && condition <= 232) {
            return "a11d";
        } else if (condition >= 600 && condition <= 622 || condition == 511) {
            return "a13d";
        } else if (condition >= 701 && condition <= 781) {
            return "a50d";
        }
        return "error";

    }*/
}

