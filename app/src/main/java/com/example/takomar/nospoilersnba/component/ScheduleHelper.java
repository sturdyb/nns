package com.example.takomar.nospoilersnba.component;

import android.content.Context;

import com.example.takomar.nospoilersnba.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by takomar on 12/26/17.
 */

public class ScheduleHelper {

    public static void sortGamesByFav(List<GameInfo> games, Context context) {
        List<Integer> favPositions = new ArrayList<>();

        for (GameInfo game : games)
            if (Helper.isTeamFavorite(context, game.visitorTeam) ||
                    Helper.isTeamFavorite(context, game.homeTeam))
                favPositions.add(games.indexOf(game));

        if (!favPositions.isEmpty()) {
            int index = 0;
            for (Integer favPos : favPositions) {
                GameInfo temp = games.get(favPos);
                games.set(favPos, games.get(index));
                games.set(index, temp);
                index++;
            }
        }
    }
}
