package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyScheduleAdapter extends ArrayAdapter<MySchedule> {
    private static final int MAIN_REQUEST_CODE = 0;
    private Context _context;
    private int _layoutID;
    private ArrayList<MySchedule> _items;
    private ArrayList<ArrayList<MyLocation>> type;

    String[] locationType;

    public MyScheduleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MySchedule> _items, ArrayList<ArrayList<MyLocation>> type) {
        super(context, resource, _items);
        this._context = context;
        this._layoutID = resource;
        this._items = _items;
        this.type = type;

        locationType = context.getResources().getStringArray(R.array.locationType);
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_layoutID, null, false);
        }

        TextView textViewLocationName = convertView.findViewById(R.id.textViewScheduleLocationName);
        TextView textViewDateTime = convertView.findViewById(R.id.textViewScheduleDateTime);
        ImageButton buttonEdit = convertView.findViewById(R.id.buttonScheduleEdit);

        final MySchedule schedule = _items.get(position);
        String str_type = "";
        ArrayList<MyLocation> locations = new ArrayList<>();
        for (int i = 0; i < locationType.length; i++){
            if(schedule.getLocationType().equals(locationType[i]))
            {
                locations = type.get(i);
                str_type = locationType[i];
            }
        }

        final int currentPosition = schedule.getLocationIndex();
        textViewLocationName.setText(locations.get(currentPosition).getLocationName());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        textViewDateTime.setText(formatter.format(schedule.getScheduleDate()));

        buttonEdit.setTag(position);
        final MyLocation myLocation = locations.get(currentPosition);
        final String finalStr_type = str_type;
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(_context.getApplicationContext(), activity_setSchedule.class);
                a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.putExtra("currentLocationID", currentPosition);
                a.putExtra("currentLocation", myLocation);
                a.putExtra("type", finalStr_type);
                a.putExtra("schedules", _items);
                a.putExtra("state", position);
                ((Activity) _context).startActivityForResult(a, MAIN_REQUEST_CODE);
            }
        });

        return convertView;
    }
}