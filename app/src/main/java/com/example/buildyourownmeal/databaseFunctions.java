package com.example.buildyourownmeal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.SQLClientInfoException;

public class databaseFunctions extends SQLiteOpenHelper {

    private static final String LOG_ALERT_TAG = "database";
    private static final String DATABASE_NAME = "myDb";


    public databaseFunctions(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
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

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from account where email = ? and password = ?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


}
