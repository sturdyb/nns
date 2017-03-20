package com.example.takomar.nospoilersnba;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

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
        void goToNextDate(View v);
        void goToPrevDate(View v);
        void pickDate(View v, AppCompatActivity activity);
        void treatDate(Date date);
    }

    private XmlClickable mCurrentFragment;

    private Map<Date, List<GameInfo>> mGamesByDate = new HashMap<>();
    private List<SimpleGamesRetriever> mCacheTasks = new ArrayList<>();

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

    public boolean alreadyLoadedGames(Date date) {
        return mGamesByDate.containsKey(date);
    }
    public List<GameInfo> retrieveGamesByDate(Date date) {
        return mGamesByDate.get(date);
    }
    public void addGamesByDate(List<GameInfo> games, Date date) {
        mGamesByDate.put(date, games);
    }
    public void addCacheTasks(SimpleGamesRetriever task) {
        mCacheTasks.add(task);
    }
    public void clearCache() {
        for (SimpleGamesRetriever task : mCacheTasks)
            if (task.getStatus() != AsyncTask.Status.FINISHED)
                task.cancel(true);
        mCacheTasks.clear();
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_daily);
        //maindisplay
        Fragment fragment = new DailyFragment();
        mCurrentFragment = (XmlClickable) fragment;
        FragmentManager fragmentManager = getFragmentManager();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.openload) {
            pairWithOpenload();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_daily) {
            clearCache();
            Fragment fragment = new DailyFragment();
            mCurrentFragment = (XmlClickable) fragment;
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_weekly) {
            clearCache();
            Fragment fragment = new WeeklyFragment();
            mCurrentFragment = (XmlClickable) fragment;
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_standings) {
            clearCache();
            Fragment fragment = new StandingsFragment();
            mCurrentFragment = (XmlClickable) fragment;
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            item.setChecked(true);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void pairWithOpenload() {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://openload.co/pair"));
        startActivity(intent);
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
