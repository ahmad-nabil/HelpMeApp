package com.ahmad.helpmeapp.HelperSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.databinding.ActivityMainMenuHelperBinding;
import com.ahmad.helpmeapp.login;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuHelper extends AppCompatActivity {
ActivityMainMenuHelperBinding mainMenuHelper;
ActionBarDrawerToggle drawerToggle;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainMenuHelper=ActivityMainMenuHelperBinding.inflate(getLayoutInflater());
        setContentView(mainMenuHelper.getRoot());
        auth=FirebaseAuth.getInstance();
        initializeNav();
        RequestList requestList=new RequestList(mainMenuHelper,this,getSupportFragmentManager());
        requestList.RecycleViewInitialize();
    }

    private void initializeNav() {
   setSupportActionBar(mainMenuHelper.bar);
   drawerToggle=new ActionBarDrawerToggle(this,mainMenuHelper.draweHelper,mainMenuHelper.bar,R.string.open_nav,R.string.close_nav);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mainMenuHelper.draweHelper.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);
mainMenuHelper.navhelper.setNavigationItemSelectedListener(this::onOptionsItemSelected);
  }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profile){
            HelperProfile profile=new HelperProfile();
            getSupportFragmentManager().beginTransaction().replace(mainMenuHelper.ViewsHelper.getId(),profile).addToBackStack(null).commit();
            return true;
        }
        if(item.getItemId()==R.id.Logout){
            startActivity(new Intent(this, login.class));
            auth.signOut();
            finish();
            return true;
        }

        return true;

    }
    @Override
    public void onBackPressed() {
        if (mainMenuHelper.draweHelper.isDrawerOpen(GravityCompat.START)){
            mainMenuHelper.draweHelper.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}