package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.takomar.nospoilersnba.component.BoxScoreTask;
import com.example.takomar.nospoilersnba.component.IBoxScoreViewUpdater;
import com.example.takomar.nospoilersnba.component.PlayerInfo;
import com.example.takomar.nospoilersnba.component.Retro.Game.BoxScoreCallback;
import com.example.takomar.nospoilersnba.component.Retro.Game.GameDetails;
import com.example.takomar.nospoilersnba.component.Retro.RetroApi;
import com.example.takomar.nospoilersnba.component.Retro.RetroInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsActivity extends AppCompatActivity implements IBoxScoreViewUpdater {

    private String mHome;
    private String mAway;
    private TableLayout mHomeNames ;
    private TableLayout mAwayNames;
    private TableLayout mHomeBox ;
    private TableLayout mAwayBox;
    private Button mOvertime;
    private Button mQuarter1;
    private Button mQuarter2;
    private Button mQuarter3;
    private Button mQuarter4;
    private String mQuarterTime;

    @Override
    public void requestLayouts() {
        mHomeBox.requestLayout();
        mAwayBox.requestLayout();
        mHomeNames.requestLayout();
        mAwayNames.requestLayout();
    }

    private int getDpInPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private void createHeaders(String name, TableLayout namesCol,
                               TableLayout body, boolean isRed) {
        TableRow nameHeader = (TableRow) LayoutInflater.from(this).inflate(R.layout.name_col, null);
        TextView headerName = ((TextView)nameHeader.findViewById(R.id.name));
        headerName.setTextColor(getResources().getColor(R.color.cardview_light_background));
        if(isRed)
            nameHeader.setBackgroundColor(getResources().getColor(R.color.red));
        else
            nameHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        int dpAsPixels = getDpInPx(3);
        headerName.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        headerName.setText(name);
        headerName.setTypeface(null, Typeface.BOLD);
        namesCol.addView(nameHeader);

        TableRow header = (TableRow) LayoutInflater.from(this).inflate(R.layout.header_line, null);
        if(isRed)
            header.setBackgroundColor(getResources().getColor(R.color.red));
        else
            header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((TextView)header.findViewById(R.id.min)).setText("MIN");
        ((TextView)header.findViewById(R.id.pts)).setText("PTS");
        ((TextView)header.findViewById(R.id.fgm)).setText("FG");
        ((TextView)header.findViewById(R.id.tgm)).setText("3PTS");
        ((TextView)header.findViewById(R.id.reb)).setText("REB");
        ((TextView)header.findViewById(R.id.stl)).setText("STL");
        ((TextView)header.findViewById(R.id.ass)).setText("ASS");
        ((TextView)header.findViewById(R.id.blk)).setText("BLK");
        ((TextView)header.findViewById(R.id.to)).setText("TO");
        ((TextView)header.findViewById(R.id.fouls)).setText("FLS");
        ((TextView)header.findViewById(R.id.pm)).setText("+/-");
        body.addView(header);
    }
    public void createHomeHeaders() {
        createHeaders(mHome, mHomeNames, mHomeBox, false);
    }
    public void createAwayHeaders() {
        createHeaders(mAway, mAwayNames, mAwayBox, true);
    }

    private void fillStats(PlayerInfo stats, TableLayout namesCol,
                           TableLayout boxScore, boolean isOdd) {
        TableRow nameRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.name_col, null);

        if(stats.name == null) {
            ((TextView) nameRow.findViewById(R.id.name)).setText("Totals");
            ((TextView) nameRow.findViewById(R.id.name)).setTypeface(Typeface.DEFAULT_BOLD);
        }
        else if (stats.pos.isEmpty())
            ((TextView)nameRow.findViewById(R.id.name)).setText(stats.name);
        else
            ((TextView)nameRow.findViewById(R.id.name)).setText(stats.name + " - " + stats.pos);

        TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.detail_line, null);

        ((TextView)row.findViewById(R.id.min)).setText(stats.mins);
        ((TextView)row.findViewById(R.id.fgm)).setText(stats.fgm + "-" + stats.fga);
        ((TextView)row.findViewById(R.id.tgm)).setText(stats.tgm + "-" + stats.tga);
        ((TextView)row.findViewById(R.id.reb)).setText("" + stats.reb);
        ((TextView)row.findViewById(R.id.stl)).setText("" + stats.stl);
        ((TextView)row.findViewById(R.id.ass)).setText("" + stats.ast);
        ((TextView)row.findViewById(R.id.blk)).setText("" + stats.blk);
        ((TextView)row.findViewById(R.id.to)).setText("" + stats.to);
        ((TextView)row.findViewById(R.id.pts)).setText("" + stats.pts);
        ((TextView)row.findViewById(R.id.fouls)).setText("" + stats.fouls);
        ((TextView)row.findViewById(R.id.pm)).setText("" + stats.plusMinus);

        if(isOdd) {
            nameRow.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            row.setBackgroundColor(getResources().getColor(R.color.lightGrey));
        }

        namesCol.addView(nameRow);
        boxScore.addView(row);
    }
    private int fillAllStats(List<PlayerInfo> oneTeam,
                              TableLayout namesCol,
                              TableLayout boxScore) {
        if (oneTeam.isEmpty())
            return 0;
        boolean isOdd = false;
        PlayerInfo totalStats = new PlayerInfo();
        for(PlayerInfo player : oneTeam)
        {
            totalStats.pts += player.pts;
            totalStats.fgm += player.fgm;
            totalStats.fga += player.fga;
            totalStats.tgm += player.tgm;
            totalStats.tga += player.tga;
            totalStats.ast += player.ast;
            totalStats.blk += player.blk;
            totalStats.to  += player.to;
            totalStats.reb += player.reb;
            totalStats.stl += player.stl;
            totalStats.fouls += player.fouls;
            totalStats.plusMinus += player.plusMinus;
            fillStats(player, namesCol, boxScore, isOdd);
            isOdd = !isOdd;
        }
        fillStats(totalStats, namesCol, boxScore, isOdd);

        return totalStats.pts;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showOverTime() {
        mOvertime.setVisibility(View.VISIBLE);
    }

    public int fillHomeStats(List<PlayerInfo> oneTeam) {
        return fillAllStats(oneTeam, mHomeNames, mHomeBox);
    }
    public int fillAwayStats(List<PlayerInfo> oneTeam) {
        return fillAllStats(oneTeam, mAwayNames, mAwayBox);
    }

    @Override
    public View getProgressBar() {
        return findViewById(R.id.linlaHeaderProgress);
    }

    @Override
    public void clearViews() {
        mHomeBox.removeAllViews();
        mAwayBox.removeAllViews();
        mHomeNames.removeAllViews();
        mAwayNames.removeAllViews();
    }

    private void enableAllButtons() {
        int colorId = getResources().getColor(R.color.colorPrimaryDark);
        mQuarter1.setEnabled(true);
        mQuarter2.setEnabled(true);
        mQuarter3.setEnabled(true);
        mQuarter4.setEnabled(true);
        mQuarter1.setTextColor(colorId);
        mQuarter2.setTextColor(colorId);
        mQuarter3.setTextColor(colorId);
        mQuarter4.setTextColor(colorId);
    }

    private void disableNewQuarter(int viewId) {
        Button buttonToDisable;
        if (viewId == mQuarter1.getId())
            buttonToDisable = mQuarter1;
        else if (viewId == mQuarter2.getId())
            buttonToDisable = mQuarter2;
        else if (viewId == mQuarter3.getId())
            buttonToDisable = mQuarter3;
        else
            buttonToDisable = mQuarter4;

        buttonToDisable.setTextColor(Color.GRAY);
        buttonToDisable.setEnabled(false);
    }

    private void retrieveDetailsButton(int viewId, String gameId, String quarterTime) {
        enableAllButtons();
        disableNewQuarter(viewId);
        retrieveDetails(gameId, quarterTime);
    }
    private void retrieveDetails(String gameId, String quarterTime)
    {
        if (quarterTime.equals("live"))
        {
            RetroInterface apiService = RetroApi.getClient().create(RetroInterface.class);
            Call<GameDetails> call = apiService.getGameDetails(gameId);
            call.enqueue(new BoxScoreCallback(this));
            return;
        }
        new BoxScoreTask(DetailsActivity.this, mHome).execute(gameId, quarterTime);
    }

    public class DetailOnClick implements View.OnClickListener {
        private final String mGameId;

        public DetailOnClick(String gameId) {
            mGameId = gameId;
        }

        public void onClick(View v) {
            String currentQTime;
            if (v.getId() == R.id.overtime) {
                int otQ = Integer.parseInt(mQuarterTime) + 3000;
                currentQTime = Integer.toString(otQ);
            }
            else
                currentQTime = Helper.getEndPeriod(DetailsActivity.this, v.getId());
            mQuarterTime = currentQTime;
            retrieveDetailsButton(v.getId(), mGameId, currentQTime);
        }
    }

    private int getQuarterId(String quarter) {
        if (quarter.equals(getString(R.string.q1time)))
            return R.id.q1;
        else if (quarter.equals(getString(R.string.q2time)))
            return R.id.q2;
        else if (quarter.equals(getString(R.string.q3time)))
            return R.id.q3;

        return R.id.q4;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mHomeNames =(TableLayout)findViewById(R.id.namesTableHome);
        mAwayNames =(TableLayout)findViewById(R.id.namesTableVisitor);
        mHomeBox =(TableLayout)findViewById(R.id.boxScoreHome);
        mAwayBox =(TableLayout)findViewById(R.id.boxScoreVisitor);
        mOvertime = (Button)findViewById(R.id.overtime);
        mQuarter1 = (Button)findViewById(R.id.q1);
        mQuarter2 = (Button)findViewById(R.id.q2);
        mQuarter3 = (Button)findViewById(R.id.q3);
        mQuarter4 = (Button)findViewById(R.id.q4);

        mHome = getIntent().getStringExtra("Home");
        mAway = getIntent().getStringExtra("Away");
        final String gameID= getIntent().getStringExtra("GameID");
        final String quarter= getIntent().getStringExtra("Quarter");
        mQuarterTime = quarter;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mAway + " @ " + mHome + " : box score");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        disableNewQuarter(getQuarterId(quarter));

        DetailOnClick dOnClick = new DetailOnClick(gameID);
        mOvertime.setVisibility(View.GONE);
        mOvertime.setOnClickListener(dOnClick);
        mQuarter4.setOnClickListener(dOnClick);
        mQuarter3.setOnClickListener(dOnClick);
        mQuarter2.setOnClickListener(dOnClick);
        mQuarter1.setOnClickListener(dOnClick);

        retrieveDetails(gameID, quarter);
    }

}
