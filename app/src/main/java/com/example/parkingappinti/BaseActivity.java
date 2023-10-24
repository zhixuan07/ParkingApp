package com.example.parkingappinti;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);

        bottomNavigationView
                = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
    HomeFragment homeFragment = new HomeFragment();
    VehicleFragment vehicleFragment = new VehicleFragment();
    AccountFragment accountFragment  = new AccountFragment();
    SearchVehicleFragment searchVehicleFragment = new SearchVehicleFragment();


    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, homeFragment)
                        .commit();
                return true;

            case R.id.nav_account:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, accountFragment)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.nav_car:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, vehicleFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.nav_search:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, searchVehicleFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return false;
    }



}
