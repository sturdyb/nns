package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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
    private Map<Date, List<GameInfo>> allGames;
    private List<GameInfo> gamesByDate;
    private Context mContext;

    public GamesAdaptor(Context context) {
        allGames = new HashMap<>();
        gamesByDate = new ArrayList<>();
        mContext = context;

    }
    public void fillCache(List<GameInfo> games, Date gameDate){
        allGames.put(gameDate, games);
    }
    public void addGames(List<GameInfo> games, Date gameDate) {
        fillCache(games, gameDate);

        gamesByDate = games;
        notifyDataSetChanged();
    }
    private void addGamesFromDate(Date gameDate) {
        if (!allGames.containsKey(gameDate)) {
            new GamesRetriever(mContext, this, true).
                    executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameDate);
        }
    }
    public void changeDate(Date gameDate) {
        if (allGames.containsKey(gameDate)) {
            gamesByDate = allGames.get(gameDate);
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
        final GameInfo ci = gamesByDate.get(position);

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
        return gamesByDate.size();
    }
}
