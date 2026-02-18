package lk.jiat.mshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.logging.Logger;

import lk.jiat.mshop.R;
import lk.jiat.mshop.fragment.CartFragment;
import lk.jiat.mshop.fragment.CategoryFragment;
import lk.jiat.mshop.fragment.HomeFragment;
import lk.jiat.mshop.fragment.MessageFragment;
import lk.jiat.mshop.fragment.OrdersFragment;
import lk.jiat.mshop.fragment.ProfileFragment;
import lk.jiat.mshop.fragment.SettingsFragment;
import lk.jiat.mshop.fragment.WishlistFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.side_navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // default load
            navigationView.getMenu().findItem(R.id.side_nav_home).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        Menu navMenu = navigationView.getMenu();
        Menu bottomNavMenu = bottomNavigationView.getMenu();

        for (int i = 0; i < navMenu.size(); i++) {
            navMenu.getItem(i).setChecked(false);
        }

        for (int i = 0; i < bottomNavMenu.size(); i++) {
            bottomNavMenu.getItem(i).setChecked(false);
        }

        if (itemId == R.id.side_nav_home || itemId == R.id.bottom_nav_home) {
            loadFragment(new HomeFragment());
            navigationView.getMenu().findItem(R.id.side_nav_home).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        } else if (itemId == R.id.side_nav_profile || itemId == R.id.bottom_nav_profile) {
            loadFragment(new ProfileFragment());
            navigationView.getMenu().findItem(R.id.side_nav_profile).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);
        } else if (itemId == R.id.side_nav_order) {
            loadFragment(new OrdersFragment());
            navigationView.getMenu().findItem(R.id.side_nav_order).setChecked(true);
        } else if (itemId == R.id.side_nav_whishlist) {
            loadFragment(new WishlistFragment());
            navigationView.getMenu().findItem(R.id.side_nav_whishlist).setChecked(true);
        } else if (itemId == R.id.side_nav_cart || itemId == R.id.bottom_nav_cart) {
            loadFragment(new CartFragment());
            navigationView.getMenu().findItem(R.id.side_nav_cart).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_cart).setChecked(true);
        } else if (itemId == R.id.side_nav_message) {
            loadFragment(new MessageFragment());
            navigationView.getMenu().findItem(R.id.side_nav_message).setChecked(true);
        } else if (itemId == R.id.side_nav_settings) {
            loadFragment(new SettingsFragment());
            navigationView.getMenu().findItem(R.id.side_nav_settings).setChecked(true);
        } else if (itemId == R.id.bottom_nav_category) {
            loadFragment(new CategoryFragment());
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_category).setChecked(true);
        } else if (itemId == R.id.side_nav_login) {
            Intent intent = new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.side_nav_logout) {

        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_container,fragment);
//        transaction.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}