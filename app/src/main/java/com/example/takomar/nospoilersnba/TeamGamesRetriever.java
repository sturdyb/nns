package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.DialerFilter;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class TeamGamesRetriever extends AsyncTask<Date, Integer, List<GameInfo>> {

    private Context mContext;
    private FavGamesAdaptor mGamesAdaptor;
    private Date mDate;
    private boolean mForCache;
    private  String myUrl;
    public TeamGamesRetriever(Context context, FavGamesAdaptor gamesAdaptor, boolean forCache) {
        mContext = context;
        mGamesAdaptor = gamesAdaptor;
        mForCache =forCache;
    }

    @Override
    protected List<GameInfo> doInBackground(Date... params) {
        List<GameInfo> gamesToday = new ArrayList<>();
        try {
            mDate = params[0];
            Log.v("SpoilDbg", "Start " + UrlHelper.dateFormatUrl.format(mDate));
            myUrl = "http://stats.nba.com/stats/scoreboardV2?" +
                           "DayOffset=0&LeagueID=00&gameDate=" +
                             UrlHelper.dateFormatUrl.format(mDate);
           // Log.v("SpoilDbg", "Start " + UrlHelper.dateFormatUrl.format(mDate));
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

                        if (Helper.isTeamFavorite(mContext, gameInfo.visitorTeam) ||
                            Helper.isTeamFavorite(mContext, gameInfo.homeTeam))
                                gamesToday.add(gameInfo);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("SpoilErr", e.getMessage(), e);
            e.printStackTrace();
        }
        
        return gamesToday;
    }

    @Override
    protected void onPostExecute(List<GameInfo> result) {

        if (mForCache)
            mGamesAdaptor.fillCache(result, mDate);
        else {
            mGamesAdaptor.addGames(result, mDate);
        }
      //  Log.v("SpoilDbg", "End" + UrlHelper.dateFormatUrl.format(mDate));
    }

}
