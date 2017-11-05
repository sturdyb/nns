package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.takomar.nospoilersnba.component.GameInfo;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.GamesCallback;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.NbaGames;
import com.example.takomar.nospoilersnba.component.Retro.RetroApi;
import com.example.takomar.nospoilersnba.component.Retro.RetroInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

import static com.example.takomar.nospoilersnba.R.id.linlaHeaderProgress;
import static com.example.takomar.nospoilersnba.R.id.noGamesPanel;

/**
 * Created by takomar on 18/03/17.
 */

public class WeeklyFragment extends GamesFragment {
    private SimpleDateFormat mWeeklyFormat = new SimpleDateFormat("MMM d, yyyy");
    private List<GamesRetriever> myTasks = new ArrayList<>();

    public WeeklyFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().findViewById(R.id.standingsDate).setVisibility(View.INVISIBLE);
    }

    @Override
    protected Date getInitialDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return cal.getTime();
    }
    @Override
    protected String formatDisplayDate(Date date) {
        return "Week of " + mWeeklyFormat.format(date);
    }

    @Override
    protected Date parseToDate(String dateText) throws ParseException {
        String text = dateText.substring(8);//length of week of_
        return mWeeklyFormat.parse(text);
    }

    @Override
    protected void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, 7);
    }

    @Override
    protected void decrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, -7);
    }

    public void showGames(MainFragmentActivity activity, Date firstDate) {
        List<GameInfo> weekGames = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        for(int i=0; i<6; i++) {
            calendar.add(Calendar.DATE, 1);
            List<GameInfo> dayGames = activity.retrieveGamesByDate(calendar.getTime());
            if (dayGames != null)
                weekGames.addAll(activity.retrieveGamesByDate(calendar.getTime()));
        }
        weekGames = Helper.retrieveFavoriteTeams(activity, weekGames);

        mRootView.findViewById(linlaHeaderProgress).setVisibility(View.GONE);

        if (weekGames.isEmpty()) {
            mRootView.findViewById(noGamesPanel).setVisibility(View.VISIBLE);
        } else {
            mRootView.findViewById(noGamesPanel).setVisibility(View.GONE);
            Collections.sort(weekGames, new Comparator<GameInfo>() {
                @Override
                public int compare(GameInfo lhs, GameInfo rhs) {
                    return lhs.gameDate.compareTo(rhs.gameDate) ;
                }
            });
            mGamesAdaptor.showGames(weekGames, true);
        }
    }

    @Override
    public void refreshGames(View v) { }

    @Override
    protected void loadGames(Date date) {
        if (getActivity() instanceof MainFragmentActivity) {
            MainFragmentActivity activity = (MainFragmentActivity)getActivity();

            if (activity.mGamesByDate.isEmpty()) {
                RetroInterface apiService = RetroApi.getClient().create(RetroInterface.class);
                Call<NbaGames> call = apiService.getAllGames();
                call.enqueue(new GamesCallback(activity.mGamesByDate,
                                                mRootView, date, mGamesAdaptor));
            }
            else
                showGames(activity,date);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        final SwipeRefreshLayout swipeContainer =(SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
           swipeContainer.setRefreshing(false);
            }
        });
        return v;
    }

    @Override
    public void pickDate(View v, AppCompatActivity activity) {}
}
