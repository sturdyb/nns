package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.takomar.nospoilersnba.component.CacheExecutor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.example.takomar.nospoilersnba.R.id.linlaHeaderProgress;

/**
 * Created by takomar on 18/03/17.
 */

public class WeeklyFragment extends GamesFragment {
    private SimpleDateFormat mWeeklyFormat = new SimpleDateFormat("MMM d, yyyy");
    private List<SimpleGamesRetriever> myTasks = new ArrayList<>();

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
            weekGames.addAll(activity.retrieveGamesByDate(calendar.getTime()));
        }
        weekGames = Helper.retrieveFavoriteTeams(activity, weekGames);

        Collections.sort(weekGames, new Comparator<GameInfo>() {
            @Override
            public int compare(GameInfo lhs, GameInfo rhs) {
                return lhs.gameDate.compareTo(rhs.gameDate) ;
            }
        });
        mRootView.findViewById(linlaHeaderProgress).setVisibility(View.GONE);
        mGamesAdaptor.showGames(weekGames, true);
    }

    private void loadCurrentWeekGames(final MainFragmentActivity activity, final Date firstDate){
        Date tempDate = (Date) firstDate.clone();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);
        incrementCalendar(calendar);

        Date endDate = calendar.getTime();
        calendar.setTime(tempDate);

        mRootView.findViewById(linlaHeaderProgress).setVisibility(View.VISIBLE);

        while (tempDate.before(endDate))
        {
            if (!activity.alreadyLoadedGames(tempDate))
                myTasks.add((SimpleGamesRetriever) new SimpleGamesRetriever(
                        activity, new CacheExecutor(activity))
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tempDate));

            calendar.add(Calendar.DATE, 1);
            tempDate = calendar.getTime();
        }

        final Thread waitingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean allDone = false;
                    while (!allDone){
                        allDone = true;
                        for (SimpleGamesRetriever task : myTasks)
                            if (task.getStatus() != AsyncTask.Status.FINISHED) {
                                Thread.sleep(200);
                                allDone = false;
                            }
                    }
                    myTasks.clear();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showGames(activity, firstDate);
                        }
                    });

                } catch (InterruptedException e) {}
            }
        });
        waitingThread.start();
    }

    @Override
    protected void loadGames(Date date) {
        if (getActivity() instanceof MainFragmentActivity) {
            MainFragmentActivity activity = (MainFragmentActivity)getActivity();

            loadCurrentWeekGames(activity, date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            incrementCalendar(cal);

            Calendar revCal = Calendar.getInstance();
            revCal.setTime(date);

            for (int i=0; i < 7; i++)
            {
                cal.add(Calendar.DATE, 1);
                loadCacheByDate(activity, cal.getTime());
                revCal.add(Calendar.DATE, -1);
                loadCacheByDate(activity, revCal.getTime());
            }
        }
    }

    @Override
    public void pickDate(View v, AppCompatActivity activity) {}
}
