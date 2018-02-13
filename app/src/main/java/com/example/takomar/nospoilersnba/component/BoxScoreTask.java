package com.example.takomar.nospoilersnba.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.takomar.nospoilersnba.Helper;
import com.example.takomar.nospoilersnba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;

/**
 * Created by takomar on 11/12/16.
 */

public class BoxScoreTask extends AsyncTask<String, Integer, Map<String, List<PlayerInfo>> > {

    private String mRange;
    private IBoxScoreViewUpdater m_BoxScoreUpdater;
    private String mHomeTeam;
    private int mTotalsPtsHome;
    private int mTotalsPtsAway;
    public BoxScoreTask(IBoxScoreViewUpdater iBoxScoreViewUpdater, String home){
        m_BoxScoreUpdater = iBoxScoreViewUpdater;
        mHomeTeam = home;
    }

    private PlayerInfo createPlayerInfo(JSONArray playerJSON) throws JSONException {
        PlayerInfo player = new PlayerInfo();
        player.team = Helper.getTeamByCode(playerJSON.getString(2));

        String name = playerJSON.getString(5);
        int family = name.indexOf(' ');
        player.name = name.charAt(0) +". " + name.substring(family + 1);

        player.pos = playerJSON.getString(6);
        player.mins = playerJSON.getString(8);
        player.fgm = playerJSON.getInt(9);
        player.fga = playerJSON.getInt(10);
        player.tgm = playerJSON.getInt(12);
        player.tga = playerJSON.getInt(13);
        player.reb = playerJSON.getInt(20);
        player.ast = playerJSON.getInt(21);
        player.stl = playerJSON.getInt(22);
        player.blk = playerJSON.getInt(23);
        player.to = playerJSON.getInt(24);
        player.pts = playerJSON.getInt(26);
        player.fouls = playerJSON.getInt(25);
        player.plusMinus = playerJSON.getInt(27);

        return  player;
    }

    @Override
    protected Map<String, List<PlayerInfo>> doInBackground(String... params) {
        Map<String, List<PlayerInfo>> playersInfo = new HashMap<>();

        android.os.Process.setThreadPriority(THREAD_PRIORITY_MORE_FAVORABLE);
        try {
            mRange = params[1];
            String gameId = params[0];
            String seasonType = "Regular+Season";
            if (gameId.startsWith("003"))
                seasonType = "All+Star";
            else if (gameId.startsWith("001"))
                seasonType = "Pre+Season";
            String myUrl =
                    "http://stats.nba.com/stats/boxscoretraditionalv2?" +
                    "EndPeriod=0&EndRange=" + params[1]
                    + "&GameID=" + gameId
                    + "&RangeType=2&Season=2017-18"
                    + "&SeasonType=" + seasonType
                    + "&StartPeriod=0&StartRange=0";
            Log.v("SpoilDtl", myUrl);
            StringBuffer buffer = UrlHelper.retrieveJSONBuffer(myUrl);

            if (buffer.length() != 0) {
                JSONObject forecastJson = new JSONObject(buffer.toString());
                JSONArray myResults = forecastJson.getJSONArray("resultSets");
                JSONObject games = myResults.getJSONObject(0);
                String gameHeader = games.getString("name");

                if (gameHeader.equals("PlayerStats"))
                {
                    JSONArray rows = games.getJSONArray("rowSet");
                    for (int i = 0; i < rows.length(); ++i) {
                        PlayerInfo player = createPlayerInfo(rows.getJSONArray(i));
                        if (playersInfo.get(player.team) == null)
                            playersInfo.put(player.team, new ArrayList<PlayerInfo>());

                        playersInfo.get(player.team).add(player);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("SpoilErr", e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playersInfo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        m_BoxScoreUpdater.clearViews();
        m_BoxScoreUpdater.getProgressBar().setVisibility(View.VISIBLE);
    }


    protected void onPostExecute(Map<String, List<PlayerInfo>> result) {
        m_BoxScoreUpdater.createHomeHeaders();
        m_BoxScoreUpdater.createAwayHeaders();

        for (List<PlayerInfo> oneTeam : result.values())
        {
            boolean isHome = oneTeam.get(0).team.equals(mHomeTeam);
            if (isHome)
                mTotalsPtsHome = m_BoxScoreUpdater.fillHomeStats(oneTeam);
            else
                mTotalsPtsAway = m_BoxScoreUpdater.fillAwayStats(oneTeam);
        }

        if(mTotalsPtsHome > 0 && mTotalsPtsHome == mTotalsPtsAway && mRange.compareTo(m_BoxScoreUpdater.getContext().getString(R.string.q4time)) >= 0)
            m_BoxScoreUpdater.showOverTime();

        int quarter = (Integer.parseInt(mRange) - 1) / 7200 + 1; // -1 on range to always add 1 (even at end)
        int diff = Integer.parseInt(mRange) % 7200;
        int time = diff == 0 ? 0 : 12 - diff / 600;
        Button live = (Button) m_BoxScoreUpdater.getLivePanel().findViewById(R.id.live);
        live.setText(time + ":00" + " remaining of the " + quarter + " quarter");

        m_BoxScoreUpdater.getProgressBar().setVisibility(View.GONE);
        m_BoxScoreUpdater.requestLayouts();

    }
}
