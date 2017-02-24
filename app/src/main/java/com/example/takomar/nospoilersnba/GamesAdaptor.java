package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takomar on 11/12/16.
 */

public class GamesAdaptor extends RecyclerView.Adapter<GamesAdaptor.GameInfoHolder>{
    protected Map<Date, List<GameInfo>> allGames;
    protected List<GameInfo> gamesToList;
    protected Context mContext;
    protected List<GamesRetriever> cacheTasks = new ArrayList<>();

    public GamesAdaptor(Context context) {
        Log.v("SpoilChk", "adaptor created");
        allGames = new HashMap<>();
        gamesToList = new ArrayList<>();
        mContext = context;

    }

    protected void addCacheTask(GamesRetriever task) {
        cacheTasks.add(task);
    }
    protected boolean alreadyLoaded(Date date) {
        return  allGames.containsKey(date);
    }

    protected void addGamesFromDate(Date gameDate) {
        if (!alreadyLoaded(gameDate))
            addCacheTask((GamesRetriever)
                    new GamesRetriever(mContext, this, true).
                            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameDate));
    }

    public List<GamesRetriever> getCacheTasks() {
        return cacheTasks;
    }

    public void fillCache(List<GameInfo> games, Date gameDate){
        allGames.put(gameDate, games);
    }

    public void addGames(List<GameInfo> games, Date gameDate) {
        fillCache(games, gameDate);
        gamesToList.addAll(games);
    }

    public void changeDate(Date gameDate) {
        gamesToList.clear();

        if (alreadyLoaded(gameDate)) {
            gamesToList.addAll(allGames.get(gameDate));
            notifyDataSetChanged();
        }
        else
            new GamesRetriever(mContext, this, false).execute(gameDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(gameDate);
        for (int i=0; i < 5; i++)
        {
            cal.add(Calendar.DATE, 1);
            addGamesFromDate(cal.getTime());
        }
        cal.setTime(gameDate);
        for (int i=-5; i < 0; i++)
        {
            cal.add(Calendar.DATE, -1);
            addGamesFromDate(cal.getTime());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , Context context, GameInfoHolder gameInfo);
    }
    private OnItemClickListener mItemClickListener;
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class GameInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView homeTeam;
        protected TextView visitorTeam;
        protected TextView gameDate;
        protected String gameId;

        public GameInfoHolder(View v) {
            super(v);
            homeTeam =  (TextView) v.findViewById(R.id.firstTeam);
            visitorTeam = (TextView)  v.findViewById(R.id.SecondTeam);
            gameDate = (TextView)  v.findViewById(R.id.dateGame);
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

    public void setGameInformation(GameInfoHolder holder, final GameInfo ci ) {
        holder.homeTeam.setText(ci.homeTeam);
        holder.visitorTeam.setText(ci.visitorTeam);
        holder.gameId = ci.gameID;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onBindViewHolder(GameInfoHolder holder, int position) {
        final GameInfo ci = gamesToList.get(position);
        setGameInformation(holder, ci);
        if(Helper.isTeamFavorite(mContext, ci.homeTeam) ||
           Helper.isTeamFavorite(mContext, ci.visitorTeam))
            holder.isFavorite();
        else
            holder.isNormal();
    }

    @Override
    public int getItemCount() {
        return gamesToList.size();
    }
}
