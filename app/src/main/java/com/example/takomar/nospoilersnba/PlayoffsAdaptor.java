package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class PlayoffsAdaptor extends RecyclerView.Adapter<PlayoffsAdaptor.GameInfoHolder>{
    private List<PlayoffsGame> gamesToList = new ArrayList<>();
    private Context mContext;

    public PlayoffsAdaptor(Context context) {
        mContext = context;
        //Log.v("SpoilChk", "adaptor created");
    }

    public void showGames(List<PlayoffsGame> gamesToShow) {
        gamesToList = gamesToShow;
        notifyDataSetChanged();
    }

    public class GameInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView firstTeam;
        protected TextView secondTeam;
        protected TextView firstSeed;
        protected TextView secondSeed;
        protected TextView firstScore;
        protected TextView secondScore;
        protected TextView summary;

        public GameInfoHolder(View v) {
            super(v);
            firstTeam =  (TextView) v.findViewById(R.id.firstTeam);
            secondTeam = (TextView)  v.findViewById(R.id.SecondTeam);
            firstScore = (TextView)  v.findViewById(R.id.firstScore);
            secondScore = (TextView)  v.findViewById(R.id.secondScore);
            firstSeed = (TextView)  v.findViewById(R.id.FirstSeed);
            secondSeed = (TextView)  v.findViewById(R.id.SecondSeed);
            summary = (TextView)  v.findViewById(R.id.summary);
            summary.setAllCaps(false);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public GameInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view_playoffs, parent, false);

        return new GameInfoHolder(itemView);
    }

    public void setGameInformation(GameInfoHolder holder, final PlayoffsGame playoffsGame ) {
        holder.firstTeam.setText(playoffsGame.team1);
        holder.secondTeam.setText(playoffsGame.team2);
        holder.firstScore.setText(playoffsGame.team1Wins);
        holder.secondScore.setText(playoffsGame.team2Wins);
        holder.firstSeed.setText(playoffsGame.team1Seed);
        holder.secondSeed.setText(playoffsGame.team2Seed);
        holder.summary.setText(playoffsGame.summary);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onBindViewHolder(GameInfoHolder holder, int position) {
        final PlayoffsGame playoffsGame = gamesToList.get(position);
        setGameInformation(holder, playoffsGame);
    }

    @Override
    public int getItemCount() {
        return gamesToList.size();
    }
}
