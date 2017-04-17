package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.core.deps.guava.collect.ImmutableMap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
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
    static final String[] MenuItems = { "All games", "Favorite teams", "Standings", "Settings"};
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
            .put("EST",     "East")
            .put("WST",     "West")
            .build();
    static final Map<String, String> IdNameTeam = ImmutableMap.<String, String>builder()
            .put("1610612737", 	"Hawks")
            .put("1610612751", 	"Nets")
            .put("1610612738", 	"Celtics")
            .put("1610612766", 	"Hornets")
            .put("1610612741", 	"Bulls")
            .put("1610612739", 	"Cavaliers")
            .put("1610612742", 	"Mavericks")
            .put("1610612743", 	"Nuggets")
            .put("1610612765", 	"Pistons")
            .put("1610612744", 	"Warriors")
            .put("1610612745", 	"Rockets")
            .put("1610612754", 	"Pacers")
            .put("1610612746", 	"Clippers")
            .put("1610612747", 	"Lakers")
            .put("1610612763", 	"Grizzlies")
            .put("1610612748", 	"Heat")
            .put("1610612749", 	"Bucks")
            .put("1610612750", 	"Timberwolves")
            .put("1610612740", 	"Pelicans")
            .put("1610612752", 	"Knicks")
            .put("1610612760", 	"Thunder")
            .put("1610612753", 	"Magic")
            .put("1610612755", 	"76ers")
            .put("1610612756", 	"Suns")
            .put("1610612757", 	"Trail Blazers")
            .put("1610612758", 	"Kings")
            .put("1610612759", 	"Spurs")
            .put("1610612761", 	"Raptors")
            .put("1610612762", 	"Jazz")
            .put("1610612764", 	"Wizards")
            .build();

    static public boolean isTeamFavorite(Context context, String team) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String[] favTeams = sharedPref.getString(
                context.getString(R.string.favTeam),
                context.getString(R.string.defaultFav)).split(",");

        for (String favTeam : favTeams)
        {
            boolean isFavCode = team.equals(CodeNameTeam.get(favTeam.toUpperCase()));
            if (isFavCode || team.equalsIgnoreCase(favTeam))
                return true;
        }
        return false;
     }

     static List<GameInfo> retrieveFavoriteTeams(Context context, List<GameInfo> games) {
         List<GameInfo> favGames = new ArrayList<>();
         for (GameInfo gameInfo : games)
             if (Helper.isTeamFavorite(context, gameInfo.visitorTeam) ||
                     Helper.isTeamFavorite(context, gameInfo.homeTeam)) {
                 favGames.add(gameInfo);
             }
             return favGames;
     }

    static public boolean shouldShowScores(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(context.getString(R.string.spoilType), false);
    }

    static public int getQuarter(int quarterTime) {
        double quart = quarterTime / 7200.0;
        if (quart % 1 != 0)
            return 5;
        return (int) quart;
    }

    @NonNull
    static public String getEndPeriod(Context context, int buttonId)
    {
        if (buttonId == R.id.button){
            return context.getString(R.string.q1time);
        }
        if (buttonId == R.id.button2){
            return context.getString(R.string.q2time);
        }
        if (buttonId == R.id.button3){
            return context.getString(R.string.q3time);
        }
        return context.getString(R.string.q4time);
    }

    static public void showGameDetails(
            Context context, SimpleGamesAdaptor.GameInfoHolder gameInfo, int viewId)
    {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("GameID", gameInfo.gameId);
        intent.putExtra("Home", gameInfo.homeTeam.getText());
        intent.putExtra("Away", gameInfo.visitorTeam.getText());
        intent.putExtra("Quarter", getEndPeriod(context, viewId));

        context.startActivity(intent);
    }

}
