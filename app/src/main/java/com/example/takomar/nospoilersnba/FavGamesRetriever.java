package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class FavGamesRetriever extends AsyncTask<String, Integer, List<GameInfo>> {

    private Context mContext;
    private FavGamesAdaptor mGamesAdaptor;
    private LinearLayout linlaHeaderProgress;
    private String mDateTo;
    private String mDateFrom;


    public FavGamesRetriever(Context context, FavGamesAdaptor gamesAdaptor) {
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
            mDateFrom = params[0];
            mDateTo = params[1];
            String myUrl = "http://stats.nba.com/stats/teamgamelog?" +
                    "teamid=1610612744" + //params[0] +
                    "&Season=2016-17&LeagueID=00&SeasonType=Regular+Season&" +
                    "DateFrom=" + mDateFrom +
                    "&DateTo=" + mDateTo;

            StringBuffer buffer = UrlHelper.retrieveJSONBuffer(myUrl);

            if (buffer.length() != 0) {
                JSONObject forecastJson = new JSONObject(buffer.toString());
                JSONArray myResults = forecastJson.getJSONArray("resultSets");
                JSONObject games = myResults.getJSONObject(0);
                String gameHeader = games.getString("name");

                if (gameHeader.equals("TeamGameLog")) {
                    JSONArray rows = games.getJSONArray("rowSet");
                    for (int i = 0; i < rows.length(); ++i) {
                        GameInfo gameInfo = new GameInfo();
                        JSONArray currentGame = rows.getJSONArray(i);

                        gameInfo.gameID = currentGame.getString(1);

                        String date = currentGame.getString(2);
                        SimpleDateFormat jsonFormat = new SimpleDateFormat("MMM d, yyyy");
                        gameInfo.gameDate = jsonFormat.parse(date);

                        String match = currentGame.getString(3);
                        String firstTeam = match.substring(match.length() - 3);
                        String secondTeam = match.substring(0, 3);
                        if (match.contains("@"))
                        {
                            gameInfo.visitorTeam = Helper.CodeNameTeam.get(firstTeam);
                            gameInfo.homeTeam = Helper.CodeNameTeam.get(secondTeam);
                        }
                        gameInfo.visitorTeam = Helper.CodeNameTeam.get(secondTeam);
                        gameInfo.homeTeam = Helper.CodeNameTeam.get(firstTeam);

                        gamesToday.add(gameInfo);
                    }
                }
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
    protected void onPreExecute() {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute(List<GameInfo> result) {

        // mGamesAdaptor.changeDate(result);
        //  try {
//            Date lastDate = UrlHelper.dateFormatUrl.parse(mDateTo);
//            Date lastRetrievedDate = result.isEmpty() ?
//                    UrlHelper.dateFormatUrl.parse(mDateFrom) :
//                    result.get(0).gameDate;
//            while (lastDate.compareTo(lastRetrievedDate) > 0)
//            {
//                Calendar c = Calendar.getInstance();
//                c.setTime(lastRetrievedDate);
//                c.add(Calendar.DATE, 1);
//                lastRetrievedDate = c.getTime();
//                //new TeamGamesRetriever(mContext, mGamesAdaptor).executeOnExecutor(THREAD_POOL_EXECUTOR,
//                   //     UrlHelper.dateFormatUrl.format(lastRetrievedDate), "1610612744");
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        linlaHeaderProgress.setVisibility(View.GONE);
        //}
    }
}
