package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by takomar on 11/12/16.
 */

public class GamesRetriever  extends AsyncTask<String, Integer, List<GameInfo>> {

    private Context mContext;
    private RecyclerView mgamesToday;
    private GamesAdaptor mGamesAdaptor;
    public GamesRetriever (Context context, GamesAdaptor gamesAdaptor){
        mContext = context;
        mGamesAdaptor = gamesAdaptor;
    }
    @Override
    protected List<GameInfo> doInBackground(String... params) {
        List<GameInfo> gamesToday = new ArrayList<>();
        try {
            String myUrl =
                    "http://stats.nba.com/stats/scoreboardV2?DayOffset=0&LeagueID=00&gameDate=" +
                            params[0];
            URL url = new URL(myUrl);
            // Create the request to OpenWeatherMap, and open the connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Referer", "http://stats.nba.com/scores/");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
                
                if (gameHeader.equals("GameHeader"))
                {
                    JSONArray rows = games.getJSONArray("rowSet");
                    for (int i = 0; i < rows.length(); ++i) {
                        GameInfo gameInfo = new GameInfo();
                        JSONArray currentGame = rows.getJSONArray(i);
                        gameInfo.gameID = currentGame.getString(2);
                        String match = currentGame.getString(5);
                        int x = match.indexOf("/");
                        gameInfo.visitorTeam = match.substring(x + 1, x + 4);
                        gameInfo.homeTeam = match.substring(x+4);

                        gamesToday.add(gameInfo);
                    }
                }
            }


        } catch (IOException e) {
            Log.e("TTTAG", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e("TTTAG", e.getMessage(), e);
            e.printStackTrace();
        }
        return gamesToday;
    }

    protected void onPostExecute(List<GameInfo> result) {
        mGamesAdaptor.changeDate(result);
    }

}
