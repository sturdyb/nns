package com.example.takomar.nospoilersnba.component.Retro.Schedule;

import android.util.Log;
import android.view.View;

import com.example.takomar.nospoilersnba.Helper;
import com.example.takomar.nospoilersnba.component.GameInfo;
import com.example.takomar.nospoilersnba.R;
import com.example.takomar.nospoilersnba.SimpleGamesAdaptor;

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
    private final View mRootView;
    private SimpleGamesAdaptor mGamesAdaptor;
    private Date mDate;

    public GamesCallback(Map<Date, List<GameInfo>> gamesByDate,
                         View rootView, Date date,
                         SimpleGamesAdaptor gamesAdaptor) {
        mGamesByDate = gamesByDate;
        mRootView = rootView;
        mDate = date;
        mGamesAdaptor = gamesAdaptor;

        //c'est parti
        mRootView.findViewById(R.id.linlaHeaderProgress).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.noGamesPanel).setVisibility(View.GONE);
    }

    private GameInfo getGameInfo(G g) {
        GameInfo gameInfo = new GameInfo();
        gameInfo.gameID = g.getGid();

        SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-d");
        try {
            gameInfo.gameDate = jsonFormat.parse(g.getGdte());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gameInfo.homeTeam = g.getH().getTn();
        gameInfo.visitorTeam = g.getV().getTn();
        if (!g.getH().getS().isEmpty()) { //could have used regexp
            gameInfo.homePts = Integer.parseInt(g.getH().getS());
            gameInfo.visitorPts = Integer.parseInt(g.getV().getS());
        }

        return gameInfo;
    }
    private void fillSchedule(List<Lscd> lscds) {
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
    private void sortSchedule() {
        for (List<GameInfo> gamesToday : mGamesByDate.values()) {
            List<Integer> favPositions = new ArrayList<>();
            for (GameInfo game : gamesToday)
                if (Helper.isTeamFavorite(mRootView.getContext(), game.visitorTeam) ||
                    Helper.isTeamFavorite(mRootView.getContext(), game.homeTeam))
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

        }



    }
    @Override
    public void onResponse(Call<NbaGames> call, Response<NbaGames> response) {
        fillSchedule(response.body().getLscd());
        sortSchedule();
        mRootView.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);

        List<GameInfo> games = mGamesByDate.get(mDate);
        if(games == null || games.isEmpty())
            mRootView.findViewById(R.id.noGamesPanel).setVisibility(View.VISIBLE);
        else
            mGamesAdaptor.showGames(mGamesByDate.get(mDate), false);
    }

    @Override
    public void onFailure(Call<NbaGames>call, Throwable t) {
        // Log error here since request failed
        Log.e("Test", t.toString());
        mRootView.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
    }
}
