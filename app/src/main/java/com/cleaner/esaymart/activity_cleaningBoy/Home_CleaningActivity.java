package com.cleaner.esaymart.activity_cleaningBoy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.cleaner.esaymart.R;
import com.cleaner.esaymart.activity.ChoiceActivity;
import com.cleaner.esaymart.fragment.AcceptFragment;
import com.cleaner.esaymart.fragment.ApplyForALeaveFragment;
import com.cleaner.esaymart.fragment.CleaningBoy_attandenceFragment;
import com.cleaner.esaymart.fragment.CurrentBookedServiceFragment;
import com.cleaner.esaymart.fragment.Home_serviceFragment;
import com.cleaner.esaymart.fragment.ProfileFragment;
import com.cleaner.esaymart.fragment.WeeklyReportFragment;
import com.cleaner.esaymart.service.MyService;
import com.cleaner.esaymart.utils.utils;
import com.skyfishjy.library.RippleBackground;

import java.io.File;

public class Home_CleaningActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static FragmentManager fragmentManager;

    Fragment fragment = null;
    private NavigationView navigationView;
    public static String s_emp_id;
    public static String s_user_id;
    public static String s_user_name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__cleaning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentManager = getSupportFragmentManager();
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Home_serviceFragment(),
                            utils.Home_serviceFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //  super.onBackPressed();
            // Find the tag of signup and forgot password fragment
            Fragment ApplyForALeaveFragment = fragmentManager
                    .findFragmentByTag(utils.ApplyForALeaveFragment);
            Fragment ProfileFragment = fragmentManager
                    .findFragmentByTag(utils.ProfileFragment);
            Fragment WeeklyReportFragment = fragmentManager
                    .findFragmentByTag(utils.WeeklyReportFragment);

            // Check if both are null or not
            // If both are not null then replace login fragment else do backpressed
            // task

            if (ProfileFragment != null)
                replaceServiceFragment();
            else if (WeeklyReportFragment != null)
                replaceServiceFragment();
            else if (ApplyForALeaveFragment != null)
                replaceServiceFragment();
            else
                super.onBackPressed();
        }
//            finish();
    }

    private void replaceServiceFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, new Home_serviceFragment(),
                        utils.Home_serviceFragment).commit();
    }

    public void acceptServiceFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, new AcceptFragment(),
                        utils.AcceptFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__cleaning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            fragment=new Home_serviceFragment();
//            loadFragment(fragment,item);
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new Home_serviceFragment(),
                            utils.Home_serviceFragment).commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new ProfileFragment(),
                            utils.ProfileFragment).commit();
        } else if (id == R.id.nav_attendence) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new CleaningBoy_attandenceFragment(),
                            utils.CleaningBoy_attandenceFragment).commit();
        } else if (id == R.id.nav_leave) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new ApplyForALeaveFragment(),
                            utils.ApplyForALeaveFragment).commit();
        } else if (id == R.id.nav_weekly) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new WeeklyReportFragment(),
                            utils.WeeklyReportFragment).commit();
        } else if (id == R.id.nav_service) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.container, new CurrentBookedServiceFragment(),
                            utils.CurrentBookedServiceFragment).commit();
        } else if (id == R.id.nav_logout) {
            insert();
            Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Esay Mart  " + "https://play.google.com/store/apps/details?id=com.cleaner.esaymart&hl=en");
            startActivity(Intent.createChooser(i, "Share via"));
        } else if (id == R.id.nav_send) {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            Intent intent = new Intent(Intent.ACTION_SEND);

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.setType("*/*");
            // Append file and send Intent
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "Share app via"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment, MenuItem item) {
        // load fragment
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            SpannableStringBuilder spannableTitle = new SpannableStringBuilder(menuItem.getTitle());
            menuItem.setTitle(spannableTitle);
            if (menuItem.getItemId() == item.getItemId())
                menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void start(View view) {
        startService(new Intent(Home_CleaningActivity.this, MyService.class));

    }

    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_emp_executed", false);
        edt.commit();
    }
}
