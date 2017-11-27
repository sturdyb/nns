package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.takomar.nospoilersnba.component.GameInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class GamesAdaptor extends RecyclerView.Adapter<GamesAdaptor.GameInfoHolder>{
    private List<GameInfo> gamesToList = new ArrayList<>();
    private Context mContext;
    private boolean mShowDate;

    public GamesAdaptor(Context context) {
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
        protected ImageView homeLogo;
        protected ImageView visitorLogo;
        protected TextView gameDate;
        protected TextView gameTime;
        protected LinearLayout livePanel;
        protected LinearLayout quarterPanel;
        protected String gameId;
        protected String gameCode;

        public GameInfoHolder(View v) {
            super(v);
            homeTeam =  (TextView) v.findViewById(R.id.firstTeam);
            visitorTeam = (TextView)  v.findViewById(R.id.SecondTeam);
            gameDate = (TextView)  v.findViewById(R.id.dateGame);
            gameTime = (TextView) v.findViewById(R.id.gameTime);
            homeScore = (TextView)  v.findViewById(R.id.firstScore);
            visitorScore = (TextView)  v.findViewById(R.id.secondScore);
            homeLogo = (ImageView)  v.findViewById(R.id.homeLogo);
            visitorLogo = (ImageView)  v.findViewById(R.id.awayLogo);
            livePanel = (LinearLayout) v.findViewById(R.id.livePanel);
            quarterPanel = (LinearLayout) v.findViewById(R.id.quarterPanel);

            Button q1 = (Button) v.findViewById(R.id.button);
            Button q2 = (Button) v.findViewById(R.id.button2);
            Button q3 = (Button) v.findViewById(R.id.button3);
            Button q4 = (Button) v.findViewById(R.id.button4);
            Button live = (Button) v.findViewById(R.id.liveStats);
            ImageView highlights = (ImageView) v.findViewById(R.id.buttonSearch);
            ImageView watch = (ImageView) v.findViewById(R.id.buttonWatch);
            highlights.setOnClickListener(this);
            watch.setOnClickListener(this);
            q1.setOnClickListener(this);
            q2.setOnClickListener(this);
            q3.setOnClickListener(this);
            q4.setOnClickListener(this);
            live.setOnClickListener(this);

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

    public void setGameInformation(GameInfoHolder holder, final GameInfo gameInfo ) {
        holder.homeTeam.setText(gameInfo.homeTeam);
        holder.visitorTeam.setText(gameInfo.visitorTeam);

        int imageResource = mContext.getResources()
                                    .getIdentifier(Helper.imageTeam.get(gameInfo.homeTeam),
                                                   null, mContext.getPackageName());
        holder.homeLogo.setImageDrawable(mContext.getResources().getDrawable(imageResource));


        imageResource = mContext.getResources()
                                .getIdentifier(Helper.imageTeam.get(gameInfo.visitorTeam),
                                               null, mContext.getPackageName());
        holder.visitorLogo.setImageDrawable(mContext.getResources().getDrawable(imageResource));

        holder.gameId = gameInfo.gameID;
        holder.gameCode = gameInfo.gameCode;

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
        holder.gameTime.setText(ci.gameTime);

        if (ci.status == 3) {
            holder.quarterPanel.setVisibility(View.VISIBLE);
            holder.livePanel.setVisibility(View.GONE);
        } else {
            holder.livePanel.setVisibility(View.VISIBLE);
            holder.quarterPanel.setVisibility(View.GONE);
        }

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
