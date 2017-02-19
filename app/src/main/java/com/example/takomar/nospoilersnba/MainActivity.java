package com.example.takomar.nospoilersnba;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static SimpleDateFormat dateFormatApp = new SimpleDateFormat("EEE, MMM d yyyy");

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private GamesAdaptor gamesAdaptor;

    protected Date getInitialDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }
    protected void onCreateSuper(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    protected void setContents() {
        setContentView(R.layout.activity_main);
    }

    protected void checkDrawerListItem(int pos) {
        mDrawerList.setItemChecked(pos, true);
        Log.v("SpoilDbg", mDrawerList.getCheckedItemCount() + " " +
                mDrawerList.getCheckedItemPosition());
    }
    protected void onCreateElements() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, Helper.MenuItems));
        mDrawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.favTeam, R.string.q3time);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        RecyclerView recViewList = (RecyclerView) findViewById(R.id.cardList);
        recViewList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recViewList.setLayoutManager(llm);
    }

    protected void createGamesAdaptor() {
        RecyclerView recViewList = (RecyclerView) findViewById(R.id.cardList);
        gamesAdaptor = new GamesAdaptor(this);
        recViewList.setAdapter(gamesAdaptor);

        gamesAdaptor.SetOnItemClickListener(new GamesAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Context context, GamesAdaptor.GameInfoHolder gameInfo) {
                if(view.getId() == R.id.search_button) {
                    String query =
                            gameInfo.homeTeam.getText() + " " +
                                    gameInfo.visitorTeam.getText() +
                                    " ximo pierto final";
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    return;
                }
                Helper.showGameDetails(context, gameInfo, view.getId());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContents();
        onCreateElements();
        checkDrawerListItem(0);
        createGamesAdaptor();

        Button datePicker = (Button) findViewById(R.id.pickDate);
        datePicker.setText(dateFormatApp.format(getInitialDate()));

        gamesAdaptor.changeDate(getInitialDate());

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkDrawerListItem(0);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Log.v("SpoilDbg", mDrawerList.getCheckedItemCount() + " " +
                mDrawerList.getCheckedItemPosition());
        if (position == 1)
           startActivity(new Intent(getApplicationContext(), FavActivity.class));
        else if (position == 0)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        else if (position == 2)
            startActivity(new Intent(this, SettingsActivity.class));
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.openload) {
            pairWithOpenload();
        }
        return super.onOptionsItemSelected(item);
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Create a new instance of DatePickerDialog and return it
            String text = (String) ((Button)getActivity().findViewById(R.id.pickDate)).getText();
            Calendar calendar = Calendar.getInstance();
            int selectedYear = 0;
            int selectedMonth = 0;
            int selectedDay = 0;
            try {
                Date selectedDate = dateFormatApp.parse(text);
                calendar.setTime(selectedDate);
                selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
                selectedMonth = calendar.get(Calendar.MONTH);
                selectedYear = calendar.get(Calendar.YEAR);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return new DatePickerDialog(
                    getActivity(), R.style.DialogTheme,
                    this, selectedYear, selectedMonth, selectedDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);

            Button datePick = (Button) getActivity().findViewById(R.id.pickDate);
            datePick.setText(dateFormatApp.format(cal.getTime()));

            RecyclerView recView = (RecyclerView) getActivity().findViewById(R.id.cardList);
            String selectedDate = UrlHelper.dateFormatUrl.format(cal.getTime());
            try {
                cal.setTime(UrlHelper.dateFormatUrl.parse(selectedDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((GamesAdaptor)recView.getAdapter()).changeDate(cal.getTime());
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void goToNextDate(View v) {
        Button pickDate = ((Button)findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        String next = null;
        try {
            Date currentDate = dateFormatApp.parse(text);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, 1);
            RecyclerView recView = (RecyclerView) findViewById(R.id.cardList);
            ((GamesAdaptor)recView.getAdapter()).changeDate(calendar.getTime());
            pickDate.setText(dateFormatApp.format(calendar.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void goToPrevDate(View v) {
        Button pickDate = ((Button)findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        try {
            Date currentDate = dateFormatApp.parse(text);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, -1);
            String prev = UrlHelper.dateFormatUrl.format(calendar.getTime());
            RecyclerView recView = (RecyclerView) findViewById(R.id.cardList);
            ((GamesAdaptor)recView.getAdapter()).changeDate(calendar.getTime());

            pickDate.setText(dateFormatApp.format(calendar.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    public void pairWithOpenload() {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://openload.co/pair"));
        startActivity(intent);
    }
}

