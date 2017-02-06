package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takomar on 11/12/16.
 */

public class GamesRetriever extends AsyncTask<String, Integer, List<GameInfo>> {

    private Context mContext;
    private GamesAdaptor mGamesAdaptor;
    private LinearLayout linlaHeaderProgress;

    public GamesRetriever(Context context, GamesAdaptor gamesAdaptor) {
        mContext = context;
        mGamesAdaptor = gamesAdaptor;
        linlaHeaderProgress =
                (LinearLayout)
                        ((Activity) mContext).findViewById(R.id.linlaHeaderProgress);
    }

    @Override
    protected List<GameInfo> doInBackground(String... params) {
        List<GameInfo> gamesToday = new ArrayList<>();

        try {
            String myUrl = "http://stats.nba.com/stats/scoreboardV2?" +
                           "DayOffset=0&LeagueID=00&gameDate=" +
                           params[0];

            StringBuffer buffer = UrlHelper.retrieveJSONBuffer(myUrl);

            if (buffer.length() != 0) {
                JSONObject forecastJson = new JSONObject(buffer.toString());
                JSONArray myResults = forecastJson.getJSONArray("resultSets");
                JSONObject games = myResults.getJSONObject(0);
                String gameHeader = games.getString("name");

                if (gameHeader.equals("GameHeader")) {
                    JSONArray rows = games.getJSONArray("rowSet");
                    for (int i = 0; i < rows.length(); ++i) {
                        GameInfo gameInfo = new GameInfo();
                        JSONArray currentGame = rows.getJSONArray(i);
                        gameInfo.gameID = currentGame.getString(2);
                        String match = currentGame.getString(5);
                        int x = match.indexOf("/");
                        gameInfo.visitorTeam = Helper.CodeNameTeam.get(match.substring(x + 1, x + 4));
                        gameInfo.homeTeam = Helper.CodeNameTeam.get(match.substring(x + 4));

                        gamesToday.add(gameInfo);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("SpoilErr", e.getMessage(), e);
            e.printStackTrace();
        }

        List<Integer> favPositions = new ArrayList<>();
        for (GameInfo game : gamesToday)
            if (Helper.isTeamFavorite(mContext, game.visitorTeam) ||
                Helper.isTeamFavorite(mContext, game.homeTeam))
                favPositions.add(gamesToday.indexOf(game));

        if (!favPositions.isEmpty()) {
            int index = 0;
            for (Integer favPos : favPositions) {
                GameInfo temp = gamesToday.get(favPos);
                gamesToday.set(favPos, gamesToday.get(index));
                gamesToday.set(index, temp);
                index++;
            }
        }
        
        return gamesToday;
    }

    @Override
    protected void onPreExecute() {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute(List<GameInfo> result) {

        mGamesAdaptor.changeDate(result);
        linlaHeaderProgress.setVisibility(View.GONE);
    }

}
