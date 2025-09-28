package com.example.projekatsalon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private MyDatabaseHelper dbHelper;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView cartContents;
    private Button clearCartButton, checkoutButton, headerLogoutButton;
    private String currentUser;

    // HashMap to track shopping cart state
    private HashMap<String, Integer> shoppingCart = new HashMap<>();
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Get current user from intent
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("user_export_key");

        initializeViews();
        setupDatabase();
        setupNavigation();
        setupRecyclerView();
        setupSearchView();
        setupButtons();
        loadProducts();
        setupBackPressHandler();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void setupBackPressHandler() {
        // Modern way to handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Prevent going back to login, user must use navigation or logout
                Toast.makeText(StoreActivity.this, "Use navigation buttons to switch screens or logout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.products_recycler_view);
        searchView = findViewById(R.id.search_view);
        cartContents = findViewById(R.id.cart_contents);
        clearCartButton = findViewById(R.id.clear_cart_button);
        checkoutButton = findViewById(R.id.checkout_button);
        headerLogoutButton = findViewById(R.id.header_logout_button);
    }

    private void setupDatabase() {
        dbHelper = new MyDatabaseHelper(this);
    }

    private void setupNavigation() {
        // Simple navigation setup
        CardView storeButton = findViewById(R.id.nav_store_button);
        CardView scheduleButton = findViewById(R.id.nav_schedule_button);

        // Store button is already current screen
        storeButton.setOnClickListener(v -> {
            Toast.makeText(this, "You're already in the store!", Toast.LENGTH_SHORT).show();
        });

        scheduleButton.setOnClickListener(v -> {
            Intent intent = new Intent(StoreActivity.this, ScheduleActivity.class);
            intent.putExtra("user_export_key", currentUser);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, filteredProducts, this);
        recyclerView.setAdapter(productAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showAllProducts();
                } else {
                    filterProducts(newText);
                }
                return true;
            }
        });
    }

    private void setupButtons() {
        clearCartButton.setOnClickListener(v -> clearCart());

        checkoutButton.setOnClickListener(v -> {
            if (shoppingCart.isEmpty() || isCartEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thank you for your purchase!", Toast.LENGTH_LONG).show();
                clearCart();
            }
        });

        headerLogoutButton.setOnClickListener(v -> {
            MainActivity.logout();
            Intent intent = new Intent(StoreActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadProducts() {
        allProducts = dbHelper.getAllProducts();

        filteredProducts.clear();
        filteredProducts.addAll(allProducts);

        // Initialize shopping cart with all products
        for (Product product : allProducts) {
            shoppingCart.put(product.getName(), 0);
        }

        productAdapter.notifyDataSetChanged();
        updateCartDisplay();

        // Debug: Log product names
        for (Product product : allProducts) {
            System.out.println("Product: " + product.getName() + " - " + product.getPrice());
        }
    }

    private void filterProducts(String query) {
        filteredProducts.clear();

        if (query.isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }

        productAdapter.notifyDataSetChanged();
    }

    private void showAllProducts() {
        filteredProducts.clear();
        filteredProducts.addAll(allProducts);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddToCartClick(Product product) {
        String productName = product.getName();
        int currentQuantity = shoppingCart.getOrDefault(productName, 0);
        shoppingCart.put(productName, currentQuantity + 1);

        updateCartDisplay();

        Toast.makeText(this, product.getName() + " added to cart!",
                Toast.LENGTH_SHORT).show();
    }

    private void updateCartDisplay() {
        StringBuilder cartText = new StringBuilder();
        boolean hasItems = false;

        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            if (entry.getValue() > 0) {
                hasItems = true;
                cartText.append(entry.getKey())
                        .append(": x")
                        .append(entry.getValue())
                        .append("\n");
            }
        }

        if (hasItems) {
            cartContents.setText(cartText.toString().trim());
        } else {
            cartContents.setText(getString(R.string.cart_contents));
        }
    }

    private void clearCart() {
        for (String key : shoppingCart.keySet()) {
            shoppingCart.put(key, 0);
        }
        updateCartDisplay();
        Toast.makeText(this, "Cart cleared!", Toast.LENGTH_SHORT).show();
    }

    private boolean isCartEmpty() {
        for (Integer quantity : shoppingCart.values()) {
            if (quantity > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}