package com.example.buildyourownmeal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

public class home_dashboard extends Fragment /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private DrawerLayout drawerLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        //SIDEBAR
        //HOOK
        /*Toolbar toolbar = view.findViewById(R.id.toolbar);
        drawerLayout = view.findViewById(R.id.home);
        NavigationView navigationView = view.findViewById(R.id.sidebar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        setToolbarIconSize(toolbar, R.drawable.baseline_account_circle_24, 110);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();*/

        return view;
    }

    //FOR DISPLAYING FRAGMENTS FOR MAIN ACTIVITY/navbar.xml
    /*private void hideMainLayout() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.toolbar).setVisibility(NavigationView.GONE);
            view.findViewById(R.id.sidebar).setVisibility(NavigationView.GONE);
            view.findViewById(R.id.bottom_navigation).setVisibility(NavigationView.GONE);
        }
    }

    private void showMainLayout() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.toolbar).setVisibility(NavigationView.VISIBLE);
            view.findViewById(R.id.sidebar).setVisibility(NavigationView.VISIBLE);
            view.findViewById(R.id.bottom_navigation).setVisibility(NavigationView.VISIBLE);
        }
    }

    //LOGIC FOR SIDEBAR ITEMS
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.account) {
            Intent intent = new Intent(getActivity(), account.class);
            startActivity(intent);
        } else if (id == R.id.orderHis) {
            Intent intent = new Intent(getActivity(), orderHistory.class);
            startActivity(intent);
        } else if (id == R.id.contactUs) {
            Intent intent = new Intent(getActivity(), contactUs.class);
            startActivity(intent);
        } else if (id == R.id.termsAndCondition) {
            Intent intent = new Intent(getActivity(), termsAndCondition.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
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
        Drawable drawable = ContextCompat.getDrawable(getContext(),  iconRes);
        if (drawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            toolbar.setNavigationIcon(new BitmapDrawable(getResources(), bitmap));
        }
    }*/
}