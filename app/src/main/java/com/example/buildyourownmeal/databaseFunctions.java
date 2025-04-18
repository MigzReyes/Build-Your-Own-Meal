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

    public Boolean insertData(String username, String email, String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = myDb.insert("account", null, contentValues);


        if (result == 1) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUsername() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * from account where username = ? and userId = ?", null);

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


}
