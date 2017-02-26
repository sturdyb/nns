package com.example.takomar.nospoilersnba;

import java.util.Date;

/**
 * Created by takomar on 11/12/16.
 */

public class GameInfo {
    protected String homeTeam;
    protected String visitorTeam;
    protected String gameID;
    protected Date gameDate;
    protected int homePts;
    protected int visitorPts;

    @Override
    public boolean equals(Object o) {
        return gameID.equals(((GameInfo)o).gameID);
    }
}
