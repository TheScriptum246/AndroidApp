package com.example.projekatsalon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TEXT_STATE_KEY = "text_state_key";
    private static final String USER_EXPORT_KEY = "user_export_key";
    private static final String PREFS_NAME = "PREFS";
    private static final String TEXT_KEY = "appointment_key";

    private String loggedUser;
    private NavigationHelper navigationHelper;
    private CalendarView calendarView;
    private Spinner timeSpinner;
    private RadioGroup technicianRadioGroup;
    private TextView appointmentDisplay;
    private Button bookAppointmentButton, cancelAppointmentButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        loggedUser = intent.getStringExtra(USER_EXPORT_KEY);

        initializeViews();
        setupNavigation();
        loadSavedAppointment();
        setupCalendar();
        setupTimeSpinner();
        setupButtons();
        setupBackPressHandler();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendarView);
        timeSpinner = findViewById(R.id.time_spinner);
        technicianRadioGroup = findViewById(R.id.technician_radio_group);
        appointmentDisplay = findViewById(R.id.appointment_display);
        bookAppointmentButton = findViewById(R.id.book_appointment_button);
        cancelAppointmentButton = findViewById(R.id.cancel_appointment_button);
        logoutButton = findViewById(R.id.logout_button);
    }

    private void setupNavigation() {
        navigationHelper = new NavigationHelper(this, loggedUser);
        FrameLayout navigationContainer = findViewById(R.id.navigation_container);
        View navView = navigationHelper.setupNavigation(navigationContainer);
        navigationContainer.addView(navView);
    }

    private void loadSavedAppointment() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = sharedPreferences.getString(TEXT_KEY, getString(R.string.no_appointment));
        appointmentDisplay.setText(savedText);
    }

    private void setupCalendar() {
        Calendar minDate = Calendar.getInstance();
        calendarView.setMinDate(minDate.getTimeInMillis());

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 1);
        calendarView.setMaxDate(maxDate.getTimeInMillis());

        calendarView.setDate(minDate.getTimeInMillis(), false, true);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            long millis = calendar.getTimeInMillis();
            calendarView.setDate(millis, true, true);
        });
    }

    private void setupTimeSpinner() {
        List<Integer> scheduledTimes = new ArrayList<>();
        scheduledTimes.add(9);
        scheduledTimes.add(11);
        scheduledTimes.add(14);
        scheduledTimes.add(16);

        List<String> availableTimes = new ArrayList<>();
        int[] startTimes = {9, 11, 13, 15, 17};
        String[] timeSlots = {
                "9:00 AM - 10:30 AM",
                "11:00 AM - 12:30 PM",
                "1:00 PM - 2:30 PM",
                "3:00 PM - 4:30 PM",
                "5:00 PM - 6:30 PM"
        };

        for (int i = 0; i < startTimes.length; i++) {
            if (!scheduledTimes.contains(startTimes[i])) {
                availableTimes.add(timeSlots[i]);
            }
        }

        if (availableTimes.isEmpty()) {
            availableTimes.add("No available appointments today 😔");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, availableTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setSelection(0);
    }

    private void setupButtons() {
        bookAppointmentButton.setOnClickListener(v -> bookAppointment());

        cancelAppointmentButton.setOnClickListener(v -> cancelAppointment());

        logoutButton.setOnClickListener(v -> logout());
    }

    private void bookAppointment() {
        List<String> appointmentData = new ArrayList<>();

        long selectedDate = calendarView.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String dateString = "📅 Date: " + sdf.format(new Date(selectedDate));
        appointmentData.add(dateString);

        String selectedTime = "⏰ Time: " + timeSpinner.getSelectedItem().toString();
        appointmentData.add(selectedTime);

        int selectedRadioButtonId = technicianRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedRadioButtonId);
        String selectedTechnician = "💅 Technician: " + radioButton.getText().toString();
        appointmentData.add(selectedTechnician);

        StringBuilder stringBuilder = new StringBuilder();
        for (String line : appointmentData) {
            stringBuilder.append(line).append("\n");
        }

        appointmentDisplay.setText(stringBuilder.toString().trim());

        saveAppointment(appointmentDisplay.getText().toString());

        Toast.makeText(this, "✨ Appointment booked successfully! ✨", Toast.LENGTH_LONG).show();
    }

    private void cancelAppointment() {
        appointmentDisplay.setText(getString(R.string.no_appointment));
        saveAppointment(appointmentDisplay.getText().toString());
        Toast.makeText(this, "Appointment cancelled", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        saveAppointment(appointmentDisplay.getText().toString());

        MainActivity.logout();

        Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveAppointment(String appointmentText) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT_KEY, appointmentText);
        editor.apply();
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Prevent going back, user must use navigation or logout
                Toast.makeText(ScheduleActivity.this, "Use navigation buttons to switch screens", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_STATE_KEY, appointmentDisplay.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String text = savedInstanceState.getString(TEXT_STATE_KEY, getString(R.string.no_appointment));
        appointmentDisplay.setText(text);
    }
}