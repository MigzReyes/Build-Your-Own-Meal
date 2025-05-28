package com.example.buildyourownmeal;

import com.example.buildyourownmeal.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.UUID;

public class checkout extends AppCompatActivity {


    //DATABASE
    private databaseFunctions databaseFunctions;

    //RECYCLER VIEW
    private RecyclerView recyclerViewCheckout;
    private ArrayList<Bitmap> mealImg;
    private ArrayList<String> mealType, addBtn, minusBtn, editBtn, orderAddonId;
    private ArrayList<Integer> mealTotalPrice, mealQuantity, trashBtn, checkoutItemPerTotalPrice, userOrderId;

    private RadioButton priority, standard, scheduledDate;
    private TextView sideActName, payment, totalPrice, subtotalPrice, priorityPickUpPrice, addItemBtn, changeScheduleBtn;
    private Button orderBtn;
    private EditText editTextPhone;
    private ImageView backBtn;
    private LinearLayout paymentMethodBtn, orderCon, changeScheduleCon;
    private int userId = 0;
    private SharedPreferences orderSession;
    private SharedPreferences.Editor orderSessionEdit;
    private boolean saveContactNumber = false;
    private String pickUp, paymentMethod;
    private String getContactNumber;
    private String getContactNumberSP;

    private int getPriorityFeePrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //BACK PRESSED ON PHONE SYSTEM
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);

                Intent intent = new Intent(checkout.this, cart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //ORDER SESSION
        orderSession = getSharedPreferences("orderSession", MODE_PRIVATE);
        orderSessionEdit = orderSession.edit();

        //USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor edit = userSession.edit();
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        userId = userSession.getInt("userId", 0);
        saveContactNumber = userSession.getBoolean("saveContactNumber", false);

        //SET ID
        sideActName = findViewById(R.id.sideFragName);
        backBtn = findViewById(R.id.backBtn);
        priority = findViewById(R.id.priorityFee);
        standard = findViewById(R.id.standardFee);
        scheduledDate = findViewById(R.id.scheduledDate);
        changeScheduleCon = findViewById(R.id.changeSchedCon);
        changeScheduleBtn = findViewById(R.id.changeSchedBtn);
        payment = findViewById(R.id.paymentMethod);
        orderBtn = findViewById(R.id.orderBtnCart);
        orderCon = findViewById(R.id.orderCon);
        totalPrice = findViewById(R.id.totalPrice);
        subtotalPrice = findViewById(R.id.subtotalPrice);
        priorityPickUpPrice = findViewById(R.id.priorityPickUpPrice);
        editTextPhone = findViewById(R.id.editTextPhone);
        addItemBtn = findViewById(R.id.addItemBtn);

        //SET TOOLBAR NAME
        sideActName.setText(getString(R.string.smallCheckOut));

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, cart.class);
                startActivity(intent);
            }
        });

        //RADIO BUTTON
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard.setChecked(true);
                priority.setChecked(false);
                scheduledDate.setChecked(false);
                changeScheduleCon.setVisibility(View.GONE);
                priorityPickUpPrice.setText(getString(R.string.priceItem));

                setUpPriorityFee();
            }
        });

        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard.setChecked(false);
                priority.setChecked(true);
                scheduledDate.setChecked(false);
                changeScheduleCon.setVisibility(View.GONE);
                priorityPickUpPrice.setText(getString(R.string.kinse));

                setUpPriorityFee();
            }
        });

        scheduledDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard.setChecked(false);
                priority.setChecked(false);
                scheduledDate.setChecked(true);

                Button saveBtn;

                BottomSheetDialog popUpSched = new BottomSheetDialog(checkout.this);
                View popUpLayout = LayoutInflater.from(checkout.this).inflate(R.layout.pop_up_pick_up, null);
                popUpSched.setContentView(popUpLayout);
                popUpSched.setCancelable(true);

                FrameLayout bottomSheet = popUpSched.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackground(null);
                }
                popUpSched.show();

                saveBtn = popUpSched.findViewById(R.id.saveBtn);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpSched.dismiss();
                        changeScheduleCon.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        changeScheduleBtn.setVisibility(View.GONE);
        changeScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button saveBtn;

                BottomSheetDialog popUpSched = new BottomSheetDialog(checkout.this);
                View popUpLayout = LayoutInflater.from(checkout.this).inflate(R.layout.pop_up_pick_up, null);
                popUpSched.setContentView(popUpLayout);
                popUpSched.setCancelable(true);

                FrameLayout bottomSheet = popUpSched.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackground(null);
                }
                popUpSched.show();

                saveBtn = popUpSched.findViewById(R.id.saveBtn);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpSched.dismiss();
                        changeScheduleCon.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        if (isUserLoggedIn) {
            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            Toast.makeText(this, getString(R.string.logInSignUpFirst), Toast.LENGTH_SHORT).show();
        }


        //ADD ITEM BUTTON
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, menu.class);
                startActivity(intent);
            }
        });

        //DISPLAY CONTACT NUMBER IF SAVED
        if (saveContactNumber) {
            getContactNumberSP = userSession.getString("userContactNumber", " ");
            editTextPhone.setText(getContactNumberSP);
        }

        //DISPLAY CONTACT NUMBER IF USER HAVE ONE
        String getNum = databaseFunctions.getContactNumber(userId);
        try {
            if (!saveContactNumber) {
                editTextPhone.setText(getNum);
            }
        } catch (NullPointerException e) {
            Log.d("may error ka", "No saved contact number");
        }

        changeScheduleCon.setVisibility(View.GONE);

        //RECYCLER
        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        orderAddonId = new ArrayList<>();
        userOrderId = new ArrayList<>();
        mealImg = new ArrayList<>();
        mealType = new ArrayList<>();
        mealTotalPrice = new ArrayList<>();
        mealQuantity = new ArrayList<>();
        trashBtn = new ArrayList<>();
        minusBtn = new ArrayList<>();
        mealQuantity = new ArrayList<>();
        addBtn = new ArrayList<>();
        editBtn = new ArrayList<>();

        setUpCheckoutModel();

        recyclerViewAdapterCheckout recyclerViewAdapterCheckout = new recyclerViewAdapterCheckout(this, orderAddonId, userOrderId, mealImg, mealType, addBtn, minusBtn, editBtn, mealTotalPrice, mealQuantity, trashBtn);

        com.example.buildyourownmeal.recyclerViewAdapterCheckout.setOnPriceUpdatedListener(new recyclerViewAdapterCart.OnPriceUpdateListener() {
            @Override
            public void OnPriceUpdate(int newTotalPrice) {
                getPriorityFeePrice = newTotalPrice;
                setUpPriorityFee();
            }
        });

        recyclerViewAdapterCheckout.recalculateTotalPriceAndNotify();
        recyclerViewAdapterCheckout.notifyDataSetChanged();

        recyclerViewCheckout.setAdapter(recyclerViewAdapterCheckout);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));


        //SET ID
        paymentMethodBtn = findViewById(R.id.paymentCon);

        //PAYMENT METHOD
        paymentMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, paymentMethod.class);
                startActivity(intent);
            }
        });

        //ORDER
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactNumber = editTextPhone.getText().toString().trim();

                boolean checkContactNum = databaseFunctions.checkContactNumberId(getContactNumber, userId);
                boolean checkContactNumAdminOrder = databaseFunctions.checkContactNumberAdminOrder(userId, getContactNumber);

                if (getContactNumber.isBlank()) {
                    popUpAlert(getString(R.string.pleaseFillUpTheInputField));
                } else if (getContactNumber.length() < 11 || getContactNumber.length() > 13) {
                    popUpAlert(getString(R.string.invalidPhoneNumber));
                } else {
                    pickUp = " ";
                    if (standard.isChecked()) {
                        pickUp = "standard";
                    } else if (priority.isChecked()) {
                        pickUp = "priority";
                    } else if (scheduledDate.isChecked()) {
                        pickUp = "scheduled";
                    }

                    if (pickUp.isBlank()) {
                        popUpAlert(getString(R.string.pleaseSelectPickUpOption));
                    } else {
                        if (editTextPhone.getText().toString().trim().equals(getContactNumberSP)) {
                            Log.d("may error ka", getContactNumber);
                            Dialog popUpAlertNum;
                            Button cancelBtnNum, yesBtn;
                            TextView alertText;

                            popUpAlertNum = new Dialog(checkout.this);
                            popUpAlertNum.setContentView(R.layout.pop_up_cancel_order);
                            popUpAlertNum.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            popUpAlertNum.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                            popUpAlertNum.setCancelable(true);
                            popUpAlertNum.show();

                            alertText = popUpAlertNum.findViewById(R.id.alertText);
                            alertText.setText(getString(R.string.doYouWantToSaveThePhoneNumber));

                            cancelBtnNum = popUpAlertNum.findViewById(R.id.cancelBtn);
                            cancelBtnNum.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popUpAlertNum.dismiss();
                                    orderSessionEdit.remove("userContactNumber");
                                    checkout();
                                }
                            });

                            yesBtn = popUpAlertNum.findViewById(R.id.proceedBtn);
                            yesBtn.setText("Yes");
                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    edit.putString("userContactNumber", getContactNumber);
                                    edit.putBoolean("saveContactNumber", true);
                                    edit.apply();
                                    popUpAlertNum.dismiss();
                                    saveContactNumber = false;
                                    checkout();
                                }
                            });

                        } else {
                            if (checkContactNum || checkContactNumAdminOrder) {
                                popUpAlert(getString(R.string.contactNumberIsAlreadyInUsed));
                            } else {
                                Dialog popUpAlertNum;
                                Button cancelBtnNum, yesBtn;
                                TextView alertText;

                                popUpAlertNum = new Dialog(checkout.this);
                                popUpAlertNum.setContentView(R.layout.pop_up_cancel_order);
                                popUpAlertNum.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                popUpAlertNum.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                                popUpAlertNum.setCancelable(true);
                                popUpAlertNum.show();

                                alertText = popUpAlertNum.findViewById(R.id.alertText);
                                alertText.setText(getString(R.string.doYouWantToSaveThePhoneNumber));

                                cancelBtnNum = popUpAlertNum.findViewById(R.id.cancelBtn);
                                cancelBtnNum.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popUpAlertNum.dismiss();
                                        orderSessionEdit.remove("userContactNumber");
                                        checkout();
                                    }
                                });

                                yesBtn = popUpAlertNum.findViewById(R.id.proceedBtn);
                                yesBtn.setText("Yes");
                                yesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (editTextPhone.getText().toString().trim().equals(getContactNumber)) {
                                            edit.putString("userContactNumber", getContactNumber);
                                            edit.putBoolean("saveContactNumber", true);
                                            edit.apply();
                                            popUpAlertNum.dismiss();
                                            saveContactNumber = false;
                                            checkout();
                                        } else {
                                            databaseFunctions.updateContactNumber(userId, getContactNumber);
                                            edit.putString("userContactNumber", getContactNumber);
                                            edit.putBoolean("saveContactNumber", true);
                                            edit.apply();
                                            popUpAlertNum.dismiss();
                                            saveContactNumber = false;
                                            checkout();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

    }

    private void checkout() {
        Dialog popUpAlert;
        Button cancelBtn, proceedBtn;

        popUpAlert = new Dialog(checkout.this);
        popUpAlert.setContentView(R.layout.pop_up_order_confirmation);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        cancelBtn = popUpAlert.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });

        proceedBtn = popUpAlert.findViewById(R.id.proceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkoutTotalPrice = Integer.parseInt(totalPrice.getText().toString().trim());
                String orderGroupId = UUID.randomUUID().toString();

                boolean insertUserCheckout = databaseFunctions.insertUserCheckout(userId, orderGroupId, getContactNumber, pickUp, "Cash", checkoutTotalPrice);

                if (insertUserCheckout) {
                    Cursor cursor = databaseFunctions.getOrderedDate(userId);

                    String getOrderedDate = "";
                    if (cursor.moveToFirst() && cursor != null) {
                        getOrderedDate = cursor.getString(cursor.getColumnIndexOrThrow("creationDate"));
                    }

                    boolean insertAdminOrders = databaseFunctions.insertAdminOrders(userId, orderGroupId, getContactNumber, pickUp, "Cash", checkoutTotalPrice, "Processing", getOrderedDate);

                    if (insertAdminOrders) {
                        Cursor getUserOrder = databaseFunctions.getUserOrder(userId);

                        if (getUserOrder != null && getUserOrder.moveToFirst()) {
                            do {
                                String getOrderAddonId = getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("orderAddonId"));
                                int getUserId = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("userId"));
                                byte[] byteArray = getUserOrder.getBlob(getUserOrder.getColumnIndexOrThrow("mealImg"));
                                Bitmap getMealImg = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                String getMealType = getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("mealType"));
                                int getMealQuantity = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("mealQuantity"));
                                int getOrderTotalPrice = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("orderTotalPrice"));
                                String getOrderDate = getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("creationDate"));
                                databaseFunctions.insertAdminUserOrder(getOrderAddonId, orderGroupId, getUserId, getMealImg, getMealType, getMealQuantity, getOrderTotalPrice, getOrderDate);
                            } while (getUserOrder.moveToNext());
                        }

                        Cursor getAddonData = databaseFunctions.getAddonData(userId);

                        if (getAddonData != null && getAddonData.moveToFirst()) {
                            do {
                                int getUserId = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("userId"));
                                String getAddonGroupId = getAddonData.getString(getAddonData.getColumnIndexOrThrow("addonGroupId"));
                                String getAddon = getAddonData.getString(getAddonData.getColumnIndexOrThrow("addon"));
                                int getQuantity = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("quantity"));
                                int getPrice = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("price"));
                                String getOrderedDateDb = getAddonData.getString(getAddonData.getColumnIndexOrThrow("creationDate"));

                                databaseFunctions.insertAdminOrderAddon(getUserId, getAddonGroupId, orderGroupId, getAddon, getQuantity, getPrice, getOrderedDateDb);
                            } while (getAddonData.moveToNext());
                            getAddonData.close();
                        }

                        boolean deleteUserOrder = databaseFunctions.deleteOrderUser(userId);

                        if (deleteUserOrder) {
                            boolean deleteUserOrderAddon = databaseFunctions.deleteOrderAddonWithUserId(userId);

                            if (deleteUserOrderAddon) {
                                popUpAlert.dismiss();
                                orderSessionEdit.putBoolean("checkIfUserOrdered", true);
                                orderSessionEdit.putInt("userId", userId);
                                orderSessionEdit.putString("orderGroupId", orderGroupId);
                                orderSessionEdit.apply();
                                Intent intent = new Intent(checkout.this, Navbar.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("may error ka", "delete user order addon failed");
                            }
                        } else {
                            Log.d("may error ka", "delete user order failed");
                        }
                    } else {
                        Log.d("may error ka", "failed insert user order to admin");
                    }
                } else {
                    Log.d("may error ka", "insert user checkout failed");
                }
            }
        });
    }

    private void setUpPriorityFee() {
        int finalPrice = priority.isChecked() ? getPriorityFeePrice + 15 : getPriorityFeePrice;
        totalPrice.setText(String.valueOf(finalPrice));
    }


    private void setUpCheckoutModel () {
        Cursor getCartData = databaseFunctions.getUserOrder(userId);

        if (getCartData != null && getCartData.moveToFirst()) {
            do {
                orderAddonId.add(getCartData.getString(getCartData.getColumnIndexOrThrow("orderAddonId")));
                userOrderId.add(getCartData.getInt(getCartData.getColumnIndexOrThrow("userOrderId")));
                byte[] byteArray = getCartData.getBlob(getCartData.getColumnIndexOrThrow("mealImg"));
                mealImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                mealType.add(getCartData.getString(getCartData.getColumnIndexOrThrow("mealType")));
                mealTotalPrice.add(getCartData.getInt(getCartData.getColumnIndexOrThrow("orderTotalPrice")));
                mealQuantity.add(getCartData.getInt(getCartData.getColumnIndexOrThrow("mealQuantity")));
                trashBtn.add(R.drawable.trashicon);
                minusBtn.add("-");
                addBtn.add("+");
                editBtn.add("Edit");
            } while (getCartData.moveToNext());
        }
    }

    public void popUpAlert(String getAlertText) {
        Dialog popUpAlert;
        Button closeBtn;
        TextView alertText;

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_alerts);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        alertText = popUpAlert.findViewById(R.id.alertText);
        alertText.setText(getAlertText);

        closeBtn = popUpAlert.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });
    }
}