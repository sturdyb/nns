package com.example.takomar.nospoilersnba;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.takomar.nospoilersnba.component.PlayoffsGame;
import com.example.takomar.nospoilersnba.component.PlayoffsTask;

import java.util.ArrayList;
import java.util.List;


public class PlayOffsActivity extends AppCompatActivity {

    private Button mEast;
    private Button mWest;
    private Button mShowingSide;
    private Button mRound1;
    private Button mRound2;
    private Button mRound3;
    private Button mFinals;
    private Button mShowingRound;
    private PlayoffsAdaptor mPlayoffsAdaptor;
    private List<PlayoffsGame> mGames = new ArrayList<>();


    private void enableAllButtons() {
        int colorId = getResources().getColor(R.color.colorPrimaryDark);
        mEast.setEnabled(true);
        mWest.setEnabled(true);
        mRound1.setEnabled(true);
        mRound2.setEnabled(true);
        mRound3.setEnabled(true);
        mFinals.setEnabled(true);
        mEast.setTextColor(colorId);
        mWest.setTextColor(colorId);
        mRound1.setTextColor(colorId);
        mRound2.setTextColor(colorId);
        mRound3.setTextColor(colorId);
        mFinals.setTextColor(colorId);
    }

    private void disableCouple(int eastWest, int round) {
        mShowingRound = ((Button)findViewById(round));
        mShowingSide = ((Button)findViewById(eastWest));
        ((Button)findViewById(eastWest)).setEnabled(false);
        ((Button)findViewById(eastWest)).setTextColor(Color.GRAY);
        ((Button)findViewById(round)).setEnabled(false);
        ((Button)findViewById(round)).setTextColor(Color.GRAY);
    }

    private String getConf(int viewId) {
        if (viewId == R.id.east)
            return "East";
        return "West";
    }
    private int getRound(int viewId) {
        if (viewId == R.id.round1)
            return 1;
        if (viewId == R.id.round2)
            return 2;
        if (viewId == R.id.round3)
            return 3;
        return 4;
    }

    private List<PlayoffsGame> getGames(String conf, int round) {
        List<PlayoffsGame> gamesToShow = new ArrayList<>();

        for(PlayoffsGame game : mGames)
            if ((round == 4 || game.conference.equalsIgnoreCase(conf)) && game.round == round)
                gamesToShow.add(game);

        return gamesToShow;
    }

    private void retrieveDetails(int confId, int roundId) {
        enableAllButtons();
        disableCouple(confId, roundId);
        mPlayoffsAdaptor.showGames(getGames(getConf(confId),
                                            getRound(roundId)));
    }
    public void retrieveInitialDetails(List<PlayoffsGame> games) {
        mGames = games;
        retrieveDetails(R.id.west, R.id.round1);
    }


    public class DetailOnClick implements View.OnClickListener {

        public DetailOnClick() {}

        public void onClick(View v) {
            if (v.getId() == R.id.east || v.getId() == R.id.west)
                retrieveDetails(v.getId(), mShowingRound.getId());
            else
                retrieveDetails(mShowingSide.getId(), v.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playoffs);
        final RecyclerView recViewList = (RecyclerView) findViewById(R.id.cardList);
        recViewList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recViewList.setLayoutManager(llm);
        mPlayoffsAdaptor = new PlayoffsAdaptor(this);
        recViewList.setAdapter(mPlayoffsAdaptor);

        mEast = (Button)findViewById(R.id.east);
        mWest = (Button)findViewById(R.id.west);
        mRound1 = (Button)findViewById(R.id.round1);
        mRound2 = (Button)findViewById(R.id.round2);
        mRound3 = (Button)findViewById(R.id.round3);
        mFinals = (Button)findViewById(R.id.finals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Playoffs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DetailOnClick dOnClick = new DetailOnClick();

        mEast.setOnClickListener(dOnClick);
        mWest.setOnClickListener(dOnClick);
        mRound1.setOnClickListener(dOnClick);
        mRound2.setOnClickListener(dOnClick);
        mRound3.setOnClickListener(dOnClick);
        mFinals.setOnClickListener(dOnClick);

        new PlayoffsTask(this, mPlayoffsAdaptor).execute();
    }

}
