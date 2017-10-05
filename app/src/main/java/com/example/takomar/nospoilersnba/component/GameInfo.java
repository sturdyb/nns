package com.example.takomar.nospoilersnba.component;

import java.util.Date;

/**
 * Created by takomar on 11/12/16.
 */

public class GameInfo {
    public String homeTeam;
    public String visitorTeam;
    public String gameID;
    public Date gameDate;
    public int homePts;
    public int visitorPts;

    @Override
    public boolean equals(Object o) {
        return gameID.equals(((GameInfo)o).gameID);
    }
}
