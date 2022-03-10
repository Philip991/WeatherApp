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
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String CITY;
    String API="4fca6ca1fe380e4678a8a94ee475cc10";
    ImageView search;
    EditText etCity;
    TextView city,country,temp,forecast,time,hum,minTemp,maxTemp,sunRise,sunSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        etCity = (EditText) findViewById(R.id.yourCity);
        search = (ImageView) findViewById(R.id.search);
        city = (TextView) findViewById(R.id.city_name);
        country =(TextView) findViewById(R.id.country_name);
        temp=(TextView) findViewById(R.id.temp);
        forecast=(TextView) findViewById(R.id.forecast);
        time = (TextView) findViewById(R.id.lastUpdatedTime);
        hum=(TextView) findViewById(R.id.humidity);
        minTemp=(TextView) findViewById(R.id.min_temp);
        maxTemp=(TextView) findViewById(R.id.max_temp);
        sunRise=(TextView) findViewById(R.id.sunrise);
        sunSet=(TextView) findViewById(R.id.sunset);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CITY=etCity.getText().toString();

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

            String response = HttpRequest.excuteGet( "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid={"4fca6ca1fe380e4678a8a94ee475cc10"});
);
            return response;
        }
      //  “https://api.openweathermap.org/data/2.5/weather?q=" + CITY + “&units=metric&appid=” + API);
    }


}