package com.example.midterm_arina_mirzakhani;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final Fragment tableFragment = new TableFragment();
    private final Fragment historyFragment = new HistoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataStore.init(getApplicationContext());
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_table) {
                setTitle("Times Table");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, tableFragment)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.nav_history) {
                setTitle("History");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, historyFragment)
                        .commit();
                return true;
            }
            return false;
        });


        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_table);
        }
    }
}
