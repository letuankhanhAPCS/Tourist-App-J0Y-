package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class getWeatherAndDate {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private Context context;
    private Activity activity;

    public getWeatherAndDate(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void getCurrentWeather() {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);

        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=Ho%20Chi%20Minh,vn&APPID=fc014f8eb6ce25ee841b2375ecc2df5b";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, urlString , null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @SuppressLint({"SetTextI18n", "ShowToast"})
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject main = response.getJSONObject("main");
                            Bitmap bmp = null;

                            ArrayList<int[]> weather_code = new ArrayList<>();

                            int[] flash = {200, 201, 202, 210, 211, 212, 221, 230, 231, 232};
                            int[] rain = {500, 501, 502, 503, 504, 511, 520, 521, 522, 531,
                                    300, 301, 302, 310, 311, 312, 313, 314, 321};
                            int[] snow = {600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622};
                            int[] sun = {800};
                            int[] cloudy = {801, 802, 803, 804, 701, 711, 721, 731, 741, 751, 761, 762, 771, 781};

                            weather_code.add(flash);
                            weather_code.add(rain);
                            weather_code.add(snow);
                            weather_code.add(sun);
                            weather_code.add(cloudy);

                            int[] picture = {
                                    R.drawable.weather_flash,
                                    R.drawable.weather_rain,
                                    R.drawable.weather_snow,
                                    R.drawable.weather_sun,
                                    R.drawable.weather_cloudy
                            };

                            for (int j = 0; j < weather_code.size(); j++) {
                                for(int i = 0; i < weather_code.get(j).length; i++) {
                                    if(weather.getJSONObject(0).getInt("id") == weather_code.get(j)[i]) {
                                        bmp = BitmapFactory.decodeResource(context.getResources(), picture[j]);
                                        break;
                                    }
                                }
                            }

                            ImageView weatherIcons = activity.findViewById(R.id.imageViewWeatherIcon);
                            weatherIcons.setImageBitmap(bmp);

                            TextView temperature = activity.findViewById(R.id.textViewTemperature);
                            double temp = main.getDouble("temp");
                            temp = temp - 273.15;
                            temperature.setText(temp + "º");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "that didn't work", Toast.LENGTH_LONG);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "that didn't work", Toast.LENGTH_LONG);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getCurrentDate() {
        TextView currentDate = activity.findViewById(R.id.textViewCurrentDate);
        String[] dateTime = formatter.format(new Date()).split(" ");
        currentDate.setText(dateTime[0]);
    }
}
