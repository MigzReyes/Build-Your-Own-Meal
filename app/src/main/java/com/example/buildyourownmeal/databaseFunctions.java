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
    public static final String LOG_ALERT_TAG = "database";
    private static final String DATABASE_NAME = "myDb";
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_USER_ORDER = "user_order";
    private static final String TABLE_ORDER_ADDON = "order_addon";
    private static final String TABLE_ADMIN_ORDER_ADDON = "admin_order_addon";
    private static final String TABLE_ADMIN_USER_ORDER = "admin_user_order";
    private static final String TABLE_ADMIN_ORDERS = "admin_orders";
    private static final String TABLE_ADMIN_MEALS = "admin_meals";
    private static final String TABLE_USER_CHECKOUT = "user_checkout";
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
                "orderAddonId String, " +
                "userId INTEGER, " +
                "mealImg TEXT, " +
                "mealType TEXT, " +
                "mealQuantity INTEGER, " +
                "orderTotalPrice INTEGER, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_ORDER_ADDON + " (" +
                "orderAddonId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER," +
                "addonGroupId TEXT, " +
                "addon TEXT, " +
                "quantity INTEGER, " +
                "price INTEGER, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_USER_CHECKOUT + " (" +
                "userCheckoutId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "orderGroupId TEXT, " +
                "contactNumber TEXT, " +
                "pickUp TEXT, " +
                "paymentMethod TEXT, " +
                "checkoutTotalPrice INTEGER, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_ADMIN_ORDERS + " (" +
                "adminOrderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "orderGroupId TEXT, " +
                "contactNumber TEXT, " +
                "pickUp TEXT, " +
                "paymentMethod TEXT, " +
                "totalPrice INTEGER, " +
                "status TEXT, " +
                "orderedDate TEXT)");

        myDb.execSQL("create Table " + TABLE_ADMIN_USER_ORDER + " (" +
                "userOrderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "orderAddonId TEXT, " +
                "orderGroupId TEXT, " +
                "mealImg BLOB, " +
                "mealType TEXT, " +
                "mealQuantity INTEGER, " +
                "orderTotalPrice INTEGER, " +
                "orderedDate String)");

        myDb.execSQL("create Table " + TABLE_ADMIN_ORDER_ADDON + " (" +
                "adminOrderAddonId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "addonGroupId TEXT," +
                "orderGroupId TEXT, " +
                "addon TEXT, " +
                "quantity INTEGER, " +
                "price INTEGER, " +
                "orderedDate TEXT)");

        myDb.execSQL("create Table " + TABLE_ADMIN_MEALS + " (" +
                "adminMealId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "adminAddonId TEXT, " +
                "mealName TEXT, " +
                "mealDescription TEXT, " +
                "mealImg BLOB," +
                "mealImgUri TEXT,  " +
                "mealTotalPrice INTEGER, " +
                "creationDate DATETIME DEFAULT CURRENT_TIMESTAMP)");

        myDb.execSQL("create Table " + TABLE_RICE + " (riceId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int, " +
                "category TEXT, " +
                "imgUri TEXT)");

        myDb.execSQL("create Table " + TABLE_MAIN_DISH + " (mainDishId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int, " +
                "category TEXT, " +
                "imgUri TEXT)");

        myDb.execSQL("create Table " + TABLE_SIDE + " (sideDishId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int, " +
                "category TEXT, " +
                "imgUri TEXT)");

        myDb.execSQL("create Table " + TABLE_SAUCE + " (sauceId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int, " +
                "category TEXT, " +
                "imgUri TEXT)");

        myDb.execSQL("create Table " + TABLE_DESSERT + " (dessertId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int, " +
                "category TEXT, " +
                "imgUri TEXT)");

        myDb.execSQL("create Table " + TABLE_DRINK + " (drinkId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "img BLOB, " +
                "price int, " +
                "category TEXT, " +
                "imgUri TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int oldVersion, int newVersion) {
        myDb.execSQL("drop Table if exists " + TABLE_ACCOUNT);
        myDb.execSQL("drop Table if exists " + TABLE_USER_ORDER);
        myDb.execSQL("drop Table if exists " + TABLE_ORDER_ADDON);
        myDb.execSQL("drop Table if exists " + TABLE_USER_CHECKOUT);
        myDb.execSQL("drop Table if exists " + TABLE_RICE);
        myDb.execSQL("drop Table if exists " + TABLE_MAIN_DISH);
        myDb.execSQL("drop Table if exists " + TABLE_SIDE);
        myDb.execSQL("drop Table if exists " + TABLE_SAUCE);
        myDb.execSQL("drop Table if exists " + TABLE_DESSERT);
        myDb.execSQL("drop Table if exists " + TABLE_DRINK );
    }

    //DELETE QUERY
    public Boolean deleteAdminMeal(int adminMealId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_ADMIN_MEALS, "adminMealId = ?", new String[]{String.valueOf(adminMealId)});

        return result != 0;
    }

    public Boolean deleteAdminOrder(String orderGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_ADMIN_USER_ORDER, "orderGroupId = ?", new String[]{orderGroupId});
        long result1 = myDb.delete(TABLE_ADMIN_ORDER_ADDON, "orderGroupId = ?", new String[]{orderGroupId});
        long result3 = myDb.delete(TABLE_ADMIN_ORDERS, "orderGroupId = ?", new String[]{orderGroupId});
        long result4 = myDb.delete(TABLE_USER_CHECKOUT, "orderGroupId = ?", new String[]{orderGroupId});

        return  result != 0 && result1 != 0 && result3 != 0 && result4 != 0;
    }

    public Boolean deleteAdminOrderAddon(String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_ADMIN_ORDER_ADDON, "addonGroupId = ?", new String[]{addonGroupId});

        return result != 0;
    }

    public Boolean deleteAdminUserOrder(String orderGroupId, String orderAddonId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_ADMIN_USER_ORDER, "orderGroupId = ? AND orderAddonId = ?", new String[]{orderGroupId, orderAddonId});

        return result != 0;
    }

    public Boolean deleteOrderUser(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_USER_ORDER, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }

    public Boolean deleteOrderAddonWithUserId(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_ORDER_ADDON, "userId = ?", new String[]{String.valueOf(userId)});

        return result != 0;
    }

    public Boolean deleteOrderAddon(String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_ORDER_ADDON, "addonGroupId = ?", new String[]{addonGroupId});

        return result != 0;
    }

    public Boolean deleteUserOrder(int userOrderId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(TABLE_USER_ORDER, "userOrderId = ?", new String[]{String.valueOf(userOrderId)});

        return result != 0;
    }

    public Boolean deleteAddon(String addonTable, String addonIdColName, int addonId, String category) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(addonTable, addonIdColName + " = ? AND category = ?", new String[]{String.valueOf(addonId), category});

        return result != 0;
    }

    public Boolean deleteAccount(String tableName, int id) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        long result = myDb.delete(tableName, "userId = ?", new String[]{String.valueOf(id)});

        return result != 0;
    }


    //INSERT QUERY
    public Boolean insertAdminMeal(String adminAddonId, String mealName, String mealDescription, Bitmap mealImg, String mealImgUri, int mealTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("adminAddonId", adminAddonId);
        contentValues.put("mealName", mealName);
        contentValues.put("mealDescription", mealDescription);
        contentValues.put("mealImg", byteArray);
        contentValues.put("mealImgUri", mealImgUri);
        contentValues.put("mealTotalPrice", mealTotalPrice);
        long result = myDb.insert(TABLE_ADMIN_MEALS, null, contentValues);

        return result != -1;
    }

    public void insertAdminUserOrder(String orderAddonId, String orderGroupId, int userId, Bitmap mealImg, String mealType, int mealQuantity, int orderTotalPrice, String orderDate) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("orderAddonId", orderAddonId);
        contentValues.put("orderGroupId", orderGroupId);
        contentValues.put("userId", userId);
        contentValues.put("mealImg", byteArray);
        contentValues.put("mealType", mealType);
        contentValues.put("mealQuantity", mealQuantity);
        contentValues.put("orderTotalPrice", orderTotalPrice);
        contentValues.put("orderedDate", orderDate);
        myDb.insert(TABLE_ADMIN_USER_ORDER, null, contentValues);
    }

    public void insertAdminOrderAddon(int userId, String addonGroupId, String orderGroupId, String addon, int quantity, int price, String orderedDate) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("addonGroupId", addonGroupId);
        contentValues.put("orderGroupId", orderGroupId);
        contentValues.put("addon", addon);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        contentValues.put("orderedDate", orderedDate);
        myDb.insert(TABLE_ADMIN_ORDER_ADDON, null, contentValues);
    }

    public Boolean insertAdminOrders(int userId, String orderGroupId, String contactNumber, String pickUp, String paymentMethod, int totalPrice, String status, String orderedDate) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("orderGroupId", orderGroupId);
        contentValues.put("contactNumber", contactNumber);
        contentValues.put("pickUp", pickUp);
        contentValues.put("paymentMethod", paymentMethod);
        contentValues.put("totalPrice", totalPrice);
        contentValues.put("status", status);
        contentValues.put("orderedDate", orderedDate);
        long result = myDb.insert(TABLE_ADMIN_ORDERS, null, contentValues);

        return result != -1;
    }

    public Boolean insertUserCheckout(int userId, String orderGroupId, String contactNumber, String pickUp, String paymentMethod, int checkoutTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("orderGroupId", orderGroupId);
        contentValues.put("contactNumber", contactNumber);
        contentValues.put("pickUp", pickUp);
        contentValues.put("paymentMethod", paymentMethod);
        contentValues.put("checkoutTotalPrice", checkoutTotalPrice);
        long result = myDb.insert(TABLE_USER_CHECKOUT, null, contentValues);

        return result != -1;
    }

    public Boolean insertAdminAddonData(String addonTable, Bitmap addonImg, String addonName, int addonPrice, String imgUri, String category) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        addonImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("img", byteArray);
        contentValues.put("name", addonName);
        contentValues.put("price", addonPrice);
        contentValues.put("category", category);
        contentValues.put("imgUri", imgUri);
        long result = myDb.insert(addonTable, null, contentValues);

        if (result == 0) {
            Log.d(LOG_ALERT_TAG, "Insert data failed");
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
        contentValues.put("ban", "false");
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

    public Boolean insertOrderData(String orderAddonId, int userId, Bitmap mealImg, String mealType, int mealQuantity, int orderTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("orderAddonId", orderAddonId);
        contentValues.put("userId", userId);
        contentValues.put("mealImg", byteArray);
        contentValues.put("mealType", mealType);
        contentValues.put("mealQuantity", mealQuantity);
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

    public Boolean insertOrderAddonData(int userId, String addonGroupId, String addon, int quantity, int price) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("addonGroupId", addonGroupId);
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
    public boolean updateAdminMeal(String addonGroupId, String addonName, String addonDescription, Bitmap mealImg, String mealImgUri, int mealTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mealName", addonName);
        contentValues.put("mealDescription", addonDescription);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("mealImg", byteArray);
        contentValues.put("mealImgUri", mealImgUri);
        contentValues.put("mealTotalPrice", mealTotalPrice);
        long result = myDb.update(TABLE_ADMIN_MEALS, contentValues, "adminAddonId = ?", new String[]{addonGroupId});

        return result != 0;
    }

    public Boolean updateAdminOrderStatus(int userId, String orderGroupId, String status) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        long result = myDb.update(TABLE_ADMIN_ORDERS, contentValues, "userId = ? AND orderGroupId = ?", new String[]{String.valueOf(userId), orderGroupId});

        return result != 0;
    }

    public Boolean updateAdminOrderTotalPrice(String orderGroupId, int newTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("totalPrice", newTotalPrice);
        long result = myDb.update(TABLE_ADMIN_ORDERS, contentValues, "orderGroupId = ?", new String[]{orderGroupId});

        return result != 0;
    }

    public Boolean updateCartItem(int userOrderId, int quantity, int totalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mealQuantity", quantity);
        contentValues.put("orderTotalPrice", totalPrice);
        int rows = myDb.update(TABLE_USER_ORDER, contentValues, "userOrderId = ?", new String[]{String.valueOf(userOrderId)});
        return rows != 0;
    }


    public Boolean updateOrderAddon(int userId, String addonGroupId, String addon, int quantity, int price) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("addonGroupId", addonGroupId);
        contentValues.put("addon", addon);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        long result = myDb.update(TABLE_ORDER_ADDON, contentValues, "userId = ? AND addonGroupId = ? AND addon = ?", new String[]{String.valueOf(userId), addonGroupId, addon});

        if (result == 0) {
            long insertNewAddon = myDb.insert(TABLE_ORDER_ADDON, null, contentValues);
            myDb.close();
            return insertNewAddon != -1;
        } else {
            myDb.close();
            return true;
        }
    }
    public Boolean updateUserOrder(String orderAddonId, int userId, Bitmap mealImg, String mealType, int mealQuantity, int orderTotalPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("orderAddonId", orderAddonId);
        contentValues.put("userId", userId);
        contentValues.put("mealImg", byteArray);
        contentValues.put("mealType", mealType);
        contentValues.put("mealQuantity", mealQuantity);
        contentValues.put("orderTotalPrice", orderTotalPrice);
        long result = myDb.update(TABLE_USER_ORDER, contentValues, "userId = ? AND orderAddonId = ?", new String[]{String.valueOf(userId), orderAddonId});

        if (result == -1) {
            Log.d(LOG_ALERT_TAG, "Update data failed");
            myDb.close();
            return false;
        } else {
            myDb.close();
            return true;
        }
    }

    public Boolean updateAddon(String addonTable, String addonIdColName, int addonId, String category, String addonName, int addonPrice, Bitmap addonImg) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        addonImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        contentValues.put("name", addonName);
        contentValues.put("price", addonPrice);
        contentValues.put("img", byteArray);
        long result = myDb.update(addonTable, contentValues,  addonIdColName + " = ? AND category = ?", new String[]{String.valueOf(addonId), category});

        return result != 0;
    }

    public Boolean updateAddonNoImg(String addonTable, String addonIdColName , int addonId, String category, String addonName, int addonPrice) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", addonName);
        contentValues.put("price", addonPrice);
        long result = myDb.update(addonTable, contentValues,   addonIdColName + " = ? AND category = ?", new String[]{String.valueOf(addonId), category});

        return result != 0;
    }

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
    public Cursor getOrderAddon(String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ORDER_ADDON + " WHERE addonGroupId = ?", new String[]{addonGroupId});
    }

    public Cursor getAdminMeal() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ADMIN_MEALS, null);
    }

    public Cursor getAdminOrderStatus(int userId, String orderGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT status FROM " + TABLE_ADMIN_ORDERS + " WHERE userId = ? AND orderGroupId = ?", new String[]{String.valueOf(userId), orderGroupId});
    }

    public Cursor getAdminUserOrderAddon(int userId, String orderAddonId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ADMIN_ORDER_ADDON + " WHERE userId = ? AND addonGroupId = ?", new String[]{String.valueOf(userId), orderAddonId});
    }

    public Cursor getAdminUserOrder(String orderGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ADMIN_USER_ORDER + " WHERE orderGroupId = ?", new String[]{orderGroupId});
    }

    public Cursor getAdminOrder() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ADMIN_ORDERS, null);
    }

    public Cursor getOrderedDate(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT creationDate FROM " + TABLE_USER_CHECKOUT + " WHERE userId = ?", new String[]{String.valueOf(userId)});
    }

    public Cursor getOrderAddonWithQuantity(String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT addon, quantity FROM " + TABLE_ORDER_ADDON + " WHERE addonGroupId = ?", new String[]{addonGroupId});
    }

    public Cursor getAddonTable(String addonTable, String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT name FROM " + addonTable + " WHERE addonGroupId = ?", new String[]{addonGroupId});
    }

    public Cursor getOrderAddonName(String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT addon FROM " + TABLE_ORDER_ADDON + " WHERE addonGroupId = ?", new String[]{addonGroupId});
    }

    public Cursor getOrderAddonQuantity(String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT quantity FROM " + TABLE_ORDER_ADDON + " WHERE addonGroupId = ?", new String[]{addonGroupId});
    }

    public Cursor getAddonTable(String addonTable) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + addonTable, null);
    }

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

    public Bitmap getAddonImg(String table) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT img FROM " + table, null);

        if (cursor.moveToFirst()) {
            byte[] byteArray = cursor.getBlob(0);
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        cursor.close();
        return null;
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

    public Cursor adminGetUserInfo(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE userId = ?", new String[]{String.valueOf(userId)});
    }

    public Cursor getAllUser() {
      SQLiteDatabase myDb = this.getWritableDatabase();
      return myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT, null);
    }


    //QUERY VALIDATION
    public Boolean checkIfUserHadOrdered(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT userId FROM " + TABLE_ADMIN_ORDERS + " WHERE userId = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            Log.d(LOG_ALERT_TAG, "user had ordered");
            return true;
        } else {
            cursor.close();
            Log.d(LOG_ALERT_TAG, "user had not ordered");
            return false;
        }
    }

    public Boolean checkAddonGroup(int userId, String addonGroupId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT addonGroupId FROM " + TABLE_ORDER_ADDON + " WHERE userId = ? AND addonGroupId = ?", new String[]{String.valueOf(userId), addonGroupId});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            myDb.close();
            Log.d(LOG_ALERT_TAG, "Addon group exists");
            return true;
        } else {
            cursor.close();
            myDb.close();
            Log.d(LOG_ALERT_TAG, "Addon group does not exists");
            return false;
        }
    }

    public Boolean checkAddonTable(String addonTable, String addonName) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT name FROM " + addonTable + " WHERE name = ?", new String[]{addonName});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            myDb.close();
            Log.d(LOG_ALERT_TAG, "Addon exists in the table");
            return true;
        } else {
            cursor.close();
            myDb.close();
            Log.d(LOG_ALERT_TAG, "Addon does not exists in the table");
            return false;
        }
    }
    public Boolean checkUserBan(String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT ban FROM " + TABLE_ACCOUNT + " WHERE email = ? LIMIT 1", new String[]{email});
        boolean isUserBanned = false;

        if (cursor != null && cursor.moveToFirst()) {
            String checkUserBan = cursor.getString(cursor.getColumnIndexOrThrow("ban"));
            if (checkUserBan.equals("true")) {
                cursor.close();
                myDb.close();
                Log.d(LOG_ALERT_TAG, "User is banned");
                isUserBanned = true;
            } else if (checkUserBan.equals("false")){
                cursor.close();
                myDb.close();
                Log.d(LOG_ALERT_TAG, "Not banned");
                isUserBanned = false;
            }

        } else {
            cursor.close();
            myDb.close();
            Log.d(LOG_ALERT_TAG, "No data found");
        }
        return isUserBanned;
    }


    public Boolean checkUserId(int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT userId FROM " + TABLE_ACCOUNT + " WHERE userId = ? LIMIT 1", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            myDb.close();
            return true;
        } else {
            cursor.close();
            myDb.close();
            Log.d(LOG_ALERT_TAG, "No id found");
            return false;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            myDb.close();
            return true;
        } else {
            cursor.close();
            myDb.close();
            return false;
        }
    }

    public Boolean checkEmailId(String email, int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE email = ? and userId != ? LIMIT 1", new String[]{email, String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            cursor.close();
            myDb.close();
            return true;
        } else {
            cursor.close();
            myDb.close();
            return false;
        }
    }

    public Boolean checkPassword(String password) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE password = ?", new String[]{password});

        if (cursor.getCount() > 0) {
            cursor.close();
            myDb.close();
            return true;
        } else {
            cursor.close();
            myDb.close();
            return false;
        }
    }

    public Boolean checkContactNumber(String contactNumber) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE contactNumber = ?", new String[]{contactNumber});

        if (cursor.getCount() > 0) {
            cursor.close();
            myDb.close();
            return true;
        } else {
            cursor.close();
            myDb.close();
            return false;
        }
    }

    public Boolean checkContactNumberId(String contactNumber, int userId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE contactNumber = ? and userId != ? LIMIT 1", new String[]{contactNumber, String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            cursor.close();
            myDb.close();
            Log.d("Cursor Error", String.valueOf(cursor.getCount()));
            return true;
        } else {
            cursor.close();
            myDb.close();
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
