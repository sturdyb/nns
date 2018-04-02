package com.example.takomar.nospoilersnba.component;

import android.content.Context;

import java.util.List;

/**
 * Created by takomar on 18/03/17.
 */
public interface IStandingsViewUpdater {
    Context getContext();
    void loading();
    void loaded();
    void fillEastStandings(List<TeamStanding> standings);
    void fillWestStandings(List<TeamStanding> standings);
}
