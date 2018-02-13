package com.example.takomar.nospoilersnba;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.test.espresso.core.deps.guava.collect.ImmutableMap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.takomar.nospoilersnba.component.GameInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    static public String getTeamByCode(String teamCode){
        return CodeNameTeam.get(teamCode);
    }
    static final Map<String, String> imageTeam = ImmutableMap.<String, String>builder()
            .put("Hawks", "@drawable/atl_2018")
            .put("Nets", "@drawable/brk_2018")
            .put("Celtics", "@drawable/bos_2018")
            .put("Hornets", "@drawable/cho_2018")
            .put("Bulls", "@drawable/chi_2018")
            .put("Cavaliers", "@drawable/cle_2018")
            .put("Mavericks", "@drawable/dal_2018")
            .put("Nuggets", "@drawable/den_2018")
            .put("Pistons", "@drawable/det_2018")
            .put("Warriors", "@drawable/gsw_2018")
            .put("Rockets", "@drawable/hou_2018")
            .put("Pacers", "@drawable/ind_2018")
            .put("Clippers", "@drawable/lac_2018")
            .put("Lakers", "@drawable/lal_2018")
            .put("Grizzlies", "@drawable/mem_2018")
            .put("Heat", "@drawable/mia_2018")
            .put("Bucks", "@drawable/mil_2018")
            .put("Timberwolves", "@drawable/min_2018")
            .put("Pelicans", "@drawable/nop_2018")
            .put("Knicks", "@drawable/nyk_2018")
            .put("Thunder", "@drawable/okc_2018")
            .put("Magic", "@drawable/orl_2018")
            .put("76ers", "@drawable/phi_2018")
            .put("Suns", "@drawable/pho_2018")
            .put("Trail Blazers", "@drawable/por_2018")
            .put("Kings", "@drawable/sac_2018")
            .put("Spurs", "@drawable/sas_2018")
            .put("Raptors", "@drawable/tor_2018")
            .put("Jazz", "@drawable/uta_2018")
            .put("Wizards", "@drawable/was_2018")
            .put("East", "@drawable/nba_2018")
            .put("West", "@drawable/nba_2018")
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
            .put("1610612745", 	"Rocketse")
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
    static public String getTeamById(String teamId){
        return IdNameTeam.get(teamId);
    }
    static public boolean isTeamFavorite(Context context, String team) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String[] favTeams = sharedPref.getString(
                context.getString(R.string.favTeam),
                context.getString(R.string.defaultFav)).split(",");

        for (String favTeam : favTeams)
        {
            boolean isFavCode = team.equals(getTeamByCode(favTeam.toUpperCase()));
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

    static public String getTime(int quarter, int minute) {
        int quartTime = quarter * 7200 ;
        int minuteTime = minute * 600;
        Integer overallTime = new Integer(quartTime + minuteTime);
        return overallTime.toString();
    }


    @NonNull
    static public String getEndPeriod(Context context, int buttonId)
    {
        if (buttonId == R.id.q1){
            return getTime(0, 12);
        }
        if (buttonId == R.id.q2){
            return getTime(1, 12);
        }
        if (buttonId == R.id.q3){
            return getTime(2, 12);
        }
        if (buttonId == R.id.q4) {
            return getTime(3, 12);
        }
        return "live";
    }

    static public Dialog createTimeDialog(final Context context)
    {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.time_chooser);
        dialog.setTitle("Choose a time:");
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.time);
        np.setWrapSelectorWheel(false);
        np.setMinValue(1);
        np.setMaxValue(12);
        np.setDisplayedValues(new String[]{
                "11:00","10:00","09:00",
                "08:00","07:00","06:00",
                "05:00","04:00","03:00",
                "02:00","01:00","end"});
        np.setValue(12);
        final NumberPicker qp = (NumberPicker) dialog.findViewById(R.id.quarter);
        qp.setDisplayedValues(
                new String[]{"1st quarter", "2nd quarter", "3rd quarter", "4th quarter"});
        qp.setMaxValue(3);
        qp.setMinValue(0);
        qp.setWrapSelectorWheel(false);

        final Button back = (Button) dialog.findViewById(R.id.goBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });

        return dialog;
    }
    static public void chooseTimeDialog(final Context context,
                                        final GamesAdaptor.GameInfoHolder gameInfo) {

        final Dialog dialog = createTimeDialog(context);
        final Button go = (Button) dialog.findViewById(R.id.go);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.time);
        final NumberPicker qp = (NumberPicker) dialog.findViewById(R.id.quarter);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showGameDetails(context, gameInfo, getTime(qp.getValue(), np.getValue()));
            }
        });

        dialog.show();
    }
    static public void showGameDetails(
            Context context, final GamesAdaptor.GameInfoHolder gameInfo, String quarter)
    {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("GameID", gameInfo.gameId);
        intent.putExtra("Home", gameInfo.homeTeam.getText());
        intent.putExtra("Away", gameInfo.visitorTeam.getText());
        intent.putExtra("Quarter", quarter);

        context.startActivity(intent);
    }

    static public void openWatchNbaSite(Context context, String gameCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://watch.nba.com/game/" + gameCode));
        context.startActivity(intent);
    }

    static public void openHighlights(Context context, String home, String away){
        String query = home + " " + away + " ximo pierto final";

        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }

    static public String calculateLocalTime(String gameDateEst,
                                            String formatPattern) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Date estDate = format.parse(gameDateEst);
        format = new SimpleDateFormat("HH:mm");

        return format.format(estDate);
    }

}
