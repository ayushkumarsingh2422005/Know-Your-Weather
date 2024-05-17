package com.ayush.knowyourweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ayush.knowyourweather.WeatherData.WeatherData;
import com.ayush.knowyourweather.recyclerViewAdapter.RecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout mainScreen, nevScreen;
    ImageButton imageButton;
    RecyclerView hourlyWeatherRecyclerView, weeklyWeatherRecyclerView;
    private boolean nevState = true;
    private int screenWidth;
    RecyclerViewAdapter recyclerViewAdapter;
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
                nevScreen.animate().translationX((float) (-screenWidth * 0.2)).setDuration(1000).start();
                mainScreen.animate().translationX((float) (screenWidth * 0.8)).setDuration(1000).start();
                nevState = false;
            } else {
                // Move from cover the screen to left of the screen
                nevScreen.animate().translationX(-screenWidth).setDuration(1000).start();
                mainScreen.animate().translationX(0).setDuration(1000).start();
                nevState = true;
            }
        });

        // Initialize RecyclerViews
        hourlyWeatherRecyclerView = findViewById(R.id.hourlyWeatherRecyclerView);
        weeklyWeatherRecyclerView = findViewById(R.id.weeklyWeatherRecyclerView);

        hourlyWeatherRecyclerView.setHasFixedSize(true);
        hourlyWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter hourlyAdapter = new RecyclerViewAdapter(this, hourlyWeatherData);
        hourlyWeatherRecyclerView.setAdapter(hourlyAdapter);

        // Weekly RecyclerView setup (if used)
        weeklyWeatherRecyclerView.setHasFixedSize(true);
        weeklyWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter weeklyAdapter = new RecyclerViewAdapter(this, dailyWeatherData);
        weeklyWeatherRecyclerView.setAdapter(weeklyAdapter);

        loadData();
    }

    public void loadRecyclerView() {
        hourlyWeatherRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, hourlyWeatherData);
        hourlyWeatherRecyclerView.setAdapter(recyclerViewAdapter);
        hourlyWeatherRecyclerView.setLayoutManager(linearLayoutManager);
        Toast.makeText(MainActivity.this, "Recycler view adapters set", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/forecast?q=chunar&units=Metric&appid=2786bf1db3b61f864ba9db93a491cbda", null, new Response.Listener<JSONObject>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject jsonObject) {
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
                    try {
                        sunRise.setText(jsonObject.getJSONArray("list").getJSONObject(0).getString("sunrise"));
                        sunSet.setText(jsonObject.getJSONArray("list").getJSONObject(0).getString("sunset"));
                    } catch (Exception e) {
                        Log.d("Error", "Missing values");
                    }

                    for (int i = 0; i < 8; i++) {
                        JSONObject data = jsonObject.getJSONArray("list").getJSONObject(i);
                        String time = data.getString("dt_txt");
                        String icon = data.getJSONArray("weather").getJSONObject(0).getString("icon");
                        float feelsLike = (float) data.getJSONObject("main").getDouble("feels_like");
                        float tempMin = (float) data.getJSONObject("main").getDouble("temp_min");
                        float tempMax = (float) data.getJSONObject("main").getDouble("temp_max");
                        int humidity = data.getJSONObject("main").getInt("humidity");
                        WeatherData weatherData = new WeatherData(time, icon, humidity, feelsLike, tempMax, tempMin);
                        hourlyWeatherData.add(weatherData);

                    }
                    Toast.makeText(MainActivity.this, "weather data set", Toast.LENGTH_SHORT).show();
                    loadRecyclerView();
                    recyclerViewAdapter.setDataChange(hourlyWeatherData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

            Log.d("myapp", "Something went wrong");
            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }) {
        };
        requestQueue.add(jsonObjectRequest);
    }
}
