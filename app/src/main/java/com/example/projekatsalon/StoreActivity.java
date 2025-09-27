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

    //HashMap to track shopping cart state.
    //Key represents product and value shows quantity in cart
    private HashMap<String, Integer> products = new HashMap<>();
    ArrayList<TextView> textViews = new ArrayList<>();
    private ScrollView scrollView;


    private void UpdateCart(TextView cart){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, Integer> entry: products.entrySet()){
            if(entry.getValue() != 0) {
                stringBuilder.append(entry.getKey()).append(": x").append(String.valueOf(entry.getValue())).append("\n");
            }
        }
        cart.setText(stringBuilder.toString());
    }

    private void ScrollToProduct(String query){
        for(TextView textView: textViews){
            if (textView.getText().toString().toLowerCase().contains(query.toLowerCase())) {
                scrollView.post(() -> scrollView.scrollTo(0, textView.getTop()));
                break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        TextView cart = findViewById(R.id.korpa);

        //Getting product names
        String nailPolish_name = getString(R.string.nail_polish_set);
        String cuticleCare_name = getString(R.string.cuticle_care);
        String manicureKit_name = getString(R.string.manicure_starter_kit);
        String nailFiles_name = getString(R.string.nail_files);
        String nailLamp_name = getString(R.string.nail_lamp);
        String handCream_name = getString(R.string.hand_cream);
        String nailArt_name = getString(R.string.nail_art_tools);

        //Adding products and their quantities to map
        products.put(nailPolish_name,0);
        products.put(cuticleCare_name,0);
        products.put(manicureKit_name,0);
        products.put(nailFiles_name,0);
        products.put(nailLamp_name,0);
        products.put(handCream_name,0);
        products.put(nailArt_name,0);

        HashMap<String, Integer> defaultMap = new HashMap<>(products);

        //Listeners for adding items to map, first catching all buttons, then onClick logic

        Button nailPolish_button = findViewById(R.id.button_makaze);
        Button cuticleCare_button = findViewById(R.id.button_sampon);
        Button manicureKit_button = findViewById(R.id.button_set);
        Button nailFiles_button = findViewById(R.id.button_vikleri);
        Button nailLamp_button = findViewById(R.id.button_masinica);
        Button handCream_button = findViewById(R.id.button_ulje);
        Button nailArt_button = findViewById(R.id.button_stalak);
        Button clear_button = findViewById(R.id.obrisi_korpu);

        nailPolish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(nailPolish_name) + 1;
                products.put(nailPolish_name, value);
                UpdateCart(cart);
            }
        });
        cuticleCare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(cuticleCare_name) + 1;
                products.put(cuticleCare_name, value);
                UpdateCart(cart);
            }
        });
        manicureKit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(manicureKit_name) + 1;
                products.put(manicureKit_name, value);
                UpdateCart(cart);
            }
        });
        nailFiles_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(nailFiles_name) + 1;
                products.put(nailFiles_name, value);
                UpdateCart(cart);
            }
        });
        nailLamp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(nailLamp_name) + 1;
                products.put(nailLamp_name, value);
                UpdateCart(cart);
            }
        });
        handCream_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(handCream_name) + 1;
                products.put(handCream_name, value);
                UpdateCart(cart);
            }
        });
        nailArt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = products.get(nailArt_name) + 1;
                products.put(nailArt_name, value);
                UpdateCart(cart);
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.clear();
                products.putAll(defaultMap);
                cart.setText(getString(R.string.cart_contents));
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
                ScrollToProduct(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Button back = findViewById(R.id.nazad);
        back.setOnClickListener(new View.OnClickListener() {
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