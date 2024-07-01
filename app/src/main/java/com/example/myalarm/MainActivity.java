package com.example.myalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 mViewPager;

    private SQL dbHelper;

    private SQL_Timezone dbTimezone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        setupBottomNavigationView();
        setupViewPager();


        dbHelper = new SQL(this);
        dbTimezone = new SQL_Timezone(this);


        dbTimezone.insertData("Hồ chí Minh" , "Việt Nam" , "Asia/Ho_Chi_Minh");



    }

    private void anhXa() {
        mBottomNavigationView = findViewById(R.id.BottomNavigation);
        mViewPager = findViewById(R.id.ViewPage);
    }

    private void setupBottomNavigationView() {
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.gioquocte) {
                    mViewPager.setCurrentItem(0);
                } else if (itemId == R.id.baothuc) {
                    mViewPager.setCurrentItem(1);
                } else if (itemId == R.id.bamgio) {
                    mViewPager.setCurrentItem(2);
                } else if (itemId == R.id.hengio) {
                    mViewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }



//

    private void setupViewPager() {
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);
        mViewPager.setAdapter(viewPageAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.gioquocte).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.baothuc).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.bamgio).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.hengio).setChecked(true);
                        break;

                }
            }
        });
    }
}
