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

public class SimpleGamesAdaptor extends RecyclerView.Adapter<SimpleGamesAdaptor.GameInfoHolder>{
    private List<GameInfo> gamesToList = new ArrayList<>();
    private Context mContext;
    private boolean mShowDate;

    public SimpleGamesAdaptor(Context context) {
        mContext = context;
    }

    public void showGames(List<GameInfo> gamesToShow, boolean showDate) {
        mShowDate = showDate;
        gamesToList = gamesToShow == null ? new ArrayList<GameInfo>() : gamesToShow;
        notifyDataSetChanged();
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
        protected TextView homeScore;
        protected TextView visitorScore;
        protected TextView gameDate;
        protected String gameId;

        public GameInfoHolder(View v) {
            super(v);
            homeTeam =  (TextView) v.findViewById(R.id.firstTeam);
            visitorTeam = (TextView)  v.findViewById(R.id.SecondTeam);
            gameDate = (TextView)  v.findViewById(R.id.dateGame);
            homeScore = (TextView)  v.findViewById(R.id.firstScore);
            visitorScore = (TextView)  v.findViewById(R.id.secondScore);

            Button q1 = (Button) v.findViewById(R.id.button);
            Button q2 = (Button) v.findViewById(R.id.button2);
            Button q3 = (Button) v.findViewById(R.id.button3);
            Button q4 = (Button) v.findViewById(R.id.button4);
            ImageView iv = (ImageView) v.findViewById(R.id.buttonSearch);
            iv.setOnClickListener(this);
            q1.setOnClickListener(this);
            q2.setOnClickListener(this);
            q3.setOnClickListener(this);
            q4.setOnClickListener(this);
        }
        public void isFavorite() {
            itemView.findViewById(R.id.card_frame).setVisibility(View.VISIBLE);
            homeScore.setVisibility(View.INVISIBLE);
            visitorScore.setVisibility(View.INVISIBLE);
        }
        public void isNormal() {
            itemView.findViewById(R.id.card_frame).setVisibility(View.INVISIBLE);
            if(Helper.shouldShowScores(itemView.getContext())) {
                homeScore.setVisibility(View.VISIBLE);
                visitorScore.setVisibility(View.VISIBLE);
            }
            else {
                homeScore.setVisibility(View.INVISIBLE);
                visitorScore.setVisibility(View.INVISIBLE);
            }
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
                inflate(R.layout.card_view_schedule, parent, false);

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
        holder.homeScore.setText(ci.homePts + "");
        holder.visitorScore.setText(ci.visitorPts + "");
        if(Helper.isTeamFavorite(mContext, ci.homeTeam) ||
           Helper.isTeamFavorite(mContext, ci.visitorTeam))
            holder.isFavorite();
        else
            holder.isNormal();

        if (mShowDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
            holder.gameDate.setText(dateFormat.format(ci.gameDate));
            holder.gameDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return gamesToList.size();
    }
}
