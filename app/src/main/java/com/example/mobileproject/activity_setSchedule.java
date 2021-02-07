package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class activity_setSchedule extends AppCompatActivity {
    MyLocation currentLocation;
    int currentLocationID, state;
    String type;
    private ArrayList<MySchedule> schedules = new ArrayList<>();

    MySchedule mySchedule;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    @SuppressLint("SimpleDateFormat")
    int currentYear = Integer.parseInt(yyyy.format(new Date()));
    int[] date = new int[] {1, 1, currentYear};
    int[] time = new int[] {0, 0};

    public interface MyCallback {
        void onCallback(int myDate, int myMonth, int myYear, int myHour, int myMinute) throws ParseException;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        try {
            loadData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_set_schedule);

        try {
            loadData();
            System.out.println("schedule loading");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getDataFromStorage tmp = new getDataFromStorage(this);
        tmp.saveSchedule(schedules);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem item = menu.findItem(R.id.action_direction);
        item.setVisible(false);
        MenuItem itemHome = menu.findItem(R.id.action_home);
        itemHome.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent a = new Intent(getApplicationContext(), activity_main.class);
                a.putExtra("schedules", schedules);
                a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                setResult(Activity.RESULT_OK, a);
                finish();
            case R.id.action_refresh:
                setContentView(R.layout.activity_set_schedule);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() throws ParseException {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentLocation = (MyLocation) bundle.getSerializable("currentLocation");
        currentLocationID = bundle.getInt("currentLocationID");
        type = bundle.getString("type");
        schedules = (ArrayList<MySchedule>) bundle.getSerializable("schedules");
        state = bundle.getInt("state");

        TextView locationName = findViewById(R.id.textViewName);
        locationName.setText(currentLocation.getLocationName());

        TextView locationAddress = findViewById(R.id.textViewAddress);
        locationAddress.setText(currentLocation.getLocationAddress());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String tmp = date[0] + "/" + date[1] + "/" + date[2] + " " + time[0] + ":" + time[1];
        System.out.println(tmp);
        mySchedule = new MySchedule(type, currentLocationID, dateFormat.parse(tmp), false);

        setDateTime(new MyCallback() {
            @Override
            public void onCallback(int myDate, int myMonth, int myYear, int myHour, int myMinute) throws ParseException {
                date[0] = myDate;
                date[1] = myMonth;
                date[2] = myYear;
                time[0] = myHour;
                time[1] = myMinute;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String tmp = date[0] + "/" + date[1] + "/" + date[2] + " " + time[0] + ":" + time[1];
                System.out.println(tmp);
                mySchedule = new MySchedule(type, currentLocationID, dateFormat.parse(tmp), false);
            }
        });
    }

    void setDateTime(final MyCallback myCallback) {
        final NumberPicker dayPicker = findViewById(R.id.numberPickerDay);
        dayPicker.setMaxValue(30);
        dayPicker.setMinValue(0);
        final String[] monthsWith31Days = new String[31];
        for(int i = 0; i < 31; i++)
            monthsWith31Days[i] = String.valueOf(i + 1);
        dayPicker.setDisplayedValues(monthsWith31Days);

        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                date[0] = dayPicker.getValue() + 1;
                try {
                    myCallback.onCallback(date[0], date[1], date[2], time[0], time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        final NumberPicker monthPicker = findViewById(R.id.numberPickerMonth);
        monthPicker.setMaxValue(11);
        monthPicker.setMinValue(0);
        final String[] monthsOfYear = new String[12];
        for(int i = 0; i < 12; i++)
            monthsOfYear[i] = String.valueOf(i + 1);
        monthPicker.setDisplayedValues(monthsOfYear);

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                date[1] = monthPicker.getValue() + 1;
                try {
                    myCallback.onCallback(date[0], date[1], date[2], time[0], time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        final NumberPicker yearPicker = findViewById(R.id.numberPickerYear);
        yearPicker.setMaxValue(9);
        yearPicker.setMinValue(0);
        final String[] years = new String[10];
        for(int i = 0; i < 10; i++)
            years[i] = String.valueOf(i + currentYear);
        yearPicker.setDisplayedValues(years);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                date[2] = yearPicker.getValue() + currentYear;
                try {
                    myCallback.onCallback(date[0], date[1], date[2], time[0], time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        final NumberPicker hoursPicker = findViewById(R.id.numberPickerHour);
        hoursPicker.setMaxValue(23);
        hoursPicker.setMinValue(0);
        final String[] hours = new String[24];
        for(int i = 0; i < 24; i++)
            hours[i] = String.valueOf(i);
        hoursPicker.setDisplayedValues(hours);

        hoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                time[0] = hoursPicker.getValue();
                try {
                    myCallback.onCallback(date[0], date[1], date[2], time[0], time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        final NumberPicker minutePicker = findViewById(R.id.numberPickerMinute);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        final String[] minutes = new String[60];
        for(int i = 0; i < 60; i++)
            minutes[i] = String.valueOf(i);
        minutePicker.setDisplayedValues(minutes);

        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                time[1] = minutePicker.getValue();
                try {
                    myCallback.onCallback(date[0], date[1], date[2], time[0], time[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onClick_getCurrentLocation(View view) {
        Intent intent_map = new Intent(getApplicationContext(), activity_maps.class);

        intent_map.putExtra("name", currentLocation.getLocationName());
        intent_map.putExtra("address", currentLocation.getLocationAddress());
        //intent_map.putExtra("picture", currentLocation.getLocationPictures());
        intent_map.putExtra("lat", currentLocation.getLocationLatitude());
        intent_map.putExtra("lng", currentLocation.getLocationLongitude());
        startActivity(intent_map);
    }

    public void onClick_saveSchedule(View view) {
        Intent a;
        if(state == -1){
            schedules.add(mySchedule);
            a = new Intent(getApplicationContext(), activity_recommend.class);
        }
        else{
            schedules.set(state, mySchedule);
            a = new Intent(getApplicationContext(), activity_main.class);
        }

        a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        a.putExtra("schedules", schedules);
        setResult(Activity.RESULT_OK, a);
        finish();
    }

    public void onClick_setReminder(View view) {
        schedules.get(schedules.size()-1).setScheduleReminder(true);
    }

    public void onClick_deleteSchedule(View view) {
        Intent a;
        if(state == -1)
            a = new Intent(getApplicationContext(), activity_recommend.class);
        else
        {
            schedules.remove(state);
            a = new Intent(getApplicationContext(), activity_main.class);
        }

        a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        a.putExtra("schedules", schedules);
        setResult(Activity.RESULT_OK, a);
        finish();
    }
}