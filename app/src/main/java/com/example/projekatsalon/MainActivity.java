package com.example.projekatsalon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static boolean isLoggedIn = false;
    private static String loggedUsername = "";

    private EditText usernameInput, passwordInput;
    private Button loginButton, signupButton;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this);

        initializeViews();

        setupButtonListeners();

        if (isLoggedIn && !loggedUsername.isEmpty()) {
            redirectToStore();
        }
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
    }

    private void setupButtonListeners() {
        loginButton.setOnClickListener(v -> handleLogin());
        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void handleLogin() {
        String username = getInputText(usernameInput);
        String password = getInputText(passwordInput);

        if (!validateInputs(username, password)) {
            return;
        }

        try {
            int passwordInt = Integer.parseInt(password);
            String loginResult = dbHelper.LogInUser(username, passwordInt);

            if (!loginResult.isEmpty()) {
                isLoggedIn = true;
                loggedUsername = username;

                Toast.makeText(this, "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show();

                loginButton.postDelayed(this::redirectToStore, 1000);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Password must be numbers only", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignup() {
        String username = getInputText(usernameInput);
        String password = getInputText(passwordInput);

        if (!validateInputs(username, password)) {
            return;
        }

        if (isLoggedIn) {
            Toast.makeText(this, "You are already logged in as " + loggedUsername, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int passwordInt = Integer.parseInt(password);
            dbHelper.SignUpUser(username, passwordInt);

            clearInputFields();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Password must be numbers only", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return false;
        }

        if (!TextUtils.isDigitsOnly(password)) {
            passwordInput.setError("Password must contain only numbers");
            passwordInput.requestFocus();
            return false;
        }

        if (password.length() < 4) {
            passwordInput.setError("Password must be at least 4 digits");
            passwordInput.requestFocus();
            return false;
        }

        return true;
    }

    private String getInputText(EditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private void clearInputFields() {
        if (usernameInput != null) usernameInput.setText("");
        if (passwordInput != null) passwordInput.setText("");
    }

    private void redirectToStore() {
        Intent intent = new Intent(MainActivity.this, StoreActivity.class);
        intent.putExtra("user_export_key", loggedUsername);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public static void logout() {
        isLoggedIn = false;
        loggedUsername = "";
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isLoggedIn) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}