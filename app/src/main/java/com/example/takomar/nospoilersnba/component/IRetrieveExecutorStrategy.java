package com.example.takomar.nospoilersnba.component;

import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 18/03/17.
 */

public interface IRetrieveExecutorStrategy {

    void preExecute();
    void postExecute(List<GameInfo> games, Date date, boolean isCancelled);
}
