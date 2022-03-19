package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class weatherData {

    private String wIcon, wCity, wCountry, wTemp, wForecast, wTime, wHum, wMinTemp, wMaxTemp, wSunRise, wSunSet;
    private int wCondition;

    public static weatherData fromJson(JSONObject jsonObject){
        try {
            weatherData WeatherD=new weatherData();
            WeatherD.wCity=jsonObject.getString("name");
            WeatherD.wCountry=jsonObject.getJSONObject("sys").getString("country");
            double temp=jsonObject.getJSONObject("main").getDouble("temp");
            int roundTemp=(int)Math.rint(temp);
            WeatherD.wTemp=Integer.toString(roundTemp);
            WeatherD.wCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            WeatherD.wIcon=updateWeatherBg(WeatherD.wCondition);
            WeatherD.wForecast=jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            Long lastUpdatedTime=jsonObject.getLong("dt");
            WeatherD.wTime="Last Updated at:" + new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH).format(new Date(lastUpdatedTime * 1000));
            WeatherD.wHum=jsonObject.getJSONObject("main").getString("humidity");
            double minTemp=jsonObject.getJSONObject("main").getDouble("temp_min");
            int roundMinTemp=(int)Math.rint(minTemp);
            WeatherD.wMinTemp=Integer.toString(roundMinTemp);
            double maxTemp=jsonObject.getJSONObject("main").getDouble("temp_max");
            int roundMaxTemp=(int)Math.rint(maxTemp);
            WeatherD.wMaxTemp=Integer.toString(roundMaxTemp);
            Long rise = jsonObject.getJSONObject("sys").getLong("sunrise");
            WeatherD.wSunRise=new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date(rise * 1000));
            Long set =jsonObject.getJSONObject("sys").getLong("sunset");
            new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date(set * 1000));
            return WeatherD;



        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String updateWeatherBg(int condition) {
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

    }

    public String getwIcon() {
        return wIcon;
    }

    public String getwCity() {
        return wCity;
    }

    public String getwCountry() {
        return wCountry;
    }

    public String getwTemp() {
        return wTemp;
    }

    public String getwForecast() {
        return wForecast;
    }

    public String getwTime() {
        return wTime;
    }

    public String getwHum() {
        return wHum;
    }

    public String getwMinTemp() {
        return wMinTemp;
    }

    public String getwMaxTemp() {
        return wMaxTemp;
    }

    public String getwSunRise() {
        return wSunRise;
    }

    public String getwSunSet() {
        return wSunSet;
    }
}
