package com.kelompok9PBP.ShoppingList;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelompok9PBP.ShoppingList.Fragment.AddFragment;
import com.kelompok9PBP.ShoppingList.Fragment.HistoryFragment;
import com.kelompok9PBP.ShoppingList.Fragment.HomeFragment;
import com.kelompok9PBP.ShoppingList.Fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Fragment homeFragment = new HomeFragment();
    Fragment addFragment = new AddFragment();
    Fragment historyFragment = new HistoryFragment();
    Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_fragment, homeFragment)
                .commit();

        // Bottom nav listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = homeFragment;
            } else if (itemId == R.id.nav_add) {
                selectedFragment = addFragment;
            } else if (itemId == R.id.nav_history) {
                selectedFragment = historyFragment;
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = profileFragment;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_fragment, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        // Inset padding handler (bawaan EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }
}
