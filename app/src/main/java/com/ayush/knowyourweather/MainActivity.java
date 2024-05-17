package com.ayush.knowyourweather;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ayush.knowyourweather.WeatherData.WeatherData;
import com.ayush.knowyourweather.recyclerViewAdapter.RecyclerViewAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout mainScreen, nevScreen;
    ImageButton imageButton;
    RecyclerView hourlyWeatherRecyclerView, weeklyWeatherRecyclerView;
    private boolean nevState = true;
    private int screenWidth;
    List<WeatherData> hourlyWeatherData = new ArrayList<>();
    List<WeatherData> dailyWeatherData = new ArrayList<>();

    // Activity layout button


    // Activity Layout decleration end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ConstraintLayouts
        mainScreen = findViewById(R.id.mainScreen);
        nevScreen = findViewById(R.id.nevScreen);
        imageButton = findViewById(R.id.nevButton);
        hourlyWeatherRecyclerView = findViewById(R.id.hourlyWeatherRecyclerView);
        weeklyWeatherRecyclerView = findViewById(R.id.weeklyWeatherRecyclerView);

        // Calculate screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = (displayMetrics.widthPixels);

        // Set initial translateX for nevScreen
        nevScreen.setTranslationX(-screenWidth); // Initial translateX

        // Button handler
        imageButton.setOnClickListener(view -> {
            if (nevState) {
                // Move from left of the screen to cover the screen
                nevScreen.animate().translationX((float) (-screenWidth*0.2)).setDuration(1000).start();
                mainScreen.animate().translationX((float) (screenWidth*0.8)).setDuration(1000).start();
                nevState = false;
            } else {
                // Move from cover the screen to left of the screen
                nevScreen.animate().translationX(-screenWidth).setDuration(1000).start();
                mainScreen.animate().translationX(0).setDuration(1000).start();
                nevState = true;
            }
        });


        loadData();

    }

    public void loadData(){
        TextView tempMainShow, weatherStatus, tempDesc, humidityVal, windSpeed, windDir, groundPressure, seaPressure, cloud, visibility, sunRise, sunSet, cityName;
        tempMainShow = findViewById(R.id.tempMainShow);
        weatherStatus = findViewById(R.id.weatherStatus);
        tempDesc = findViewById(R.id.tempDesc);
        humidityVal = findViewById(R.id.humidityVal);
        windSpeed = findViewById(R.id.windSpeed);
        windDir = findViewById(R.id.windDir);
        groundPressure = findViewById(R.id.groundPressure);
        seaPressure = findViewById(R.id.seaPressure);
        cloud = findViewById(R.id.cloud);
        visibility = findViewById(R.id.visibility);
        sunRise = findViewById(R.id.sunRise);
        sunSet = findViewById(R.id.sunSet);
        cityName = findViewById(R.id.cityName);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.openweathermap.org/data/2.5/forecast?q=chunar&units=Metric&appid=2786bf1db3b61f864ba9db93a491cbda", null, jsonObject -> {
            try {
                cityName.setText(jsonObject.getJSONObject("city").getString("name"));
                tempMainShow.setText(String.format("%s°", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp")));
                weatherStatus.setText(jsonObject.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main"));
                tempDesc.setText(String.format("%s° / %s° feels like %s°", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_min"), jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_max"), jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("feels_like")));
                humidityVal.setText(String.format("%s%%", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("humidity")));
                windSpeed.setText(String.format("%s m/sec", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("wind").getString("speed")));
                windDir.setText(String.format("%s deg", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("wind").getString("deg")));
                groundPressure.setText(String.format("%s HPa", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("grnd_level")));
                seaPressure.setText(String.format("%s HPa", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("sea_level")));
                cloud.setText(String.format("%s %%", jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("clouds").getString("all")));
                visibility.setText(String.format("%s m", jsonObject.getJSONArray("list").getJSONObject(0).getString("visibility")));
                sunRise.setText(jsonObject.getJSONArray("list").getJSONObject(0).getString("sunrise"));
                sunSet.setText(jsonObject.getJSONArray("list").getJSONObject(0).getString("sunset"));


                for(int i = 0; i < 8; i++) {
                    JSONObject data = jsonObject.getJSONArray("list").getJSONObject(i);
                    String time = data.getString("dt_txt");
                    String icon = data.getJSONArray("weather").getJSONObject(0).getString("icon");
                    float temp = (float) data.getJSONObject("main").getDouble("temp");
                    float feelsLike = (float) data.getJSONObject("main").getDouble("feels_like");
                    float tempMin = (float) data.getJSONObject("main").getDouble("temp_min");
                    float tempMax = (float) data.getJSONObject("main").getDouble("temp_max");
                    int humidity = data.getJSONObject("main").getInt("humidity");
                    WeatherData weatherData = new WeatherData(time, icon, humidity, feelsLike, tempMax, tempMin);
                    hourlyWeatherData.add(weatherData);
                }
                hourlyWeatherRecyclerView.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                hourlyWeatherRecyclerView.setLayoutManager(linearLayoutManager);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, hourlyWeatherData);
                hourlyWeatherRecyclerView.setAdapter(recyclerViewAdapter);

//        WeatherData weatherData = new WeatherData("12:00", "image", 23, 45.01f, 25.05f);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                dailyWeatherData.add(weatherData);
//                weeklyWeatherRecyclerView.setHasFixedSize(true);
//                linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//                weeklyWeatherRecyclerView.setLayoutManager(linearLayoutManager);
//                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, dailyWeatherData);
//                weeklyWeatherRecyclerView.setAdapter(recyclerViewAdapter);

            } catch (Exception e){
                Log.d(e.getMessage(), "vollyerror");
            }
        }, volleyError -> Log.d(volleyError.getMessage(), "megvolly"));

        requestQueue.add(jsonObjectRequest);

    }
}
