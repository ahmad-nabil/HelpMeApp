package com.ahmad.helpmeapp.UserSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.databinding.ActivityMainMenuUserBinding;
import com.ahmad.helpmeapp.login;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuUser extends AppCompatActivity {
    ActivityMainMenuUserBinding UserBinding;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserBinding = ActivityMainMenuUserBinding.inflate(getLayoutInflater());
        setContentView(UserBinding.getRoot());
        auth = FirebaseAuth.getInstance();
        MapUser mapUser = new MapUser(UserBinding, this);
        mapUser.initializeUI();
        NavigationInitialize();

    }
//initialize navigation
    private void NavigationInitialize() {
        setSupportActionBar(UserBinding.toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, UserBinding.drawer, UserBinding.toolbar, R.string.close_nav, R.string.open_nav);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        UserBinding.drawer.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //change icon nav
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);
        UserBinding.nav.setNavigationItemSelectedListener(this::onOptionsItemSelected);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile) {
            UserProfile profile = new UserProfile();
            getSupportFragmentManager().beginTransaction().replace(UserBinding.Views.getId(), profile).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.orders) {
            Orders orders = new Orders();
            getSupportFragmentManager().beginTransaction().replace(UserBinding.Views.getId(), orders).addToBackStack(null).commit();
            return true;
        } else if (item.getItemId() == R.id.Logout) {
            auth.signOut();
            startActivity(new Intent(this, login.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (UserBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            UserBinding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}