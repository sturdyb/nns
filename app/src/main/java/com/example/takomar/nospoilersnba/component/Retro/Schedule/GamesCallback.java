package com.example.takomar.nospoilersnba.component.Retro.Schedule;

import android.util.Log;
import android.view.View;

import com.example.takomar.nospoilersnba.Helper;
import com.example.takomar.nospoilersnba.component.GameInfo;
import com.example.takomar.nospoilersnba.R;
import com.example.takomar.nospoilersnba.component.IGamesViewUpdater;
import com.example.takomar.nospoilersnba.component.ScheduleHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by takomar on 9/28/17.
 */

public class GamesCallback implements Callback<NbaGames> {

    private Map<Date, List<GameInfo>> mGamesByDate = new HashMap<>();
    private IGamesViewUpdater m_gamesViewUpdater;
    private Date mDate;

    public GamesCallback(Date date, IGamesViewUpdater iGamesViewUpdater) {

        m_gamesViewUpdater = iGamesViewUpdater;
        mDate = date;

        //c'est parti
        m_gamesViewUpdater.getProgressBar().setVisibility(View.VISIBLE);
        m_gamesViewUpdater.getNoGamesPanel().setVisibility(View.GONE);
    }

    private GameInfo getGameInfo(G g) throws ParseException {
        GameInfo gameInfo = new GameInfo();

        gameInfo.gameID = g.getGid();
        gameInfo.gameCode = g.getGcode();
        gameInfo.status = Integer.parseInt(g.getSt());
        gameInfo.homeTeam = g.getH().getTn();
        gameInfo.visitorTeam = g.getV().getTn();

        SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-d");
        gameInfo.gameDate = jsonFormat.parse(g.getGdte());

        if (gameInfo.status == 1)
            gameInfo.gameTime = Helper.calculateLocalTime(g.getEtm(),
                                                          "yyyy-MM-dd'T'HH:mm:ss");

        if (!g.getH().getS().isEmpty()) { //could have used regexp
            gameInfo.homePts = Integer.parseInt(g.getH().getS());
            gameInfo.visitorPts = Integer.parseInt(g.getV().getS());
        }

        return gameInfo;
    }
    private void retrieveSchedule(List<Lscd> lscds) throws ParseException {
        for (Lscd lscd : lscds) {
            for( G g : lscd.getMscd().getG()) {
                GameInfo gameInfo = getGameInfo(g);

                List<GameInfo> games = mGamesByDate.get(gameInfo.gameDate);
                if (games == null)
                    games = new ArrayList<>();
                games.add(gameInfo);
                mGamesByDate.put(gameInfo.gameDate, games);
            }
        }
    }
    private void fillSchedule() {
        for (Map.Entry<Date, List<GameInfo>> gamesToday : mGamesByDate.entrySet()) {
            ScheduleHelper.sortGamesByFav(gamesToday.getValue(),
                                          m_gamesViewUpdater.getGamesContext());
            m_gamesViewUpdater.fillGames(gamesToday.getValue(), gamesToday.getKey());
        }

    }
    @Override
    public void onResponse(Call<NbaGames> call, Response<NbaGames> response) {
        try {
            retrieveSchedule(response.body().getLscd());
            fillSchedule();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        m_gamesViewUpdater.getProgressBar().setVisibility(View.GONE);

        List<GameInfo> games = mGamesByDate.get(mDate);
        if(games == null || games.isEmpty())
            m_gamesViewUpdater.getNoGamesPanel().setVisibility(View.VISIBLE);
        else
            m_gamesViewUpdater.displayTodayGames(false);
    }

    @Override
    public void onFailure(Call<NbaGames>call, Throwable t) {
        // Log error here since request failed
        Log.e("Test", t.toString());
        m_gamesViewUpdater.getProgressBar().setVisibility(View.GONE);
    }
}
