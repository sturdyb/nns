package com.example.takomar.nospoilersnba.component;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.takomar.nospoilersnba.Helper;

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

public class GamesByDayTask extends AsyncTask<Date, Integer, List<GameInfo>> {
    private SimpleDateFormat dateFormatUrl = new SimpleDateFormat("MM/d/yyyy");
    private IGamesViewUpdater m_gamesViewUpdater;
    private Date mDate;

    public GamesByDayTask(IGamesViewUpdater iGamesViewUpdater) {
        m_gamesViewUpdater = iGamesViewUpdater;
    }

    private GameInfo fillGameInfo(JSONArray currentGame) throws JSONException, ParseException {
        GameInfo gameInfo = new GameInfo();
        gameInfo.gameID = currentGame.getString(2);
        gameInfo.gameCode = currentGame.getString(5);
        gameInfo.status = currentGame.getInt(3);

        if(gameInfo.status == 1) {
            String gameTimeEST = currentGame.getString(4);
            gameTimeEST = gameTimeEST.substring(0, gameTimeEST.indexOf("m") + 1);
            gameTimeEST = gameTimeEST.toUpperCase();

            gameInfo.gameTime = Helper.calculateLocalTime(
                    dateFormatUrl.format(mDate) + " " + gameTimeEST,
                    "MM/d/yyyy h:mm a");
        }

        String match = gameInfo.gameCode;
        int x = match.indexOf("/");
        gameInfo.visitorTeam = Helper.getTeamByCode(match.substring(x + 1, x + 4));
        gameInfo.homeTeam = Helper.getTeamByCode(match.substring(x + 4));
        gameInfo.gameDate = mDate;
        return gameInfo.visitorTeam == null || gameInfo.homeTeam == null ? null : gameInfo;
    }

    private void fillDetails(List<GameInfo> games, JSONArray teamDetails) throws JSONException {
        if(teamDetails.optInt(22) == 0) //game not started yet
            return ;

        GameInfo key = new GameInfo();
        key.gameID = teamDetails.getString(2);
        int index = games.indexOf(key);
        if (index == -1)
            return;
        GameInfo game = games.get(index);

        if (game != null) {
            String teamAbbr = teamDetails.getString(4);
            if (Helper.getTeamByCode(teamAbbr).equals(game.homeTeam))
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
                           "gameDate=" + dateFormatUrl.format(date) + "&DayOffset=0&LeagueID=00";

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
        List<GameInfo> gamesToday = retrieveGames(mDate, true);

        if (gamesToday == null)
            return null;

        ScheduleHelper.sortGamesByFav(gamesToday, m_gamesViewUpdater.getGamesContext());
        return gamesToday;
    }

    @Override
    protected void onPreExecute() { }

    protected void onPostExecute(List<GameInfo> result) {
        if (result != null && !result.isEmpty() && !isCancelled())
        {
            Toast.makeText(m_gamesViewUpdater.getGamesContext(),
                    "updating...", Toast.LENGTH_SHORT).show();
            m_gamesViewUpdater.fillGames(result, mDate);
            m_gamesViewUpdater.displayTodayGames(false);
            m_gamesViewUpdater.getNoGamesPanel().setVisibility(View.GONE);
        }
        m_gamesViewUpdater.stopRefreshing();
    }

}
