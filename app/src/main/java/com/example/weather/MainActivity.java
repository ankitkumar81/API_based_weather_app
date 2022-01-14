package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView result;

    private final String url= "http://api.openweathermap.org/data/2.5/weather";
    private final String appid= ""; 
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        result = findViewById(R.id.Result);
    }

    public void getWeatherDetail(View view) {
        String tempURL="";
        String city = cityName.getText().toString().trim();
        if(city.equals("")){
            result.setText("Please enter city/pin");
        }
        else{
            tempURL = url + "?q=" + city + "&appid="+ appid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                   JSONObject jsonResponse = new JSONObject(response);
                   JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                   JSONObject jsonObjectWeather =jsonArray.getJSONObject(0);
                   String description = jsonObjectWeather.getString("description");
                   JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                   double temp = jsonObjectMain.getDouble("temp")-273.15;
                   double feelsLike = jsonObjectMain.getDouble("feels_like")-273.15;
                   float pressure = jsonObjectMain.getInt("pressure");
                   int humidity = jsonObjectMain.getInt("humidity");

                   JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                   String  wind = jsonObjectWind.getString("speed");

                   JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                   String clouds = jsonObjectCloud.getString("all");

                   JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                   String  countryName = jsonObjectSys.getString("country");
                   String cityName = jsonResponse.getString("name");

                   output+=" current weather of "+cityName+", "+ countryName+
                           "\n Temp: "+df.format(temp)+"°C"+
                           "\n Feels like: "+df.format(feelsLike)+"°C" +
                           "\n Humidity: "+ humidity+"%"+
                           "\n Description: "+ description+
                           "\n Wind speed: "+wind+" m/s"+
                           "\n Cloud: "+ clouds+"%"+
                           "\n Pressure: "+ pressure+" hpa";
                   result.setText(output);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
