package com.example.takomar.nospoilersnba;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static SimpleDateFormat dateFormatApp = new SimpleDateFormat("MM/dd/yyyy");
    @NonNull
    private String getEndPeriod( int buttonId)
    {
        if (buttonId == R.id.button){
            return getString(R.string.q1time);
        }
        if (buttonId == R.id.button2){
            return getString(R.string.q2time);
        }
        if (buttonId == R.id.button3){
            return getString(R.string.q3time);
        }
        return getString(R.string.q4time);
    }

    private String getYestedayDefaultDate() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormatApp.format(cal.getTime());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recViewList = (RecyclerView) findViewById(R.id.cardList);
        recViewList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recViewList.setLayoutManager(llm);
        List<GameInfo> gameInfos = new ArrayList<>();
        GamesAdaptor gamesAdaptor = new GamesAdaptor(this, gameInfos);
        recViewList.setAdapter(gamesAdaptor);

        gamesAdaptor.SetOnItemClickListener(new GamesAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, GamesAdaptor.GameInfoHolder gameInfo) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("GameID", gameInfo.gameId);
                intent.putExtra("Home", gameInfo.homeTeam.getText());
                intent.putExtra("Away", gameInfo.visitorTeam.getText());
                intent.putExtra("Quarter", getEndPeriod(view.getId()));

                startActivity(intent);
            }
        });


        Button datePicker = (Button) findViewById(R.id.pickDate);
        String yesterday = getYestedayDefaultDate();
        datePicker.setText(yesterday);

        new GamesRetriever(this, gamesAdaptor).execute(yesterday);
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
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        else if (id == R.id.openload) {
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String selectedDate = dateFormat.format(cal.getTime());

            Button datePick = (Button) getActivity().findViewById(R.id.pickDate);
            datePick.setText(selectedDate);

            RecyclerView recView = (RecyclerView) getActivity().findViewById(R.id.cardList);
            new GamesRetriever(getContext(), (GamesAdaptor)recView.getAdapter()).execute(selectedDate);
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
            next = dateFormatApp.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        RecyclerView recView = (RecyclerView) findViewById(R.id.cardList);
        pickDate.setText(next);
        new GamesRetriever(this, (GamesAdaptor)recView.getAdapter()).execute(next);
    }
    public void goToPrevDate(View v) {
        Button pickDate = ((Button)findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        String prev = null;
        try {
            Date currentDate = dateFormatApp.parse(text);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, -1);
            prev = dateFormatApp.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        RecyclerView recView = (RecyclerView) findViewById(R.id.cardList);
        pickDate.setText(prev);
        new GamesRetriever(this, (GamesAdaptor)recView.getAdapter()).execute(prev);
    }
    public void pairWithOpenload() {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://openload.co/pair"));
        startActivity(intent);
    }
}

