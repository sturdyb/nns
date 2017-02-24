package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takomar on 11/12/16.
 */

public class FavGamesAdaptor extends GamesAdaptor{
    private LinearLayout linlaHeaderProgress;
    private List<TeamGamesRetriever> myTasks = new ArrayList<>();

    public FavGamesAdaptor(Context context) {
        super(context);
    }

    @Override
    protected void addGamesFromDate(Date gameDate) {
        if (!alreadyLoaded(gameDate)) {
            addCacheTask((GamesRetriever)
                    new TeamGamesRetriever(mContext, this, true).
                    executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameDate));
        }
    }

    public void updateScreen()
    {
        Collections.sort(gamesToList, new Comparator<GameInfo>() {
            @Override
            public int compare(GameInfo lhs, GameInfo rhs) {
                return lhs.gameDate.compareTo(rhs.gameDate) ;
            }
        });
        linlaHeaderProgress.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    public void changeDate(Date firstDate) {
        gamesToList.clear();

        for (GamesRetriever task : cacheTasks)
            if (task.getStatus() != AsyncTask.Status.FINISHED)
                task.cancel(true);
        cacheTasks.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        calendar.add(Calendar.DATE, 7);
        Date endDate = calendar.getTime();
        linlaHeaderProgress = (LinearLayout) ((Activity) mContext)
                                                .findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        calendar.setTime(firstDate);
        while (firstDate.before(endDate))
        {
            if (alreadyLoaded(firstDate)) {
                gamesToList.addAll(allGames.get(firstDate));
            }
            else
                myTasks.add((TeamGamesRetriever) new TeamGamesRetriever(
                                                        mContext, this, false)
                                                .executeOnExecutor(
                                                        AsyncTask.THREAD_POOL_EXECUTOR, firstDate));

            calendar.add(Calendar.DATE, 1);
            firstDate = calendar.getTime();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);

        for (int i=0; i < 7; i++)
        {
            cal.add(Calendar.DATE, 1);
            addGamesFromDate(cal.getTime());
        }
        cal.setTime(endDate);
        cal.add(Calendar.DATE, -7);
        for (int i=0; i < 7; i++)
        {
            cal.add(Calendar.DATE, -1);
            addGamesFromDate(cal.getTime());
        }
        final Thread waitingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean allDone = false;
                    while (!allDone){
                        allDone = true;
                        for (TeamGamesRetriever task : myTasks)
                            if (task.getStatus() != AsyncTask.Status.FINISHED) {
                                Thread.sleep(200);
                                allDone = false;
                            }
                    }
                    myTasks.clear();
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateScreen();
                        }
                    });

                } catch (InterruptedException e) {
                    // TODO log
                }
            }
        });

        waitingThread.start();

    }

    @Override
    public void onBindViewHolder(GameInfoHolder holder, int position) {
        final GameInfo ci = gamesToList.get(position);
        setGameInformation(holder, ci);
        SimpleDateFormat dateFormatApp = new SimpleDateFormat("EEE, MMM d");
        holder.gameDate.setText(dateFormatApp.format(ci.gameDate));
        holder.gameDate.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return gamesToList.size();
    }
}
