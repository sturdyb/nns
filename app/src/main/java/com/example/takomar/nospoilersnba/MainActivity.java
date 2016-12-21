package com.example.takomar.nospoilersnba;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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

                if (view.getId() == R.id.button){
                    Toast.makeText(getApplicationContext(), "quarter 1", Toast.LENGTH_SHORT).show();
                    intent.putExtra("Quarter", "7200");
                }
                else if (view.getId() == R.id.button2){
                    Toast.makeText(getApplicationContext(), "quarter 2", Toast.LENGTH_SHORT).show();
                    intent.putExtra("Quarter", "14400");
                }
                else if (view.getId() == R.id.button3){
                    Toast.makeText(getApplicationContext(), "quarter 3", Toast.LENGTH_SHORT).show();
                    intent.putExtra("Quarter", "21600");
                }
                else if (view.getId() == R.id.button4){
                    Toast.makeText(getApplicationContext(), "quarter 4", Toast.LENGTH_SHORT).show();
                    intent.putExtra("Quarter", "28800");
                }


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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
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
}

