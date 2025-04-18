package com.example.buildyourownmeal;

import static android.app.PendingIntent.getActivity;
import static com.example.buildyourownmeal.R.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.transition.MaterialArcMotion;

public class Navbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    //SIDE NAV USERNAME
    private TextView sideNavUsername;
    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.navbar);
        //HOOK FOR SIDE BAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.navbar);
        NavigationView navigationView = findViewById(R.id.sidebar);
        navigationView.setNavigationItemSelectedListener(this);

        //SIDE NAV USER DISPLAY
        //GETTING THE ID FROM THE side_nav.xml
        View headerView = navigationView.getHeaderView(0);

        sideNavUsername = headerView.findViewById(R.id.helloUserSideNav);

        //SHARED PREFERENCE CONNECTION
        //SHARED PREFERENCE FOR USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        String userName = userSession.getString("username", "Guest");

        if (isUserLoggedIn) {
            String userWelcome = getString(string.hello) + " " + userName;
            sideNavUsername.setText(userWelcome);
        }


        //BOTTOM NAVBAR
        //HOOK
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setAnimation(null);
        bottomNav.setItemRippleColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navCon = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNav, navCon);
        } else {
            Log.e("Navbar", "NavHostFragment is empty, Check your layout xml");
        }

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu) {
                    Intent intent = new Intent(Navbar.this, menu.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.cart) {
                    Intent intent = new Intent(Navbar.this, cart.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.aboutUs) {
                    Intent intent = new Intent(Navbar.this, aboutUs.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });



        //SIDEBAR
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setTitleTextColor(getColor(color.main_color));
        setToolbarIconSize(toolbar, drawable.bentosidebaricon, 125);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //IF USER IS NOT LOGGED IN DO NOT DISPLAY ACCOUNT, ORDER HISTORY, AND SIGNOUT BUTTON
        Menu menu = navigationView.getMenu();

        if (!isUserLoggedIn) {
            menu.findItem(R.id.account).setVisible(false);
            menu.findItem(R.id.orderHis).setVisible(false);
            menu.findItem(R.id.logOutBtn).setVisible(false);
        }
    }

    //ACTION BAR ICON SIZE METHOD
    private void setToolbarIconSize(Toolbar toolbar, int iconRes, int size) {
        Drawable drawable = ContextCompat.getDrawable(this, iconRes);
        if (drawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            toolbar.setNavigationIcon(new BitmapDrawable(getResources(), bitmap));
        }
    }

    //FOR DISPLAYING FRAGMENTS FOR MAIN ACTIVITY/navbar.xml
    private void hideMainLayout() {
        findViewById(R.id.toolbar).setVisibility(NavigationView.GONE);
        findViewById(R.id.sidebar).setVisibility(NavigationView.GONE);
        findViewById(R.id.bottom_navigation).setVisibility(NavigationView.GONE);
    }

    private void showMainLayout() {
        findViewById(R.id.toolbar).setVisibility(NavigationView.VISIBLE);
        findViewById(R.id.sidebar).setVisibility(NavigationView.VISIBLE);
        findViewById(R.id.bottom_navigation).setVisibility(NavigationView.VISIBLE);
    }

    //LOGIC FOR SIDEBAR ITEMS
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //SHARED PREFERENCE CONNECTION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);

        int id = item.getItemId();
        if (isUserLoggedIn) {
            if (id == R.id.account) {
                Intent intent = new Intent(this, account.class);
                startActivity(intent);
            } else if (id == R.id.orderHis) {
                Intent intent = new Intent(this, orderHistory.class);
                startActivity(intent);
            } else if (id == R.id.contactUs) {
                Intent intent = new Intent(this, contactUs.class);
                startActivity(intent);
            } else if (id == R.id.termsAndCondition) {
                Intent intent = new Intent(this, termsAndCondition.class);
                startActivity(intent);
            } else if (id == R.id.logOutBtn) {
                if (isUserLoggedIn) {
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(this, Navbar.class);
                    startActivity(intent);
                }
            }
        } else {
            if (id == R.id.contactUs) {
                Intent intent = new Intent(this, contactUs.class);
                startActivity(intent);
            } else if (id == R.id.termsAndCondition) {
                Intent intent = new Intent(this, termsAndCondition.class);
                startActivity(intent);
            }
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            showMainLayout();
        } else {
            super.getOnBackPressedDispatcher().onBackPressed();
        }

    }
}