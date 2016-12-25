package com.example.takomar.nospoilersnba;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final Calendar cal = Calendar.getInstance();
    private static int selectedYear = cal.get(Calendar.YEAR);
    private static int selectedMonth = cal.get(Calendar.MONTH);
    private static int selectedDay = cal.get(Calendar.DAY_OF_MONTH);

    @NonNull
    private String getEndPeriod( int buttonId)
    {
        if (buttonId == R.id.button){
            Toast.makeText(getApplicationContext(), "quarter 1", Toast.LENGTH_SHORT).show();
            return "7200";
        }
        if (buttonId == R.id.button2){
            Toast.makeText(getApplicationContext(), "quarter 2", Toast.LENGTH_SHORT).show();
            return "14400";
        }
        if (buttonId == R.id.button3){
            Toast.makeText(getApplicationContext(), "quarter 3", Toast.LENGTH_SHORT).show();
            return "21600";
        }
        Toast.makeText(getApplicationContext(), "quarter 4", Toast.LENGTH_SHORT).show();
        return "28800";
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
        GamesAdaptor ga = new GamesAdaptor(gameInfos);
        recViewList.setAdapter(ga);

        ga.SetOnItemClickListener(new GamesAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String gameId) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("GameID", gameId);
                intent.putExtra("Quarter", getEndPeriod(view.getId()));

                startActivity(intent);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterdayNba = dateFormat.format(cal.getTime());

        Button datePicker = (Button) findViewById(R.id.pickDate);
        datePicker.setText(yesterdayNba);

        new GamesRetriever(getApplicationContext(), ga).execute(yesterdayNba);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(
                    getActivity(), R.style.DialogTheme,
                    this, selectedYear, selectedMonth, selectedDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            selectedYear = year;
            selectedMonth = month;
            selectedDay = day;
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
    public void pairWithOpenload(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://openload.co/pair"));
        startActivity(intent);
    }
}

