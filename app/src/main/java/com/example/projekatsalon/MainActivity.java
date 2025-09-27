package com.example.projekatsalon;


import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.projekatsalon.R;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static boolean logged;
    private static String logged_username;

    private static final String USER_EXPORT_KEY = "user_export_key";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.schedule){
                if(!logged){
                    Toast.makeText(this, "Niste logovani", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                    intent.putExtra(USER_EXPORT_KEY, logged_username);
                    startActivity(intent);
                }
                return true;
            }
            if(item.getItemId() == R.id.store){
                if(!logged){
                    Toast.makeText(this, "Niste logovani", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, StoreActivity.class);
                    intent.putExtra(USER_EXPORT_KEY, logged_username);
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });

        //Hvatanje username i password polja
        EditText username_field = findViewById(R.id.username_id);
        EditText password_field = findViewById(R.id.password_id);

        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(TextUtils.isDigitsOnly(password_field.getText().toString()))){
                    Toast.makeText(MainActivity.this, "Pass moraju biti brojevi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(logged){
                    Toast.makeText(MainActivity.this, "Vec ste logovani", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                        myDB.SignUpUser(username_field.getText().toString(), Integer.parseInt(password_field.getText().toString()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(TextUtils.isDigitsOnly(password_field.getText().toString()))){
                    Toast.makeText(MainActivity.this, "Pass moraju biti brojevi", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                    logged_username = myDB.LogInUser(username_field.getText().toString(), Integer.parseInt(password_field.getText().toString()));
                    if (!(logged_username.equals(""))){
                    logged = true;
                }
            }catch (NullPointerException e){e.printStackTrace();}
            }
        });
    }
}
