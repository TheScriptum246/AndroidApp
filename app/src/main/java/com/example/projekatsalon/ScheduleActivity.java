package com.example.projekatsalon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TEXT_STATE_KEY = "text_state_key";
    private static final String USER_EXPORT_KEY = "user_export_key";

    private static final String PREFS_NAME = "PREFS";
    private static final String TEXT_KEY = "appointment_key";

    private String logged_user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        logged_user = intent.getStringExtra(USER_EXPORT_KEY);

        TextView appointment_display = findViewById(R.id.prikaz_termina);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = sharedPreferences.getString(TEXT_KEY, "No appointment scheduled");
        appointment_display.setText(savedText);

        // Simulating already booked appointments
        // This is a list of Integers, representing times when booking is not possible
        List<Integer> Scheduled_times= new ArrayList<>();
        Scheduled_times.add(9);
        Scheduled_times.add(11);
        Scheduled_times.add(14);
        Scheduled_times.add(16);

        // Working hours from 9 AM to 6 PM, each appointment is 1.5 hours for nail services
        List<String> Available_times = new ArrayList<>();
        int[] startTimes = {9, 11, 13, 15, 17}; // 9:00, 11:00, 1:00, 3:00, 5:00
        String[] timeSlots = {"9:00 AM - 10:30 AM", "11:00 AM - 12:30 PM", "1:00 PM - 2:30 PM",
                "3:00 PM - 4:30 PM", "5:00 PM - 6:30 PM"};

        for (int i = 0; i < startTimes.length; i++) {
            if(!Scheduled_times.contains(startTimes[i])){
                Available_times.add(timeSlots[i]);
            }
        }

        //Adding to DropDown
        if(Available_times.isEmpty()){
            Available_times.add("No available appointments :(");
        }
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Available_times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        //Setting up calendar
        CalendarView calendarView = findViewById(R.id.calendarView);
        //Minimum date (today)
        Calendar minDate = Calendar.getInstance();
        calendarView.setMinDate(minDate.getTimeInMillis());
        //Maximum date (1 month ahead)
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 1);
        calendarView.setMaxDate(maxDate.getTimeInMillis());
        //Set today as selected
        calendarView.setDate(minDate.getTimeInMillis(), false, true);

        //User can freely choose desired date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                long millis = calendar.getTimeInMillis();
                calendarView.setDate(millis, true, true);
            }
        });

        //Getting Radio Group from layout
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        // Button captures data from calendar, spinner and Radio Group and places it
        // in Text View that we use to display the scheduled appointment
        Button book = findViewById(R.id.zakazi);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> appointment_data = new ArrayList<>();

                long selectedDate = calendarView.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                String dateString = "Date: " + sdf.format(new Date(selectedDate));
                appointment_data.add(dateString);

                String selectedSpinnerItem = "Time: " + spinner.getSelectedItem().toString();
                appointment_data.add(selectedSpinnerItem);

                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedRadioButtonId);
                String selectedRadioButtonText = "Technician: " + radioButton.getText().toString();
                appointment_data.add(selectedRadioButtonText);

                TextView appointment_display = findViewById(R.id.prikaz_termina);
                StringBuilder stringBuilder = new StringBuilder();
                for(String line : appointment_data){
                    stringBuilder.append(line).append("\n");
                }
                appointment_display.setText(stringBuilder.toString());
            }
        });

        // Cancel appointment
        Button cancel = findViewById(R.id.otkazi_termin);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointment_display.setText(getString(R.string.no_appointment));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, appointment_display.getText().toString());
                editor.apply();
            }
        });

        // Return to previous Activity (MainActivity)
        Button back = findViewById(R.id.nazad);
        final String APPOINTMENT_EXPORT_KEY = "appointmentExportKey";

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                if (!appointment_display.getText().toString().equals(getString(R.string.no_appointment))) {
                    intent.putExtra(APPOINTMENT_EXPORT_KEY, appointment_display.getText().toString());
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, appointment_display.getText().toString());
                editor.apply();
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                if(!appointment_display.getText().toString().equals(getString(R.string.no_appointment))) {
                    intent.putExtra(APPOINTMENT_EXPORT_KEY, appointment_display.getText().toString());
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, appointment_display.getText().toString());
                editor.apply();
                startActivity(intent);
            }
        });
    }

    // When we return to our scheduling Activity, the scheduled appointment display will remain
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView appointment_display = findViewById(R.id.prikaz_termina);
        outState.putString(TEXT_STATE_KEY, appointment_display.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView appointment_display = findViewById(R.id.prikaz_termina);
        String text = savedInstanceState.getString(TEXT_STATE_KEY, getString(R.string.no_appointment));
        appointment_display.setText(text);
    }
}