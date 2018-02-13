package com.example.takomar.nospoilersnba.component.Retro.Game;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.takomar.nospoilersnba.Helper;
import com.example.takomar.nospoilersnba.R;
import com.example.takomar.nospoilersnba.component.GameInfo;
import com.example.takomar.nospoilersnba.component.IBoxScoreViewUpdater;
import com.example.takomar.nospoilersnba.component.IGamesViewUpdater;
import com.example.takomar.nospoilersnba.component.PlayerInfo;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.G;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.Lscd;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.NbaGames;
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

public class LiveBoxScoreCallback implements Callback<GameDetails> {

    private IBoxScoreViewUpdater m_BoxScoreUpdater;
    public LiveBoxScoreCallback(IBoxScoreViewUpdater iBoxScoreViewUpdater){
        m_BoxScoreUpdater = iBoxScoreViewUpdater;

        m_BoxScoreUpdater.clearViews();
        m_BoxScoreUpdater.getProgressBar().setVisibility(View.VISIBLE);
    }

    private List<PlayerInfo> fillInfo(List<Pstsg> playerStats) {
        List<PlayerInfo> playerInfos = new ArrayList<>();
        for(Pstsg playerStat : playerStats) {
            if (playerStat.getMin() == 0 && playerStat.getSec() == 0)
                continue;
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.pos = playerStat.getPos();
            playerInfo.name = playerStat.getFn().charAt(0) +". " + playerStat.getLn();
            playerInfo.mins = playerStat.getMin().toString() + ":" + playerStat.getSec().toString();
            playerInfo.pts = playerStat.getPts();
            playerInfo.ast = playerStat.getAst();
            playerInfo.blk = playerStat.getBlk();
            playerInfo.reb = playerStat.getReb();
            playerInfo.to = playerStat.getTov();
            playerInfo.fga = playerStat.getFga();
            playerInfo.fgm = playerStat.getFgm();
            playerInfo.tga = playerStat.getTpa();
            playerInfo.tgm = playerStat.getTpm();
            playerInfo.fouls = playerStat.getPf();
            playerInfo.plusMinus = playerStat.getPm();
            playerInfos.add(playerInfo);
        }
        return playerInfos;
    }

    @Override
    public void onResponse(Call<GameDetails> call, Response<GameDetails> response) {
        List<PlayerInfo> playersAway = fillInfo(response.body().getG().getVls().getPstsg());
        List<PlayerInfo> playersHome = fillInfo(response.body().getG().getHls().getPstsg());
        Button live = (Button) m_BoxScoreUpdater.getLivePanel().findViewById(R.id.live);
        live.setText(response.body().getG().getCl() + " remaining of the " +
                     response.body().getG().getStt());
        live.setAllCaps(false);

        m_BoxScoreUpdater.createHomeHeaders();
        m_BoxScoreUpdater.createAwayHeaders();
        m_BoxScoreUpdater.fillHomeStats(playersHome);
        m_BoxScoreUpdater.fillAwayStats(playersAway);
        m_BoxScoreUpdater.getProgressBar().setVisibility(View.GONE);
        m_BoxScoreUpdater.requestLayouts();

    }

    @Override
    public void onFailure(Call<GameDetails>call, Throwable t) {
        // Log error here since request failed
        Log.e("Test", t.toString());
        m_BoxScoreUpdater.createHomeHeaders();
        m_BoxScoreUpdater.createAwayHeaders();
        m_BoxScoreUpdater.getProgressBar().setVisibility(View.GONE);
        m_BoxScoreUpdater.requestLayouts();
    }
}
