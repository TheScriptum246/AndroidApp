package com.example.projekatsalon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {

    //Hashmapa koju koristimo da vodimo evidenciju stanja korpe.
    //Kljuc predstavlja proizvod a value koliko ih ima u korpi
    private HashMap<String, Integer> proizvodi = new HashMap<>();
    ArrayList<TextView> textViews = new ArrayList<>();
    private ScrollView scrollView;


    private void UpdateCart(TextView korpa){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, Integer> entry: proizvodi.entrySet()){
            if(entry.getValue() != 0) {
                stringBuilder.append(entry.getKey()).append(": x").append(String.valueOf(entry.getValue())).append("\n");
            }
        }
        korpa.setText(stringBuilder.toString());
    }

    private void Saltaj(String query){
        for(TextView textView: textViews){
            if (textView.getText().toString().equalsIgnoreCase(query)) {
                scrollView.post(() -> scrollView.scrollTo(0, textView.getTop()));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);



        TextView korpa = findViewById(R.id.korpa);

        //Prikupljanje imena proizvoda
        String makaze_ime = getString(R.string.super_duper_makaze);
        String sampon_ime = getString(R.string.sampon_za_kosu);
        String set_ime = getString(R.string.tri_plus_jedan_gratis_set);
        String vikleri_ime = getString(R.string.vikleri);
        String masinica_ime = getString(R.string.masinica_za_sisanje);
        String ulje_ime = getString(R.string.ulje_za_kosu);
        String stalak_ime = getString(R.string.stalak_za_brijac);


        //Dodavanje proizvoda i koliko ih ima u mapi
        proizvodi.put(makaze_ime,0);
        proizvodi.put(sampon_ime,0);
        proizvodi.put(set_ime,0);
        proizvodi.put(vikleri_ime,0);
        proizvodi.put(masinica_ime,0);
        proizvodi.put(ulje_ime,0);
        proizvodi.put(stalak_ime,0);

        HashMap<String, Integer> defaultMap = new HashMap<>(proizvodi);


        //Listeneri za dodavanje item-a u mapu, prvo hvatanje svih dugmica, pa onda logika onClick

        Button makaze_button = findViewById(R.id.button_makaze);
        Button sampon_button = findViewById(R.id.button_sampon);
        Button set_button = findViewById(R.id.button_set);
        Button vikleri_button = findViewById(R.id.button_vikleri);
        Button masinica_button = findViewById(R.id.button_masinica);
        Button ulje_button = findViewById(R.id.button_ulje);
        Button stalak_button = findViewById(R.id.button_stalak);
        Button obrisi_button = findViewById(R.id.obrisi_korpu);

        makaze_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(makaze_ime) + 1;
                proizvodi.put(makaze_ime, value);
                UpdateCart(korpa);
            }
        });
        sampon_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(sampon_ime) + 1;
                proizvodi.put(sampon_ime, value);
                UpdateCart(korpa);
            }
        });
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(set_ime) + 1;
                proizvodi.put(set_ime, value);
                UpdateCart(korpa);
            }
        });
        vikleri_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(vikleri_ime) + 1;
                proizvodi.put(vikleri_ime, value);
                UpdateCart(korpa);
            }
        });
        masinica_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(masinica_ime) + 1;
                proizvodi.put(masinica_ime, value);
                UpdateCart(korpa);
            }
        });
        ulje_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(ulje_ime) + 1;
                proizvodi.put(ulje_ime, value);
                UpdateCart(korpa);
            }
        });
        stalak_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = proizvodi.get(stalak_ime) + 1;
                proizvodi.put(stalak_ime, value);
                UpdateCart(korpa);
            }
        });

        obrisi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proizvodi = defaultMap;
                korpa.setText(getString(R.string.korpa_sadrzaj));
            }
        });


        scrollView = findViewById(R.id.scrollV);
        textViews.add(findViewById(R.id.makaze_id));
        textViews.add(findViewById(R.id.sampon_id));
        textViews.add(findViewById(R.id.set_id));
        textViews.add(findViewById(R.id.vikleri_id));
        textViews.add(findViewById(R.id.masinica_id));
        textViews.add(findViewById(R.id.ulje_id));
        textViews.add(findViewById(R.id.stalak_id));

        SearchView searchView = findViewById(R.id.search_id);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Saltaj(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Button nazad = findViewById(R.id.nazad);
        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
