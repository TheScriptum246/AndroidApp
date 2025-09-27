package com.example.projekatsalon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "nail_salon.db";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";

    public static final String USER_TABLE_NAME = "users";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSWORD = "password";

    public static final String SCHEDULE_TABLE_NAME = "appointments";
    public static final String SCHEDULE_DATE = "date";
    public static final String SCHEDULE_TIME = "time";
    public static final String SCHEDULE_TECHNICIAN = "technician";

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
        db.execSQL(query1);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
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

        if(db != null){
            cursor = db.rawQuery(query, null);
        }else{
            return false;
        }

        if(cursor.getCount() == 0){
            return true;
        }else{
            while(cursor.moveToNext()){
                if(username.equals(cursor.getString(0))){
                    return false;
                }
            }
        }
        cursor.close();
        return true;
    }

    //Check if user with given username and password exists in database
    String checkPassword(String username, int password){
        String query = "SELECT * FROM " + USER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }else{
            Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show();
            return "";
        }

        if(cursor.getCount() == 0){
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
            cursor.close();
            return "";
        }else{
            while(cursor.moveToNext()){
                if(username.equals(cursor.getString(1)) && String.valueOf(password).equals(cursor.getString(2))){
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                    return cursor.getString(0);
                }
            }
        }
        cursor.close();
        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
        return "";
    }
}