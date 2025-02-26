package com.example.buildyourownmeal;

import static android.app.PendingIntent.getActivity;
import static com.example.buildyourownmeal.R.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavHostKt;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Navbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    //SIDE NAV USERNAME
    private TextView sideNavUsername;

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
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        String userName = userSession.getString("username", "No username");

        if (isUserLoggedIn) {
            String userWelcome = getString(string.hello) + " " + userName;
            sideNavUsername.setText(userWelcome);
        }



        //CART
        FloatingActionButton cartBtn = findViewById(R.id.floatBtn);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navbar.this, cart.class);
                startActivity(intent);
            }
        });


        //BOTTOM NAVBAR
        //HOOK
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navCon = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNav, navCon);
        } else {
            Log.e("Navbar", "NavHostFragment is empty, Check your layout xml");
        }


        //SIDEBAR
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        setToolbarIconSize(toolbar, drawable.baseline_account_circle_24, 110);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
        } else if (id == R.id.signoutbtn) {
            if (isUserLoggedIn) {
                editor.clear();
                editor.apply();

                Intent intent = new Intent(this, Navbar.class);
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
}