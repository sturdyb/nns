package com.example.takomar.nospoilersnba.component;

import android.content.Context;
import android.view.View;

import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 18/03/17.
 */
public interface IGamesViewUpdater {
    void fillGames(List<GameInfo> games, Date date);
    void displayTodayGames(boolean showDate);
    Context getGamesContext();
    View getNoGamesPanel();
    View getProgressBar();
    void stopRefreshing();
}
//public interface IRetrieveExecutorStrategy {
//
//    void preExecute();
//    void postExecute(List<GameInfo> games, Date date, boolean isCancelled);
//}
