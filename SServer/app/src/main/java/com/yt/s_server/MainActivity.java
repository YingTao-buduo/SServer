package com.yt.s_server;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Home fragment_home = new Home();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fg_container, fragment_home).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_my_course:
                    MyCourse fragment_my_course = new MyCourse();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fg_container, fragment_my_course).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_about_user:
                    AboutUser fragment_about_user = new AboutUser();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fg_container, fragment_about_user).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Home fragment_home = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.fg_container, fragment_home).commitAllowingStateLoss();
    }

}
