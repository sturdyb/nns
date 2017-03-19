package com.example.takomar.nospoilersnba.component;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.takomar.nospoilersnba.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by takomar on 18/03/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    SimpleDateFormat dateFormatApp = new SimpleDateFormat("EEE, MMM d yyyy");

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Button datePick = (Button) getActivity().findViewById(R.id.pickDate);
        datePick.setText(dateFormatApp.format(cal.getTime()));
    }
}
