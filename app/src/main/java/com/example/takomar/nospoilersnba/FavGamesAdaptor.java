package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by takomar on 11/12/16.
 */

public class FavGamesAdaptor extends RecyclerView.Adapter<FavGamesAdaptor.GameInfoHolder>{
    private Map<Date, List<GameInfo>> allGames;
    private List<GameInfo> gamesByWeek;
    private Context mContext;
    private LinearLayout linlaHeaderProgress;
    private List<TeamGamesRetriever> myTasks = new ArrayList<>();
    private List<TeamGamesRetriever> cacheTasks = new ArrayList<>();

    public FavGamesAdaptor(Context context) {
        allGames = new HashMap<>();
        gamesByWeek = new ArrayList<>();
        mContext = context;
    }
    public void fillCache(List<GameInfo> games, Date gameDate){
        allGames.put(gameDate, games);
    }
    public void addGames(List<GameInfo> games, Date gameDate) {
        fillCache(games, gameDate);

        gamesByWeek.addAll(games);
       // updateScreen();
    }
    private void addGamesFromDate(Date gameDate) {
        if (!allGames.containsKey(gameDate)) {
            cacheTasks.add((TeamGamesRetriever)
                    new TeamGamesRetriever(mContext, this, true).
                    executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameDate));
        }
    }
    public void updateScreen()
    {
        linlaHeaderProgress.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    public void changeDate(Date firstDate) {
        gamesByWeek.clear();

        for (TeamGamesRetriever task : cacheTasks)
            if (task.getStatus() != AsyncTask.Status.FINISHED)
                task.cancel(true);
        cacheTasks.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        calendar.add(Calendar.DATE, 7);
        Date endDate = calendar.getTime();
        linlaHeaderProgress =
                (LinearLayout)
                        ((Activity) mContext).findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        calendar.setTime(firstDate);
        while (firstDate.before(endDate))
        {
            if (allGames.containsKey(firstDate)) {
                gamesByWeek.addAll(allGames.get(firstDate));
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
                                Thread.sleep(500);
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

    public interface OnItemClickListener {
        void onItemClick(View view, Context context, GameInfoHolder gameInfo);
    }
    private OnItemClickListener mItemClickListener;
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class GameInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView homeTeam;
        protected TextView visitorTeam;
        protected String gameId;

        public GameInfoHolder(View v) {
            super(v);
            homeTeam =  (TextView) v.findViewById(R.id.firstTeam);
            visitorTeam = (TextView)  v.findViewById(R.id.SecondTeam);
            Button q1 = (Button) v.findViewById(R.id.button);
            Button q2 = (Button) v.findViewById(R.id.button2);
            Button q3 = (Button) v.findViewById(R.id.button3);
            Button q4 = (Button) v.findViewById(R.id.button4);
            ImageView iv = (ImageView) v.findViewById(R.id.search_button);
            iv.setOnClickListener(this);
            q1.setOnClickListener(this);
            q2.setOnClickListener(this);
            q3.setOnClickListener(this);
            q4.setOnClickListener(this);
        }
        public void isFavorite() {
            itemView.findViewById(R.id.card_frame).setVisibility(View.VISIBLE);
        }
        public void isNormal() {
            itemView.findViewById(R.id.card_frame).setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mContext, this);
            }
        }
    }

    @Override
    public GameInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view_linear, parent, false);

        return new GameInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameInfoHolder holder, int position) {
        final GameInfo ci = gamesByWeek.get(position);

        if(Helper.isTeamFavorite(mContext, ci.homeTeam) ||
           Helper.isTeamFavorite(mContext, ci.visitorTeam))
            holder.isFavorite();
        else
            holder.isNormal();

        holder.homeTeam.setText(ci.homeTeam);
        holder.visitorTeam.setText(ci.visitorTeam);
        holder.gameId = ci.gameID;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("YO", "onClick: " +  ci.homeTeam);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gamesByWeek.size();
    }
}
