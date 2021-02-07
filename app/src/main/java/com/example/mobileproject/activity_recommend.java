package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class activity_recommend extends AppCompatActivity {
    private static final int RECOMMEND_REQUEST_CODE = 0;
    private ArrayList<MyLocation> locations = new ArrayList<>();
    private ArrayList<MySchedule> schedules = new ArrayList<>();
    private int[] recommendList;
    private String type;

    int currentLocationID = 0, imagePos = 0, numOfImages;
    MyLocation currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_screen);

        Bundle bundle = getIntent().getExtras();
        locations = (ArrayList<MyLocation>) bundle.getSerializable("locations");
        schedules = (ArrayList<MySchedule>) bundle.getSerializable("schedules");
        recommendList = bundle.getIntArray("recommendList");
        type = bundle.getString("type");

        try {
            loadData();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Bugggggggggggggg");
        }
        if (savedInstanceState != null) {
            imagePos = savedInstanceState.getInt("imagePos");
            numOfImages = savedInstanceState.getInt("numOfImages");
            currentLocationID = savedInstanceState.getInt("current_locationId");
            currentLocation = (MyLocation) savedInstanceState.getSerializable("current_location");
            schedules = (ArrayList<MySchedule>) savedInstanceState.getSerializable("listOfSchedule");
            locations = (ArrayList<MyLocation>) savedInstanceState.getSerializable("listOfLocation");
            recommendList = savedInstanceState.getIntArray("listOfRecommendList");
            type = savedInstanceState.getString("listOfType");
            try {
                loadData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        switchBetweenImages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == RECOMMEND_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                assert data != null;
                Bundle bundle = data.getExtras();
                schedules = (ArrayList<MySchedule>) bundle.getSerializable("schedules");
                Toast.makeText(activity_recommend.this, "pass from set to recommend complete", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem item = menu.findItem(R.id.action_direction);
        item.setVisible(false);
        MenuItem itemBack = menu.findItem(R.id.action_back);
        itemBack.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent a = new Intent(getApplicationContext(), activity_main.class);
                a.putExtra("schedules", schedules);
                a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                setResult(Activity.RESULT_OK, a);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("imagePos",imagePos);
        outState.putInt("numOfImages",numOfImages);
        outState.putInt("current_locationId",currentLocationID);
        outState.putSerializable("current_location",currentLocation);
        outState.putSerializable("listOfSchedule",schedules);
        outState.putSerializable("listOfLocation",locations);
        outState.putIntArray("listOfRecommendList",recommendList);
        outState.putString("ListOfType",type);
    }

    @SuppressLint("SetTextI18n")
    private void loadData() throws ParseException {
        currentLocation = locations.get(recommendList[currentLocationID]);

        numOfImages = currentLocation.getLocationPictures().length;
        GridLayout gridLayout = findViewById(R.id.gridNumberOfImageBar);
        CreateObjects createObjects = new CreateObjects(gridLayout, activity_recommend.this);
        createObjects.createNumberOfImageBar(numOfImages);

        checkOpenClose();

        double locationDistance = Double.parseDouble(new DecimalFormat("##.##").format(currentLocation.getLocationDistance()));
        TextView distance = findViewById(R.id.textViewDistance);
        if(locationDistance < 1.0)
            distance.setText(locationDistance * 1000 + " m");
        else
            distance.setText(locationDistance + " km");

        ImageView imageView = (ImageView) findViewById(R.id.imageViewPicture);
        Picasso.get()
                .load(currentLocation.getLocationPictures()[imagePos])
                .placeholder(R.color.colorWhite)
                .error(R.color.colorRed)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("load success");
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("load fail " + e.getMessage());
                    }
                });

        TextView name = findViewById(R.id.textViewName);
        name.setText(currentLocation.getLocationName());

        float rating = Float.parseFloat(currentLocation.getLocationRating().toString());
        RatingBar ratingBar = findViewById(R.id.ratingStar);
        ratingBar.setRating(rating);

        TextView description = findViewById(R.id.textViewDescription);
        description.setText(currentLocation.getLocationDescription());

        TextView address = findViewById(R.id.textViewAddress);
        address.setText(currentLocation.getLocationAddress());

        TextView numberOfRating = findViewById(R.id.textViewNumOfRating);
        numberOfRating.setText("(" + String.valueOf(currentLocation.getNumOfRating()) + ")");
    }

    private void checkOpenClose() throws ParseException {
        ImageView imageView = findViewById(R.id.imageViewOpenCloseSign);
        Bitmap bmp;

        MyTimeZone myTimeZone = currentLocation.getLocationTimeZone();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date openTime = formatter.parse(myTimeZone.getOpenTime());
        Date closeTime = formatter.parse(myTimeZone.getCloseTime());

        String currentTime = formatter.format(new Date());

        int compareOpen = Objects.requireNonNull(formatter.parse(currentTime)).compareTo(openTime);
        int compareClose = Objects.requireNonNull(formatter.parse(currentTime)).compareTo(closeTime);

        if(compareOpen >=  0 && compareClose <= 0)
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sign_open);
        else
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sign_close);

        imageView.setImageBitmap(bmp);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void switchBetweenImages() {
        final ImageView imageView = findViewById(R.id.imageViewPicture);
        imageView.setOnTouchListener(new OnSwipeTouchListener(activity_recommend.this) {
            private static final int MAIN_REQUEST_CODE = 0;

            public void onSwipeTop() {
                Toast.makeText(activity_recommend.this, "bottom", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(getApplicationContext(), activity_setSchedule.class);
                a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.putExtra("currentLocation", currentLocation);
                a.putExtra("currentLocationID", currentLocationID);
                a.putExtra("schedules", schedules);
                a.putExtra("type", type);
                a.putExtra("state", -1);
                startActivityForResult(a, MAIN_REQUEST_CODE);
            }
            public void onSwipeRight() {
                imagePos = 0;
                if(currentLocationID == recommendList.length - 1)
                    currentLocationID = 0;
                else {
                    currentLocationID = currentLocationID + 1;
                }

                try {
                    loadData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switchBetweenImages();
            }
            public void onSwipeLeft() {
                imagePos = 0;
                if(currentLocationID == 0)
                    currentLocationID = recommendList.length - 1;
                else {
                    currentLocationID = currentLocationID - 1;
                }

                try {
                    loadData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switchBetweenImages();
            }
            public void onSwipeBottom() {
                Toast.makeText(activity_recommend.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void onClick_prevPicture(View view) {
        ImageView imageView = findViewById(R.id.imageViewPicture);
        if(imagePos == 0)
            imagePos = numOfImages - 1;
        else
            imagePos--;
        Picasso.get()
                .load(currentLocation.getLocationPictures()[imagePos])
                .placeholder(R.color.colorWhite)
                .error(R.color.colorRed)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("load success");
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("load fail " + e.getMessage());
                    }
                });
    }

    public void onClick_nextPicture(View view) {
        ImageView imageView = findViewById(R.id.imageViewPicture);
        if(imagePos == numOfImages - 1)
            imagePos = 0;
        else
            imagePos++;
        Picasso.get()
                .load(currentLocation.getLocationPictures()[imagePos])
                .placeholder(R.color.colorWhite)
                .error(R.color.colorRed)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("load success");
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("load fail " + e.getMessage());
                    }
                });
    }

    public void onClick_getCurrentLocation(View view) {
        Intent intent_map = new Intent(getApplicationContext(), activity_maps.class);
        intent_map.putExtra("type", type);
        intent_map.putExtra("name", currentLocation.getLocationName());
        intent_map.putExtra("address", currentLocation.getLocationAddress());
        //intent_map.putExtra("picture", currentLocation.getLocationPictures());
        intent_map.putExtra("lat", currentLocation.getLocationLatitude());
        intent_map.putExtra("lng", currentLocation.getLocationLongitude());
        startActivity(intent_map);
    }
}