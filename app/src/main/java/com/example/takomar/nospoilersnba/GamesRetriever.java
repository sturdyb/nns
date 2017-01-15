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
import java.util.List;

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
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            String myUrl =
                    "http://stats.nba.com/stats/scoreboardV2?DayOffset=0&LeagueID=00&gameDate=" +
                            params[0];
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Referer", "http://stats.nba.com/scores/");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            }

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
                        gameInfo.visitorTeam = match.substring(x + 1, x + 4);
                        gameInfo.homeTeam = match.substring(x + 4);

                        gamesToday.add(gameInfo);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("TTTAG", "Error ", e);
        } catch (JSONException e) {
            Log.e("TTTAG", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("TTTAG", "Error closing stream", e);
                }
            }
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String favTeam = sharedPref.getString("favTeam", "");

        int favPosition = 0;
        for (GameInfo game : gamesToday)
            if (game.visitorTeam.equals(favTeam) || game.homeTeam.equals(favTeam)) {
                favPosition = gamesToday.indexOf(game);
                break;
            }
        if (favPosition > 0) {
            GameInfo temp = gamesToday.get(0);
            gamesToday.set(0, gamesToday.get(favPosition));
            gamesToday.set(favPosition, temp);
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
