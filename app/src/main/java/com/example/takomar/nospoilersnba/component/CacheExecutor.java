package com.example.takomar.nospoilersnba.component;

import com.example.takomar.nospoilersnba.GameInfo;
import com.example.takomar.nospoilersnba.MainFragmentActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 19/03/17.
 */

public class CacheExecutor implements IRetrieveExecutorStrategy {
    private final MainFragmentActivity mActivity;

    public CacheExecutor(MainFragmentActivity activity) {
        mActivity = activity;
    }
    @Override
    public void preExecute() {
    }

    @Override
    public void postExecute(List<GameInfo> games, Date date, boolean isCancelled) {
        if (games != null && !isCancelled)
            mActivity.addGamesByDate(games, date);
    }


}
