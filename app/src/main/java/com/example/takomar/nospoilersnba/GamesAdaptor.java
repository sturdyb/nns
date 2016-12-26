package com.example.takomar.nospoilersnba;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class GamesAdaptor extends RecyclerView.Adapter<GamesAdaptor.GameInfoHolder>{
    private List<GameInfo> gameList;

    public GamesAdaptor(List<GameInfo> games) {
        gameList = games;
    }
    public void changeDate(List<GameInfo> games) {
        gameList.clear();
        gameList.addAll(games);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view , String gameId);
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
            q1.setOnClickListener(this);
            q2.setOnClickListener(this);
            q3.setOnClickListener(this);
            q4.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, gameId);
            }
        }
    }

    @Override
    public GameInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);

        return new GameInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameInfoHolder holder, int position) {
        final GameInfo ci = gameList.get(position);
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
        return gameList.size();
    }
}
