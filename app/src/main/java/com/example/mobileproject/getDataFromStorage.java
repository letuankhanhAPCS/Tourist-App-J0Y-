package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class getDataFromStorage {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private Context context;

    public getDataFromStorage(Context context) {
        this.context = context;
    }

    public String ReadData(String filename) {
        FileInputStream fis;
        try {
            fis = context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();

            for (String text; (text = bufferedReader.readLine()) != null; ) {
                stringBuilder.append(text);
            }
            Toast.makeText(context, "Reading Successful", Toast.LENGTH_SHORT).show();
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Reading - File not found Error", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Reading Error", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void SaveData(String string, String filename) {
        if (isExternalStorageAvailable()) {
            FileOutputStream fos;
            try {
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(string.getBytes());
                //Toast.makeText(MainActivity.this, "Saving Successful", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(context, "Saving - File not found Error", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, "Saving Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void SaveDataAppend(String string, String filename) throws IOException {
        File file = new File(filename);
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        br.write(string);

        br.close();
        fr.close();
    }

    private boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }

    public void saveSchedule(ArrayList<MySchedule> schedules) {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < schedules.size(); i++) {
            string.append(schedules.get(i).getLocationType()).append("_")
                    .append(schedules.get(i).getLocationIndex()).append("_")
                    .append(formatter.format(schedules.get(i).getScheduleDate())).append("_")
                    .append(schedules.get(i).isScheduleReminder()).append(";");
        }
        System.out.println(string.toString());
        SaveData(string.toString(), "schedule.txt");
    }

    public void loadSchedule(ArrayList<MySchedule> schedules) throws ParseException {
        if(!fileExists(context, "schedule.txt")) {
            String unlocked = ";";
            SaveData(unlocked, "schedule.txt");
        }
        String[] items = Objects.requireNonNull(ReadData("schedule.txt")).split(";");
        for (String item : items) {
            String[] attributes = item.split("_", 4);
            if(attributes.length == 4) {
                Date scheduleDate = formatter.parse(attributes[2]);
                boolean reminder;
                reminder = attributes[3].equals("1");
                MySchedule schedule = new MySchedule(attributes[0], Integer.parseInt(attributes[1]), scheduleDate, reminder);
                schedules.add(schedule);
            }
        }
    }
}
