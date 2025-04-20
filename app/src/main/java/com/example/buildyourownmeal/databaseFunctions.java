package com.example.buildyourownmeal;

import static androidx.core.app.NotificationCompat.getColor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.sql.SQLClientInfoException;

public class databaseFunctions extends SQLiteOpenHelper {

    private static final String LOG_ALERT_TAG = "database";
    private static final String DATABASE_NAME = "myDb";
    private final Context context;


    public databaseFunctions(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase myDb) {
        myDb.execSQL("create Table account(" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                " username TEXT," +
                " email TEXT UNIQUE," +
                " password TEXT," +
                " contactNumber TEXT UNIQUE," +
                " role TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int oldVersion, int newVersion) {
        myDb.execSQL("drop Table if exists account");
    }

    public Boolean insertData(String username, String email, String password, String role) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("role", role);
        long result = myDb.insert("account", null, contentValues);


        if (result == 1) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertUsername(String username) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        long result = myDb.update("account", contentValues, "username = ?", new String[]{username});

        return result != 0;
    }

    public Boolean insertEmail(String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        long result = myDb.insert("account", null, contentValues);

        return result != 0;
    }

    public Boolean insertPassword(String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        long result = myDb.insert("account", null, contentValues);

        return result != 0;
    }

    public Boolean insertContactNumber(String contactNumber) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contactNumber", contactNumber);
        long result = myDb.insert("account", null, contentValues);

        return result != 0;
    }


    public Cursor getUserInfo(String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * from account where email = ? LIMIT 1", new String[]{email});
    }

    public Boolean checkUserId(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT userId from account where userId = ? LIMIT 1", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            return true;
        } else {
            Log.d(LOG_ALERT_TAG, "No id found");
            return false;
        }
    }

    public Boolean isUserInfoValid(String email, int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor checkEmail = myDb.rawQuery("SELECT email from account where email = ? LIMIT 1", new String[]{email});
        Cursor checkUserId = myDb.rawQuery("SELECT userId from account where userId = ? LIMIT 1", new String[]{String.valueOf(userId)});

        if (checkUserId != null && checkUserId.moveToFirst()) {
            return true;
        } else if (checkEmail != null && checkEmail.moveToFirst()) {
            return true;
        } else {
            Log.d(LOG_ALERT_TAG, "No id");
            return false;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from account where email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean checkPassword(String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from account where password = ?", new String[]{password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean isPasswordValid(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;

        for (Character c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }

        return hasUppercase && hasLowercase && hasNumber;
    }

    public void cannotRetrieveData() {
        Log.d(LOG_ALERT_TAG, "Database error: Cannot get data from table");
    }

}
