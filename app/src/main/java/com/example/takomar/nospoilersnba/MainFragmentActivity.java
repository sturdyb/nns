package com.example.takomar.nospoilersnba;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;

import com.example.takomar.nospoilersnba.component.GameInfo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    DatePickerDialog.OnDateSetListener {

    public interface XmlClickable {
        void refreshGames(View v);
        void goToNextDate(View v);
        void goToPrevDate(View v);
        void goToInitialDate(View v);
        void goToCurrentStandings(View v) throws ParseException;
        void pickDate(View v, AppCompatActivity activity);
        void treatDate(Date date);
        void dispatchEvent(MotionEvent evt);
    }

    private XmlClickable mCurrentFragment;

    public Map<Date, List<GameInfo>> mGamesByDate = new HashMap<>();

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        mCurrentFragment.treatDate(cal.getTime());
    }

    public List<GameInfo> retrieveGamesByDate(Date date) {
        date.setHours(0);
        return mGamesByDate.get(date);
    }
    public void addGamesByDate(List<GameInfo> games, Date date) {
        date.setHours(0);
        mGamesByDate.put(date, games);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navi drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                                                 R.string.navigation_drawer_open,
                                                                 R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_daily);
        //maindisplay
        Fragment fragment = new DailyFragment();
        mCurrentFragment = (XmlClickable) fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mCurrentFragment.dispatchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.openload) {
            pairWithOpenload();
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(Fragment fragment) {
        mCurrentFragment = (XmlClickable) fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_daily) {
            changeFragment(new DailyFragment());
        } else if (id == R.id.nav_weekly) {
            changeFragment(new WeeklyFragment());
        } else if (id == R.id.nav_standings) {
            changeFragment(new StandingsFragment());
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            item.setChecked(true);
        } else if (id == R.id.nav_playoffs) {
            startActivity(new Intent(this, PlayOffsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void pairWithOpenload() {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://openload.co/pair"));
        startActivity(intent);
    }
    public void goToCurrentStandings(View v) throws ParseException {
        mCurrentFragment.goToCurrentStandings(v);
    }

    public void refreshGames(View v) {
        mCurrentFragment.refreshGames(v);
    }
    public void goToInitialDate(View v) {
        mCurrentFragment.goToInitialDate(v);
    }
    public void goToNextDate(View v) {
        mCurrentFragment.goToNextDate(v);
    }
    public void goToPrevDate(View v) {
        mCurrentFragment.goToPrevDate(v);
    }
    public void showDatePickerDialog(View v) {
        mCurrentFragment.pickDate(v, this);
    }
}
