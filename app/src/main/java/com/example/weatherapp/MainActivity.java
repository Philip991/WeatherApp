package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
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
    TextView city,country,Temp,Forecast,time,hum,minTemp,maxTemp,sunRise,sunSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        etCity = (EditText) findViewById(R.id.yourCity);
        search = (ImageView) findViewById(R.id.search);
        city = (TextView) findViewById(R.id.city_name);
        country =(TextView) findViewById(R.id.country_name);
        Temp=(TextView) findViewById(R.id.temp);
        Forecast=(TextView) findViewById(R.id.forecast);
        time = (TextView) findViewById(R.id.lastUpdatedTime);
        hum=(TextView) findViewById(R.id.humidity);
        minTemp=(TextView) findViewById(R.id.min_temp);
        maxTemp=(TextView) findViewById(R.id.max_temp);
        sunRise=(TextView) findViewById(R.id.sunrise);
        sunSet=(TextView) findViewById(R.id.sunset);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   CITY=etCity.getText().toString();
                new weatherTask().execute();


            }
        });

    }
    class weatherTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response=HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=lagos&units=metric&appid=c4d36921ee2783e189b8feb168a10a50"
);

            return response;
        }

        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main= jsonObject.getJSONObject("main");
                JSONObject weather= jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObject.getJSONObject("sys");

                String city_name = jsonObject.getString("name");
                String country_name = sys.getString("country");
                String temp = main.getString("temp");
                String forecast = weather.getString("description");
                Long lastUpdatedTime= jsonObject.getLong("dt");
                String lastUpdatedTimeText="Last Updated at:"+new SimpleDateFormat("dd/MM/yyyy hh:mm",Locale.ENGLISH).format(new Date(lastUpdatedTime *1000));
                String Humidity = main.getString("humidity");
                String min_temp = main.getString("temp_min");
                String max_temp = main.getString("temp_max");
                Long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm",Locale.ENGLISH).format(new Date(rise *1000));
                Long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date(set *1000));

                city.setText(city_name);
                country.setText(country_name);
                Temp.setText(temp +"\u2103");
                Forecast.setText(forecast);
                time.setText(lastUpdatedTimeText);
                hum.setText(Humidity);
                minTemp.setText(min_temp+"\u2103");
                maxTemp.setText(max_temp+"\u2103");
                sunRise.setText(sunrise);
                sunSet.setText(sunset);




            } catch (JSONException e) {
               Toast.makeText(MainActivity.this, "Error"+e.toString(),Toast.LENGTH_SHORT).show();
            }


        }
    }


}