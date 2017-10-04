package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.takomar.nospoilersnba.component.IRetrieveExecutorStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class SimpleGamesRetriever extends AsyncTask<Date, Integer, List<GameInfo>> {
    protected SimpleDateFormat dateFormatUrl = new SimpleDateFormat("MM/d/yyyy");
    protected Context mContext;
    private IRetrieveExecutorStrategy mStrategy;
    protected Date mDate;

    public SimpleGamesRetriever(Context context, IRetrieveExecutorStrategy strategy) {
        mContext = context;
        mStrategy = strategy;
    }

    private GameInfo fillGameInfo(JSONArray currentGame) throws JSONException, ParseException {
        GameInfo gameInfo = new GameInfo();
        gameInfo.gameID = currentGame.getString(2);

        String match = currentGame.getString(5);
        int x = match.indexOf("/");
        gameInfo.visitorTeam = Helper.CodeNameTeam.get(match.substring(x + 1, x + 4));
        gameInfo.homeTeam = Helper.CodeNameTeam.get(match.substring(x + 4));
        gameInfo.gameDate = mDate;
        return gameInfo.visitorTeam == null || gameInfo.homeTeam == null ? null : gameInfo;
    }

    private void fillDetails(List<GameInfo> games, JSONArray teamDetails) throws JSONException {
        if(teamDetails.optInt(22) == 0) //game not started yet
            return ;

        GameInfo key = new GameInfo();
        key.gameID = teamDetails.getString(2);
        GameInfo game = games.get(games.indexOf(key));

        if (game != null) {
            String teamAbbr = teamDetails.getString(4);
            if (Helper.CodeNameTeam.get(teamAbbr).equals(game.homeTeam))
                game.homePts = teamDetails.getInt(22);
            else
                game.visitorPts = teamDetails.getInt(22);

            //Log.v("Spoilwtf", game.firstTeam + " " + game.homePts + " " +
              //                game.secondTeam + " " + game.visitorPts);
        }

    }

    protected  List<GameInfo> retrieveGames(Date date, boolean retrieveDetails) {
        List<GameInfo> gamesToday = new ArrayList<>();

        try {
            String myUrl = "http://stats.nba.com/stats/scoreboardV2?" +
                    "DayOffset=0&LeagueID=00&gameDate=" +
                    dateFormatUrl.format(date);

            Log.v("SpoilUrl", myUrl);
            if(isCancelled()) {
                Log.v("SpoilUrl", myUrl + "cancelled");
                return null;
            }

            StringBuffer buffer = UrlHelper.retrieveJSONBuffer(myUrl);
            if (buffer.length() == 0)
                return null;

            JSONObject forecastJson = new JSONObject(buffer.toString());
            JSONArray myResults = forecastJson.getJSONArray("resultSets");
            JSONObject games = myResults.getJSONObject(0);
            String gameHeader = games.getString("name");

            if (gameHeader.equals("GameHeader")) {
                JSONArray rows = games.getJSONArray("rowSet");
                for (int i = 0; i < rows.length(); ++i) {

                    GameInfo gameInfo = fillGameInfo(rows.getJSONArray(i));
                    if (gameInfo != null)
                        gamesToday.add(gameInfo);
                }


            }

            if (retrieveDetails) {
                JSONObject gamesDetails = myResults.getJSONObject(1);
                gameHeader = gamesDetails.getString("name");
                if (gameHeader.equals("LineScore")) {
                    JSONArray rows = gamesDetails.getJSONArray("rowSet");
                    for (int i = 0; i < rows.length(); ++i)
                        fillDetails(gamesToday, rows.getJSONArray(i));
                }
            }

        } catch (JSONException e) {
            Log.e("SpoilErr", e.getMessage(), e);
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gamesToday;
    }

    @Override
    protected List<GameInfo> doInBackground(Date... params) {

        mDate = (Date) params[0].clone();
        //Log.v("Spoilwtf", "Daily " + this.toString() + " date " + dateFormatUrl.format(mDate) + (mForCache ? " y" : " n"));
        List<GameInfo> gamesToday = retrieveGames(mDate, true);
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
        mStrategy.preExecute();
    }

    protected void onPostExecute(List<GameInfo> result) {
            mStrategy.postExecute(result, mDate, isCancelled());
    }

}
