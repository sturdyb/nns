package com.example.takomar.nospoilersnba.component;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by takomar on 18/03/17.
 */
public interface IBoxScoreViewUpdater {
    Context getContext();
    void createHomeHeaders();
    void createAwayHeaders();
    void clearViews();
    void requestLayouts();
    int fillHomeStats(List<PlayerInfo> oneTeam);
    int fillAwayStats(List<PlayerInfo> oneTeam);
    View getProgressBar();
    View getLivePanel();
    void showOverTime();

}
//public interface IRetrieveExecutorStrategy {
//
//    void preExecute();
//    void postExecute(List<GameInfo> games, Date date, boolean isCancelled);
//}