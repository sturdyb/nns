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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takomar on 11/12/16.
 */

public class GamesRetriever extends AsyncTask<Date, Integer, List<GameInfo>> {
    protected SimpleDateFormat dateFormatUrl = new SimpleDateFormat("MM/d/yyyy");
    protected Context mContext;
    protected GamesAdaptor mGamesAdaptor;
    private LinearLayout linlaHeaderProgress;
    protected Date mDate;
    protected boolean mForCache;

    public GamesRetriever(Context context, GamesAdaptor gamesAdaptor, boolean forCache) {
        mContext = context;
        mGamesAdaptor = gamesAdaptor;
        mForCache = forCache;
        linlaHeaderProgress = (LinearLayout)((Activity) mContext)
                                                .findViewById(R.id.linlaHeaderProgress);
    }

    private GameInfo fillGameInfo(JSONArray currentGame) throws JSONException, ParseException {
        GameInfo gameInfo = new GameInfo();
        gameInfo.gameID = currentGame.getString(2);

        String match = currentGame.getString(5);
        int x = match.indexOf("/");
        gameInfo.visitorTeam = Helper.CodeNameTeam.get(match.substring(x + 1, x + 4));
        gameInfo.homeTeam = Helper.CodeNameTeam.get(match.substring(x + 4));
        gameInfo.gameDate = mDate;
        return  gameInfo;
    }

    protected  List<GameInfo> retrieveGames(Date date) {
        List<GameInfo> gamesToday = new ArrayList<>();

        try {
            String myUrl = "http://stats.nba.com/stats/scoreboardV2?" +
                    "DayOffset=0&LeagueID=00&gameDate=" +
                    dateFormatUrl.format(date);
            StringBuffer buffer = UrlHelper.retrieveJSONBuffer(myUrl);
            if (buffer.length() == 0)
                return null;

            JSONObject forecastJson = new JSONObject(buffer.toString());
            JSONArray myResults = forecastJson.getJSONArray("resultSets");
            JSONObject games = myResults.getJSONObject(0);
            String gameHeader = games.getString("name");

            if (gameHeader.equals("GameHeader")) {
                JSONArray rows = games.getJSONArray("rowSet");
                for (int i = 0; i < rows.length(); ++i)
                    gamesToday.add(fillGameInfo(rows.getJSONArray(i)));
            }

        } catch (JSONException e) {
            Log.e("SpoilErr", e.getMessage(), e);
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gamesToday;
    }

    @Override
    protected List<GameInfo> doInBackground(Date... params) {

        mDate = (Date) params[0].clone();
        Log.v("Spoilwtf", "Daily " + this.toString() + " date " + dateFormatUrl.format(mDate) + (mForCache ? " y" : " n"));
        List<GameInfo> gamesToday = retrieveGames(mDate);
        if (gamesToday == null)
            return null;

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
        if (!mForCache)
            linlaHeaderProgress.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute(List<GameInfo> result) {
     //   Log.v("SpoilDbg", "End");
        if (result != null &&!isCancelled())
            if (mForCache)
                mGamesAdaptor.fillCache(result, mDate);
             else {
                mGamesAdaptor.addGames(result, mDate);
                mGamesAdaptor.notifyDataSetChanged();
                linlaHeaderProgress.setVisibility(View.GONE);
            }
    }

}
