package com.example.takomar.nospoilersnba.component.Retro;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.takomar.nospoilersnba.GameInfo;
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
        return gameInfo;
    }

    @Override
    public void onResponse(Call<NbaGames> call, Response<NbaGames> response) {
        Log.d("Test", "Number of games received: " );
        List<Lscd> lscds = response.body().getLscd();
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
        mRootView.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
        mGamesAdaptor.showGames(mGamesByDate.get(mDate), false);
        //Toast.makeText(getApplicationContext(), "All future games loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<NbaGames>call, Throwable t) {
        // Log error here since request failed
        Log.e("Test", t.toString());
        mRootView.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
    }
}
