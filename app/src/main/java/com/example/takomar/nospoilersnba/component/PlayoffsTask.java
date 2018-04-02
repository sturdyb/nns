package com.example.takomar.nospoilersnba.component;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.takomar.nospoilersnba.Helper;
import com.example.takomar.nospoilersnba.PlayOffsActivity;
import com.example.takomar.nospoilersnba.PlayoffsAdaptor;
import com.example.takomar.nospoilersnba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;

/**
 * Created by takomar on 11/12/16.
 */

public class PlayoffsTask extends AsyncTask<String, Integer, List<PlayoffsGame> > {

    private Context mContext;
    private LinearLayout linlaHeaderProgress;
    private PlayoffsAdaptor mPlayoffsAdaptor;

    public PlayoffsTask(Context context, PlayoffsAdaptor playoffsAdaptor){
        mContext = context;
        mPlayoffsAdaptor = playoffsAdaptor;
        linlaHeaderProgress = (LinearLayout)((Activity) mContext)
                .findViewById(R.id.linlaHeaderProgress);
    }

    @Override
    protected List<PlayoffsGame> doInBackground(String... params) {
        List<PlayoffsGame> playoffs = new ArrayList<>();

        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND +
                                             THREAD_PRIORITY_MORE_FAVORABLE);
        try {
            String myUrl = "http://data.nba.net/data/10s/prod/v1/2017/playoffsBracket.json";
            Log.v("SpoilDtl", myUrl);
            StringBuffer buffer = UrlHelper.retrieveJSONBuffer(myUrl);

            if (buffer.length() != 0) {
                JSONObject playoffsJSON = new JSONObject(buffer.toString());
                JSONArray mySeries = playoffsJSON.getJSONArray("series");
                for (int i = 0; i < mySeries.length(); ++i) {
                    JSONObject gameInfo = mySeries.getJSONObject(i);
                    PlayoffsGame game = new PlayoffsGame();
                    game.round = Integer.parseInt(gameInfo.getString("roundNum"));
                    game.conference = gameInfo.getString("confName");
                    game.summary =gameInfo.getString("summaryStatusText");

                    JSONObject topRow = gameInfo.getJSONObject("topRow");
                    game.team1 = Helper.getTeamById(topRow.getString("teamId"));
                    game.team1Seed = topRow.getString("seedNum");
                    game.team1Wins = topRow.getString("wins");
                    JSONObject bottomRow = gameInfo.getJSONObject("bottomRow");
                    game.team2 = Helper.getTeamById(bottomRow.getString("teamId"));
                    game.team2Seed = bottomRow.getString("seedNum");
                    game.team2Wins = bottomRow.getString("wins");

                    if (game.team1 != null && !game.team1.isEmpty())
                        playoffs.add(game);
                }


            }
        } catch (JSONException e) {
            Log.e("SpoilErr", e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playoffs;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
    }



    protected void onPostExecute(List<PlayoffsGame> games) {
        linlaHeaderProgress.setVisibility(View.GONE);
        PlayOffsActivity pActivity = (PlayOffsActivity) mContext;
        pActivity.retrieveInitialDetails(games);

    }
}
