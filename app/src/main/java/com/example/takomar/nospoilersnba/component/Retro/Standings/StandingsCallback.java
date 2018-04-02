package com.example.takomar.nospoilersnba.component.Retro.Standings;

import android.util.Log;

import com.example.takomar.nospoilersnba.Helper;
import com.example.takomar.nospoilersnba.component.IStandingsViewUpdater;
import com.example.takomar.nospoilersnba.component.TeamStanding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by takomar on 4/2/18.
 */

public class StandingsCallback implements Callback<Standings> {
    private final IStandingsViewUpdater mUpdater;

    public StandingsCallback(IStandingsViewUpdater updater)
    {
        mUpdater = updater;

        mUpdater.loading();
    }
    @Override
    public void onResponse(Call<Standings> call, Response<Standings> response) {

        if (response.body() == null)
            return;

        List<TeamStanding> s = convertToStandings(response.body().league.standard.conference.east);
        mUpdater.fillEastStandings(s);
        s = convertToStandings(response.body().league.standard.conference.west);
        mUpdater.fillWestStandings(s);
        mUpdater.loaded();
    }


    private List<TeamStanding> convertToStandings(List<Standing> east) {
        List<TeamStanding> standings = new ArrayList<>();
        for (Standing est : east)
        {
            TeamStanding standing = new TeamStanding();
            standing.teamId = est.teamId;
            standing.losses = est.loss;
            standing.wins = est.win;
            standing.pct = est.winPctV2;
            standing.name = Helper.getTeamById(standing.teamId);

            standings.add(standing);
        }
        return  standings;
    }

    @Override
    public void onFailure(Call<Standings> call, Throwable t) {
        Log.e("Test", t.toString());

    }
}
