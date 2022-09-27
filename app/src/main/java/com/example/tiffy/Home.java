package com.example.tiffy;

import static com.example.tiffy.Login.EMAIL;
import static com.example.tiffy.Login.Email;
import static com.example.tiffy.Login.OneTimeLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "LoginPrefs";
    SQLiteHelper sqLiteHelper= new SQLiteHelper(this);
    TextView navUsername, navUseremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.nav_header_name);
        navUseremail = (TextView) headerView.findViewById(R.id.nav_header_email);


        if(OneTimeLogin) {
            navUsername.setText(sqLiteHelper.getDataEmail());
            navUseremail.setText(Email);
        }
        else {
            navUsername.setText(sqLiteHelper.getDataEMAIL());
            navUseremail.setText(EMAIL);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Home.super.onBackPressed();
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            //DrawerLayout drawer = findViewById(R.id.drawer_layout);
            //drawer.closeDrawer(GravityCompat.START);


        }else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_about_us) {


            Intent i= new Intent(getApplicationContext(), AboutUs.class);
            startActivity(i);


        }else if (id == R.id.nav_contact) {


            Intent i= new Intent(getApplicationContext(), ContactUs.class);
            startActivity(i);


        }else if (id == R.id.nav_feedback) {


            Intent i= new Intent(getApplicationContext(), Feedback.class);
            startActivity(i);


        }else if (id == R.id.nav_logout) {

            //Shared Prefrence for the logout of the one time login style
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();


            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


            Toast.makeText(getApplicationContext(),"Logged out!",Toast.LENGTH_SHORT).show();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
