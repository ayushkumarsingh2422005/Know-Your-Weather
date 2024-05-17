package com.ayush.knowyourweather.WeatherData;

public class WeatherData {
    private String time;
    private String image;
    private int pricipatation;
    private float temp;
    private float temp_max;
    private float temp_min;

    public WeatherData(String time, String image, int pricipatation, float temp, float temp_max, float temp_min) {
        this.time = time;
        this.image = image;
        this.pricipatation = pricipatation;
        this.temp = temp;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }
    public String getTime() {
        return time;
    }
    public String getImage() {
        return image;
    }
    public String getPricipatation() {
        return String.valueOf(pricipatation);
    }
    public String getTemp() {
        return String.valueOf(temp);
    }
    public float getTemp_max() {
        return temp_max;
    }
    public float getTemp_min() {
        return temp_min;
    }
}

