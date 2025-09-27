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
    private static final String TEXT_KEY = "zakazivanje_key";

    private String logged_user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        logged_user = intent.getStringExtra(USER_EXPORT_KEY);

        TextView zakazivanje_termina = findViewById(R.id.prikaz_termina);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedText = sharedPreferences.getString(TEXT_KEY, "Nemate zakazan termin");
        zakazivanje_termina.setText(savedText);

        // Simuliranje vec zakazanih termina
        // Ovo je lista Integera, kao vremena kada je nemoguce zakazati
        List<Integer> Scheduled_times= new ArrayList<>();
        Scheduled_times.add(8);
        Scheduled_times.add(10);
        Scheduled_times.add(11);
        Scheduled_times.add(14);

        // Radimo od 8 do 17, svaki termin sat vremena
        List<String> Avalible_times = new ArrayList<>();
        for (int i = 8; i <= 16; i++) {
            if(!Scheduled_times.contains(i)){
                Avalible_times.add(String.valueOf(i) + ":00 - " + String.valueOf(i + 1) +":00");
            }
        }

        //Dodavanje u DropDown
        if(Avalible_times.isEmpty()){
            Avalible_times.add("Nema slobodnih termina :(");
        }
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Avalible_times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        //Podesavanje kalendara
        CalendarView calendarView = findViewById(R.id.calendarView);
        //Minimalni dete (danasnji dan)
        Calendar minDate = Calendar.getInstance();
        calendarView.setMinDate(minDate.getTimeInMillis());
        //Maksimalni dete (1 mesec)
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 1);
        calendarView.setMaxDate(maxDate.getTimeInMillis());
        //Da selektovan bude danasnji dan
        calendarView.setDate(minDate.getTimeInMillis(), false, true);

        //Korisnik slobodno bira datum koji zeli
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                long millis = calendar.getTimeInMillis();
                calendarView.setDate(millis, true, true);
            }
        });

        //Hvatanje Radio Grop-a iz layout-a
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        // Dugme hvata podatke iz kalendara, spinnera i Radio Groupa i smesta ih
        // u Text View koji koristimo za prikaz zakazanog termina
        Button zakazi = findViewById(R.id.zakazi);
        zakazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> zakazivanje_data = new ArrayList<>();

                long selectedDate = calendarView.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateString = "Datum: " + sdf.format(new Date(selectedDate));
                zakazivanje_data.add(dateString);

                String selectedSpinnerItem = "Vreme  " + spinner.getSelectedItem().toString();
                zakazivanje_data.add(selectedSpinnerItem);

                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedRadioButtonId);
                String selectedRadioButtonText = "Frizer " + radioButton.getText().toString();
                zakazivanje_data.add(selectedRadioButtonText);

                TextView prikaz_termina = findViewById(R.id.prikaz_termina);
                StringBuilder stringBuilder = new StringBuilder();
                for(String line : zakazivanje_data){
                    stringBuilder.append(line).append("\n");
                }
                prikaz_termina.setText(stringBuilder.toString());

            }
        });

        // Otkazivanje termina
        Button otkazi = findViewById(R.id.otkazi_termin);
        otkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zakazivanje_termina.setText(getString(R.string.zakazan_termin));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, zakazivanje_termina.getText().toString());
                editor.apply();
            }
        });

        // Vracamo se na prethodni Activity (MainActivity)
        Button nazad = findViewById(R.id.nazad);
        final String TERMIN_EXPORT_KEY = "terminExportKey";

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                if (!zakazivanje_termina.getText().toString().equals(getString(R.string.zakazan_termin))) {
                    intent.putExtra(TERMIN_EXPORT_KEY, zakazivanje_termina.getText().toString());
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, zakazivanje_termina.getText().toString());
                editor.apply();
                startActivity(intent);

            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                if(!zakazivanje_termina.getText().toString().equals(getString(R.string.zakazan_termin))) {
                    intent.putExtra(TERMIN_EXPORT_KEY, zakazivanje_termina.getText().toString());
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT_KEY, zakazivanje_termina.getText().toString());
                editor.apply();
                startActivity(intent);
            }
        });
    }
    // Kada se vratimo na nas Activity za zakazivanje ostace nam prikaz zakazanog termin
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView zakazivanje_termina = findViewById(R.id.prikaz_termina);
        outState.putString(TEXT_STATE_KEY, zakazivanje_termina.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView zakazivanje_termina = findViewById(R.id.prikaz_termina);
        String text = savedInstanceState.getString(TEXT_STATE_KEY, getString(R.string.zakazan_termin));
        zakazivanje_termina.setText(text);
    }


}