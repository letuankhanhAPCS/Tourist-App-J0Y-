package com.example.mobileproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.util.ArrayList;

public class activity_main extends AppCompatActivity{
    private static final int MAIN_REQUEST_CODE = 0;

    private ArrayList<MySchedule> schedules = new ArrayList<>();
    private ArrayList<ArrayList<MyLocation>> locationsList = new ArrayList<>();

    private String[] locationType;
    private int nextId = 10000;

    private getDataFromServer getDataFromServer = new getDataFromServer(this);
    private int[] typeIndex = {-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 44);
        checkPermission(Manifest.permission.INTERNET, 44);

        locationType = getResources().getStringArray(R.array.locationType);

        getDataFromStorage getSchedule = new getDataFromStorage(activity_main.this);
        try {
            getSchedule.loadSchedule(schedules);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        loadLocationFromServer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getDataFromStorage tmp = new getDataFromStorage(this);
        tmp.saveSchedule(schedules);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == MAIN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                assert data != null;
                Bundle bundle = data.getExtras();
                schedules = (ArrayList<MySchedule>) bundle.getSerializable("schedules");
                initListView(schedules);
            }
        }
    }

    private void loadLocationFromServer() {
        typeIndex[0]++;
        getDataFromServer.loadLocation(locationType[typeIndex[0]], new getDataFromServer.MyCallback() {
            @Override
            public void onCallback(ArrayList<MyLocation> myLocations) {
                locationsList.add(typeIndex[0], myLocations);
                System.out.println(locationType[typeIndex[0]] + " " + myLocations.size());
                if (typeIndex[0] == locationType.length - 1) {
                    System.out.println(typeIndex[0] + " finish loading");
                    getLocation();
                    initListView(schedules);
                    createLocationTypeButton();
                } else
                    loadLocationFromServer();
            }
        });
    }

    private void getLocation() {
        MyLocation deviceLocation = new MyLocation(0.0, 0.0);
        for(int i = 0; i < 9; i++){
            getDeviceLocation getDeviceLocation = new getDeviceLocation(activity_main.this, deviceLocation, locationsList.get(i));
        }

        getWeatherAndDate getWeatherAndDate = new getWeatherAndDate(activity_main.this, activity_main.this);
        getWeatherAndDate.getCurrentDate();
        getWeatherAndDate.getCurrentWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem item = menu.findItem(R.id.action_direction);
        item.setVisible(false);
        MenuItem itemHome = menu.findItem(R.id.action_home);
        itemHome.setVisible(false);
        MenuItem itemBack = menu.findItem(R.id.action_back);
        itemBack.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                setContentView(R.layout.activity_main);
                getLocation();
                initListView(schedules);
                createLocationTypeButton();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initListView(ArrayList<MySchedule> schedules) {
        ListView listView_schedule = findViewById(R.id.listViewSchedule);
        MyScheduleAdapter myScheduleAdapter = new MyScheduleAdapter(this, R.layout.schedule_list_layout, schedules, locationsList);
        listView_schedule.setAdapter(myScheduleAdapter);
    }

    private void createLocationTypeButton() {
        GridLayout gridLayout = findViewById(R.id.gridLayoutType);
        for (int i = 0; i < locationType.length; i++) {
            ImageButton imageButton = createImageButton(i);
            gridLayout.addView(imageButton);
        }
    }

    private int getNextID() {return nextId++;}

    private ImageButton createImageButton(final int s) {
        final ArrayList<MyLocation> locations;
        final ImageButton imageButton = new ImageButton(this);

        int[] pictures = new int[] {
                R.drawable.type_coffee, R.drawable.type_restaurant, R.drawable.type_cinema, R.drawable.type_theme_park, R.drawable.type_park,
                R.drawable.type_zoo, R.drawable.type_museum, R.drawable.type_shopping_mall, R.drawable.type_sport
        };

        imageButton.setBackgroundResource(pictures[s]);
        locations = locationsList.get(s);
        imageButton.setId(getNextID());

        View.OnClickListener helper = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] recommendList = new int[locations.size()];
                double[] distanceList = new double[locations.size()];

                for (int i = 0; i < recommendList.length; i++) {
                    recommendList[i] = i;
                    System.out.println(distanceList[i]);
                    distanceList[i] = locations.get(i).getLocationDistance();
                }

                for(int i = 0; i < recommendList.length; i++) {
                    for(int j = i + 1; j < recommendList.length; j++)
                    if (distanceList[i] + locations.get(i).getLocationRating()*100 > distanceList[j]+ locations.get(i).getLocationRating() * 100) {
                        double tmp1 = distanceList[i];
                        distanceList[i] = distanceList[j];
                        distanceList[j] = tmp1;

                        int tmp2 = recommendList[i];
                        recommendList[i] = recommendList[j];
                        recommendList[j] = tmp2;
                    }
                }

                Intent a = new Intent(getApplicationContext(), activity_recommend.class);
                a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.putExtra("recommendList", recommendList);
                a.putExtra("locations", locations);
                a.putExtra("schedules", schedules);
                a.putExtra("type", locationType[s]);
                startActivityForResult(a, MAIN_REQUEST_CODE);
            }
        };
        imageButton.setOnClickListener(helper);
        return imageButton;
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity_main.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(activity_main.this,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(activity_main.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity_main.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity_main.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

}