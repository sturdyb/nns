package com.example.takomar.nospoilersnba;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;


public class DetailsActivity extends AppCompatActivity {

    private TableLayout mHomeNames ;
    private TableLayout mVisitorNames ;
    private TableLayout mHomeBox ;
    private TableLayout mVisitorBox ;
    private Button mOvertime;
    private Button mQuarter1;
    private Button mQuarter2;
    private Button mQuarter3;
    private Button mQuarter4;
    private String mQuarterTime;

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

    private void retrieveDetailsButton(int viewId, String gameId, String home,
                                       String away, String quarterTime) {
        enableAllButtons();
        disableNewQuarter(viewId);
        retrieveDetails(gameId, home, away, quarterTime);
    }
    private void retrieveDetails(String gameId, String home,
                                 String away, String quarterTime)
    {
        new GameDetailRetriever(DetailsActivity.this,
                                home, mHomeBox, mHomeNames,
                                away, mVisitorBox, mVisitorNames)
                                .execute(gameId, quarterTime);
    }

    public class DetailOnClick implements View.OnClickListener {
        private final String mHome;
        private final String mGameId;
        private final String mAway;

        public DetailOnClick(String home, String away, String gameId) {
            mHome = home;
            mAway = away;
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
            retrieveDetailsButton(v.getId(), mGameId, mHome, mAway, currentQTime);
        }
    }

    private int getQuarterId(String quarter) {
        if (quarter.equals(getString(R.string.q1time)))
            return R.id.button;
        else if (quarter.equals(getString(R.string.q2time)))
            return R.id.button2;
        else if (quarter.equals(getString(R.string.q3time)))
            return R.id.button3;

        return R.id.button4;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mHomeNames =(TableLayout)findViewById(R.id.namesTableHome);
        mVisitorNames =(TableLayout)findViewById(R.id.namesTableVisitor);
        mHomeBox =(TableLayout)findViewById(R.id.boxScoreHome);
        mVisitorBox =(TableLayout)findViewById(R.id.boxScoreVisitor);
        mOvertime = (Button)findViewById(R.id.overtime);
        mQuarter1 = (Button)findViewById(R.id.button);
        mQuarter2 = (Button)findViewById(R.id.button2);
        mQuarter3 = (Button)findViewById(R.id.button3);
        mQuarter4 = (Button)findViewById(R.id.button4);

        final String home = getIntent().getStringExtra("Home");
        final String away = getIntent().getStringExtra("Away");
        final String gameID= getIntent().getStringExtra("GameID");
        final String quarter= getIntent().getStringExtra("Quarter");
        mQuarterTime = quarter;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(home + " vs " + away + " statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        disableNewQuarter(getQuarterId(quarter));

        DetailOnClick dOnClick = new DetailOnClick(home, away, gameID);
        mOvertime.setVisibility(View.GONE);
        mOvertime.setOnClickListener(dOnClick);
        mQuarter4.setOnClickListener(dOnClick);
        mQuarter3.setOnClickListener(dOnClick);
        mQuarter2.setOnClickListener(dOnClick);
        mQuarter1.setOnClickListener(dOnClick);

        retrieveDetails(gameID, home, away, quarter);
    }

}
