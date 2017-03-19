package com.example.takomar.nospoilersnba;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.takomar.nospoilersnba.component.DailyExecutor;
import com.example.takomar.nospoilersnba.component.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by takomar on 18/03/17.
 */

public class StandingsFragment extends Fragment implements MainFragmentActivity.XmlClickable {
    private SimpleDateFormat mDailyFormat = new SimpleDateFormat("EEE, MMM d yyyy");
    private View mRootView;

    public StandingsFragment() {
    }

    private Date getInitialDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }

    void loadStandings(Date date) {
        new Standings(mRootView, getActivity()).execute(date);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.standings_fragment, container, false);

        Button datePicker = (Button) getActivity().findViewById(R.id.pickDate);
        Date initialDate = getInitialDate();
        datePicker.setText(formatDisplayDate(initialDate));
        loadStandings(initialDate);
        return mRootView;
    }

    protected String formatDisplayDate(Date date) {
        return mDailyFormat.format(date);
    }

    protected Date parseToDate(String dateText) throws ParseException {
        return mDailyFormat.parse(dateText);
    }

    protected void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, 1);
    }

    protected void decrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, -1);
    }

    @Override
    public void goToNextDate(View v) {
        Button pickDate = ((Button)getActivity().findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        Date currentDate = null;
        try {
            currentDate = parseToDate(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        incrementCalendar(calendar);
        loadStandings(calendar.getTime());
        pickDate.setText(formatDisplayDate(calendar.getTime()));
    }

    @Override
    public void goToPrevDate(View v) {
        Button pickDate = ((Button)getActivity().findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        Date currentDate = null;
        try {
            currentDate = parseToDate(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        decrementCalendar(calendar);
        loadStandings(calendar.getTime());
        pickDate.setText(formatDisplayDate(calendar.getTime()));
    }

    @Override
    public void pickDate(View v, AppCompatActivity activity) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }
}
