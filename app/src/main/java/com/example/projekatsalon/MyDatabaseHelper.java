package com.example.projekatsalon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "nail_salon.db";
    public static final int DATABASE_VERSION = 2; // Increased version for new table
    public static final String COLUMN_ID = "_id";

    public static final String USER_TABLE_NAME = "users";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSWORD = "password";

    public static final String SCHEDULE_TABLE_NAME = "appointments";
    public static final String SCHEDULE_DATE = "date";
    public static final String SCHEDULE_TIME = "time";
    public static final String SCHEDULE_TECHNICIAN = "technician";

    // New Products table
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String PRODUCT_IMAGE_NAME = "product_image_name";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 =
                "CREATE TABLE " + USER_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        USER_USERNAME + " TEXT," +
                        USER_PASSWORD + " INTEGER);";
        String query2 =
                "CREATE TABLE " + SCHEDULE_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        SCHEDULE_DATE + " TEXT," +
                        SCHEDULE_TIME + " TEXT," +
                        SCHEDULE_TECHNICIAN + " TEXT);";
        String query3 =
                "CREATE TABLE " + PRODUCTS_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PRODUCT_NAME + " TEXT UNIQUE," +
                        PRODUCT_PRICE + " TEXT," +
                        PRODUCT_DESCRIPTION + " TEXT," +
                        PRODUCT_IMAGE_NAME + " TEXT);";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);

        // Insert initial product data
        insertInitialProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
        onCreate(db);
    }

    private void insertInitialProducts(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        // Product 1
        cv.put(PRODUCT_NAME, "Premium Nail Polish Collection");
        cv.put(PRODUCT_PRICE, "$45.99");
        cv.put(PRODUCT_DESCRIPTION, "Professional quality nail polish set with 12 vibrant colors");
        cv.put(PRODUCT_IMAGE_NAME, "makaze");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
        cv.clear();

        // Product 2
        cv.put(PRODUCT_NAME, "Cuticle Care Essential Kit");
        cv.put(PRODUCT_PRICE, "$28.75");
        cv.put(PRODUCT_DESCRIPTION, "Complete cuticle care kit with oils and tools");
        cv.put(PRODUCT_IMAGE_NAME, "sampon_za_kosu");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
        cv.clear();

        // Product 3
        cv.put(PRODUCT_NAME, "Complete Manicure Starter Kit");
        cv.put(PRODUCT_PRICE, "$67.99");
        cv.put(PRODUCT_DESCRIPTION, "Everything you need to start your manicure journey");
        cv.put(PRODUCT_IMAGE_NAME, "tri_plus_jedan_gratis_set");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
        cv.clear();

        // Product 4
        cv.put(PRODUCT_NAME, "Professional Nail Files Set");
        cv.put(PRODUCT_PRICE, "$24.99");
        cv.put(PRODUCT_DESCRIPTION, "High-quality nail files for perfect shaping");
        cv.put(PRODUCT_IMAGE_NAME, "vikleri");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
        cv.clear();

        // Product 5
        cv.put(PRODUCT_NAME, "UV/LED Nail Lamp");
        cv.put(PRODUCT_PRICE, "$89.99");
        cv.put(PRODUCT_DESCRIPTION, "Professional grade UV/LED nail curing lamp");
        cv.put(PRODUCT_IMAGE_NAME, "masinica_za_sisanje");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
        cv.clear();

        // Product 6
        cv.put(PRODUCT_NAME, "Luxury Hand Cream");
        cv.put(PRODUCT_PRICE, "$32.50");
        cv.put(PRODUCT_DESCRIPTION, "Moisturizing hand cream with natural ingredients");
        cv.put(PRODUCT_IMAGE_NAME, "ulje_za_kosu");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
        cv.clear();

        // Product 7
        cv.put(PRODUCT_NAME, "Nail Art Design Tools");
        cv.put(PRODUCT_PRICE, "$156.99");
        cv.put(PRODUCT_DESCRIPTION, "Professional nail art tools for creative designs");
        cv.put(PRODUCT_IMAGE_NAME, "stalak_za_brijac");
        db.insert(PRODUCTS_TABLE_NAME, null, cv);
    }

    // Method to get all products from database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + PRODUCTS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0), // id
                        cursor.getString(1), // name
                        cursor.getString(2), // price
                        cursor.getString(3), // description
                        cursor.getString(4)  // image name
                );
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return products;
    }

    // Method to search products by name
    public List<Product> searchProducts(String searchQuery) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + PRODUCTS_TABLE_NAME +
                " WHERE " + PRODUCT_NAME + " LIKE ? OR " + PRODUCT_DESCRIPTION + " LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();

        String searchPattern = "%" + searchQuery + "%";
        Cursor cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return products;
    }

    //Check user data for Login
    String LogInUser(String username, int password){
        if((checkPassword(username, password).equals(""))){
            return "";
        }
        return checkPassword(username, password);
    }

    //Check user data for SignUp and if user already exists
    void SignUpUser(String username, int password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_USERNAME, username);
        cv.put(USER_PASSWORD, password);

        long result = -1;
        if(checkUsers(username)) {
            result = db.insert(USER_TABLE_NAME, null, cv);
        }
        if(result == -1){
            Toast.makeText(context,"Registration failed",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context,"Account created successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    //Check if user with given username already exists in database (cursor object for iteration)
    boolean checkUsers(String username){
        String query = "SELECT " + USER_USERNAME + " FROM " + USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            if(db != null){
                cursor = db.rawQuery(query, null);
            }else{
                return false;
            }

            if(cursor.getCount() == 0){
                return true;
            }else{
                while(cursor.moveToNext()){
                    String dbUsername = cursor.getString(0);
                    if(username.equals(dbUsername)){
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }

    //Check if user with given username and password exists in database
    String checkPassword(String username, int password){
        String query = "SELECT * FROM " + USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            if(db != null){
                cursor = db.rawQuery(query, null);
            }else{
                Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show();
                return "";
            }

            if(cursor.getCount() == 0){
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
                return "";
            }else{
                while(cursor.moveToNext()){
                    String dbUsername = cursor.getString(1);
                    String dbPassword = cursor.getString(2);
                    String userId = cursor.getString(0);

                    if(username.equals(dbUsername) && String.valueOf(password).equals(dbPassword)){
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                        return userId;
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
        return "";
    }
}