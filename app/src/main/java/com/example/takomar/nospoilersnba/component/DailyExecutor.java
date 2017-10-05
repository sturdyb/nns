package com.example.takomar.nospoilersnba.component;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.takomar.nospoilersnba.MainFragmentActivity;
import com.example.takomar.nospoilersnba.R;
import com.example.takomar.nospoilersnba.SimpleGamesAdaptor;

import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 19/03/17.
 */

public class DailyExecutor implements IRetrieveExecutorStrategy {
    private final MainFragmentActivity mActivity;
    private final View mRootView;
    private SimpleGamesAdaptor gamesAdaptor;

    public DailyExecutor(MainFragmentActivity activity,
                         View mRootView,
                         SimpleGamesAdaptor gamesAdaptor) {
        mActivity = activity;
        this.mRootView = mRootView;
        this.gamesAdaptor = gamesAdaptor;
    }

    @Override
    public void preExecute() {
        //mRootView.findViewById(R.id.linlaHeaderProgress).setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(List<GameInfo> games, Date date, boolean isCancelled) {
        if (games != null && !games.isEmpty() && !isCancelled) {

            gamesAdaptor.showGames(games, false);
            mActivity.addGamesByDate(games, date);

            mRootView.findViewById(R.id.noGamesPanel).setVisibility(View.GONE);
        }
        ((SwipeRefreshLayout) mRootView.findViewById(R.id.swipeContainer)).setRefreshing(false);
    }


}
