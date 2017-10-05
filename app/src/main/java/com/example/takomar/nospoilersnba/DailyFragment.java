package com.example.takomar.nospoilersnba;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.takomar.nospoilersnba.component.GameInfo;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.GamesCallback;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.NbaGames;
import com.example.takomar.nospoilersnba.component.Retro.RetroApi;
import com.example.takomar.nospoilersnba.component.Retro.RetroInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by takomar on 18/03/17.
 */

public class DailyFragment extends GamesFragment {
    private SimpleDateFormat mDailyFormat = new SimpleDateFormat("EEE, MMM d yyyy");

    public DailyFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().findViewById(R.id.standingsDate).setVisibility(View.VISIBLE);
    }

    @Override
    protected Date getInitialDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }

    @Override
    protected String formatDisplayDate(Date date) {
        return mDailyFormat.format(date);
    }

    @Override
    protected Date parseToDate(String dateText) throws ParseException {
        return mDailyFormat.parse(dateText);
    }

    @Override
    protected void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, 1);
    }

    @Override
    protected void decrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, -1);
    }

    @Override
    protected void loadGames(Date date) {
        if (getActivity() instanceof MainFragmentActivity) {
            MainFragmentActivity activity = (MainFragmentActivity) getActivity();

            if (activity.mGamesByDate.isEmpty()) {
                RetroInterface apiService = RetroApi.getClient().create(RetroInterface.class);
                Call<NbaGames> call = apiService.getAllGames();
                call.enqueue(new GamesCallback(activity.mGamesByDate, mRootView, date, mGamesAdaptor));
            }
            else {
                List<GameInfo> games = activity.retrieveGamesByDate(date);
                if(games == null || games.isEmpty())
                    mRootView.findViewById(R.id.noGamesPanel).setVisibility(View.VISIBLE);
                else {
                    mRootView.findViewById(R.id.noGamesPanel).setVisibility(View.GONE);
                    mGamesAdaptor.showGames(games, false);
                }
            }
        }
    }

    @Override
    public void goToCurrentStandings(View v) throws ParseException {
        Button pickDate = ((Button) getActivity().findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();

        Bundle date = new Bundle();
        date.putLong("currentDate", parseToDate(text).getTime());
        Fragment standings = new StandingsFragment();
        standings.setArguments(date);
        ((MainFragmentActivity)getActivity()).changeFragment(standings);
    }
}
