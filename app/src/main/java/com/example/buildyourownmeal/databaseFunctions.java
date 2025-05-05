package com.example.buildyourownmeal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class databaseFunctions extends SQLiteOpenHelper {

    //HASHMAPS
    private static HashMap<String, Integer> addonPrice = new HashMap<String, Integer>();
    private static HashMap<String, String> addonName = new HashMap<String, String>();
    private static final String LOG_ALERT_TAG = "database";
    private static final String DATABASE_NAME = "myDb";
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_USER_ORDER = "user_order";
    private static final String TABLE_ORDER_ADDON = "order_addon";
    private static final String TABLE_ADMIN_MENU = "admin_menu";
    private static final String TABLE_RICE = "rice";
    private static final String TABLE_MAIN_DISH = "main_dish";
    private static final String TABLE_SIDE = "side_dish";
    private static final String TABLE_SAUCE = "sauce";
    private static final String TABLE_DESSERT = "dessert";
    private static final String TABLE_DRINK = "drink";


    public databaseFunctions(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        insertAddonHashPrice();
        insertAddonHashName();
    }

    @Override
    public void onCreate(SQLiteDatabase myDb) {
        myDb.execSQL("create Table " +  TABLE_ACCOUNT + " (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "contactNumber TEXT UNIQUE, " +
                "ban TEXT, " +
                "role TEXT, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_USER_ORDER + " (" +
                "userOrderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderAddonId INTEGER, " +
                "userId INTEGER, " +
                "mealImg TEXT, " +
                "mealType TEXT, " +
                "orderTotalPrice INTEGER, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_ORDER_ADDON + " (" +
                "orderAddonId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "addon TEXT, " +
                "quantity INTEGER, " +
                "price INTEGER, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_RICE + " (riceId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int)");

        myDb.execSQL("create Table " + TABLE_MAIN_DISH + " (mainDishId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int)");

        myDb.execSQL("create Table " + TABLE_SIDE + " (sideDishId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int)");

        myDb.execSQL("create Table " + TABLE_SAUCE + " (sauceId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int)");

        myDb.execSQL("create Table " + TABLE_DESSERT + " (dessertId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int)");

        myDb.execSQL("create Table " + TABLE_DRINK + " (drinkId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int oldVersion, int newVersion) {
        myDb.execSQL("drop Table if exists " + TABLE_ACCOUNT);
        myDb.execSQL("drop Table if exists " + TABLE_USER_ORDER);
        myDb.execSQL("drop Table if exists " + TABLE_ORDER_ADDON);
        myDb.execSQL("drop Table if exists " + TABLE_RICE);
        myDb.execSQL("drop Table if exists " + TABLE_MAIN_DISH);
        myDb.execSQL("drop Table if exists " + TABLE_SIDE);
        myDb.execSQL("drop Table if exists " + TABLE_SAUCE);
        myDb.execSQL("drop Table if exists " + TABLE_DESSERT);
        myDb.execSQL("drop Table if exists " + TABLE_DRINK );
    }

    //DELETE QUERY
    public Boolean deleteAccount(String tableName, int id) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(tableName, "userId = ?", new String[]{String.valueOf(id)});

        return result != 0;
    }


    //INSERT QUERY
    public Boolean insertRice(String addonName, int price) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", addonName);
        contentValues.put("price", price);
        long result = myDb.insert(TABLE_RICE, null, contentValues);

        if (result == 0) {
            Log.d(LOG_ALERT_TAG, "Insert data failed: Addon already exist");
            myDb.close();
            return false;
        } else {
            myDb.close();
            return true;
        }
    }

    public Boolean insertAdminData() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", "admin");
        contentValues.put("email", "admin@gmail.com");
        contentValues.put("password", "AdminSamplePass");
        contentValues.put("role", "admin");
        long result = myDb.insert(TABLE_ACCOUNT, null, contentValues);

        return result != -1;
    }

    public Boolean insertUserData(String username, String email, String password, String ban, String role) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("ban", ban);
        contentValues.put("role", role);
        long result = myDb.insert(TABLE_ACCOUNT, null, contentValues);


        if (result == -1) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
            myDb.close();
            return false;
        } else {
            myDb.close();
            return true;
        }
    }

    public Boolean insertOrderData(int orderAddonId, int userId, Bitmap mealImg, String mealType, int orderTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("orderAddonId", orderAddonId);
        contentValues.put("userId", userId);
        contentValues.put("mealImg", byteArray);
        contentValues.put("mealType", mealType);
        contentValues.put("orderTotalPrice", orderTotalPrice);
        long result = myDb.insert(TABLE_USER_ORDER, null, contentValues);

        if (result == -1) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
            myDb.close();
            return false;
        } else {
            myDb.close();
            return true;
        }
    }

    public Boolean insertAddonData(int userId, String addon, int quantity, int price) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("addon", addon);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        long result = myDb.insert(TABLE_ORDER_ADDON, null, contentValues);

        if (result == -1) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
            myDb.close();
            return false;
        } else {
            myDb.close();
            return true;
        }
    }

    public void insertAddonHashPrice() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_RICE, null);

        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    int getAddonPrice = cursor.getColumnIndexOrThrow("price");
                    addonPrice.put(cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getInt(getAddonPrice));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDb.close();
    }

    private void insertAddonHashName() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_RICE, null);

        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    addonName.put(cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getString(cursor.getColumnIndexOrThrow("name")));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDb.close();
    }

    public Boolean insertAdminUserData(String username, String email, String contactNumber, String password, String ban, String role) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("contactNumber", contactNumber);
        contentValues.put("password", password);
        contentValues.put("ban", ban);
        contentValues.put("role", role);
        long result = myDb.insert(TABLE_ACCOUNT, null, contentValues);


        if (result == -1) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
            myDb.close();
            return false;
        } else {
            myDb.close();
            return true;
        }
    }


    //UPDATE QUERY

    public Boolean updateUserInfo(int userId, String username, String email, String contactNum, String password, String ban, String role) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("contactNumber", contactNum);
        contentValues.put("password", password);
        contentValues.put("ban", ban);
        contentValues.put("role", role);
        long result = myDb.update(TABLE_ACCOUNT, contentValues, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }
    public Boolean updateUsername(int userId, String username) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        long result = myDb.update(TABLE_ACCOUNT, contentValues, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }

    public Boolean updateEmail(int userId , String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        long result = myDb.update(TABLE_ACCOUNT, contentValues, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }

    public Boolean updatePassword(int userId, String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        long result = myDb.update(TABLE_ACCOUNT, contentValues, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }

    public Boolean updateContactNumber(int userId, String contactNumber) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contactNumber", contactNumber);
        long result = myDb.update(TABLE_ACCOUNT, contentValues, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }


    //GET QUERY
    public Cursor getUserOrder(int userId) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_USER_ORDER + " WHERE userId = ?", new String[]{String.valueOf(userId)});
    }

    public static int getAddonPrice(String addonName) {
        Integer price = addonPrice.get(addonName);
        return price != null ? price : 10;
    }

    public static String getAddonName(String name) {
        String nameAddon = addonName.get(name);
        return nameAddon != null ? nameAddon : "no addon";
    }

    public Cursor getAddonData(int userId) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ORDER_ADDON + " WHERE userId = ?", new String[]{String.valueOf(userId)});
    }

    public Bitmap getImg(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT mealImg FROM " + TABLE_USER_ORDER + " WHERE userId = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            byte[] byteArray = cursor.getBlob(0);
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        cursor.close();
        return null;
    }

    public Cursor getUserInfo(String email) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE email = ? LIMIT 1", new String[]{email});
    }

    public Cursor getAllUser() {
      SQLiteDatabase myDb = this.getWritableDatabase();
      return myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT, null);
    }



    //QUERY VALIDATION
    public Boolean checkUserId(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT userId FROM " + TABLE_ACCOUNT + " WHERE userId = ? LIMIT 1", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            return true;
        } else {
            Log.d(LOG_ALERT_TAG, "No id found");
            return false;
        }
    }

    public Boolean isUserInfoValid(String email, int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor checkEmail = myDb.rawQuery("SELECT email FROM " + TABLE_ACCOUNT + " WHERE email = ? LIMIT 1", new String[]{email});
        Cursor checkUserId = myDb.rawQuery("SELECT userId FROM " + TABLE_ACCOUNT + " WHERE userId = ? LIMIT 1", new String[]{String.valueOf(userId)});

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
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean checkEmailId(String email, int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE email = ? and userId = ? LIMIT 1", new String[]{email, String.valueOf(userId)});

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
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE password = ?", new String[]{password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean checkContactNumber(String contactNumber) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE contactNumber = ?", new String[]{contactNumber});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean checkContactNumberId(String contactNumber, int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE contactNumber = ? and userId != ? LIMIT 1", new String[]{contactNumber, String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            cursor.close();
            Log.d("Cursor Error", String.valueOf(cursor.getCount()));
            return true;
        } else {
            cursor.close();
            Log.d("Cursor Error", String.valueOf(cursor.getCount()));
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
        Log.d(LOG_ALERT_TAG, "Database error: Cannot get data FROM table");
    }

}
