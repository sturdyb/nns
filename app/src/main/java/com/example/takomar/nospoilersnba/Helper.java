package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.core.deps.guava.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by takomar on 15/01/17.
 *
 .put("ATL", 	"Atlanta Hawks")
 .put("BKN", 	"Brooklyn Nets")
 .put("BOS", 	"Boston Celtics")
 .put("CHA", 	"Charlotte Hornets")
 .put("CHI", 	"Chicago Bulls")
 .put("CLE", 	"Cleveland Cavaliers")
 .put("DAL", 	"Dallas Mavericks")
 .put("DEN", 	"Denver Nuggets")
 .put("DET", 	"Detroit Pistons")
 .put("GSW", 	"Golden State Warriors")
 .put("HOU", 	"Houston Rockets")
 .put("IND", 	"Indiana Pacers")
 .put("LAC", 	"Los Angeles Clippers")
 .put("LAL", 	"Los Angeles Lakers")
 .put("MEM", 	"Memphis Grizzlies")
 .put("MIA", 	"Miami Heat")
 .put("MIL", 	"Milwaukee Bucks")
 .put("MIN", 	"Minnesota Timberwolves")
 .put("NOP", 	"New Orleans Pelicans")
 .put("NYK", 	"New York Knicks")
 .put("OKC", 	"Oklahoma City Thunder")
 .put("ORL", 	"Orlando Magic")
 .put("PHI", 	"Philadelphia 76ers")
 .put("PHX", 	"Phoenix Suns")
 .put("POR", 	"Portland Trail Blazers")
 .put("SAC", 	"Sacramento Kings")
 .put("SAS", 	"San Antonio Spurs")
 .put("TOR", 	"Toronto Raptors")
 .put("UTA", 	"Utah Jazz")
 .put("WAS", 	"Washington Wizards")
 */


public class Helper {

    static final Map<String, String> CodeNameTeam = ImmutableMap.<String, String>builder()
            .put("ATL", 	"Hawks")
            .put("BKN", 	"Nets")
            .put("BOS", 	"Celtics")
            .put("CHA", 	"Hornets")
            .put("CHI", 	"Bulls")
            .put("CLE", 	"Cavaliers")
            .put("DAL", 	"Mavericks")
            .put("DEN", 	"Nuggets")
            .put("DET", 	"Pistons")
            .put("GSW", 	"Warriors")
            .put("HOU", 	"Rockets")
            .put("IND", 	"Pacers")
            .put("LAC", 	"Clippers")
            .put("LAL", 	"Lakers")
            .put("MEM", 	"Grizzlies")
            .put("MIA", 	"Heat")
            .put("MIL", 	"Bucks")
            .put("MIN", 	"Timberwolves")
            .put("NOP", 	"Pelicans")
            .put("NYK", 	"Knicks")
            .put("OKC", 	"Thunder")
            .put("ORL", 	"Magic")
            .put("PHI", 	"76ers")
            .put("PHX", 	"Suns")
            .put("POR", 	"Trail Blazers")
            .put("SAC", 	"Kings")
            .put("SAS", 	"Spurs")
            .put("TOR", 	"Raptors")
            .put("UTA", 	"Jazz")
            .put("WAS", 	"Wizards")
            .build();

    static public boolean isTeamFavorite(Context context, String team) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String favTeam = sharedPref.getString("favTeam","");

        boolean isFavCode = team.equals(CodeNameTeam.get(favTeam.toUpperCase()));

        return isFavCode || team.equalsIgnoreCase(favTeam);
    }
}
