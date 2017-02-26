package com.example.takomar.nospoilersnba;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takomar on 11/12/16.
 */

public class GameDetailRetriever extends AsyncTask<String, Integer, Map<String, List<PlayerInfo>> > {

    private TableLayout mAwayNamesCol;
    private TableLayout mAwayDetailTable;
    private String mAwayTeam;
    private TableLayout mHomeNamesCol;
    private TableLayout mHomeDetailTable;
    private String mHomeTeam;
    private Context mContext;
    private String mRange;
    private LinearLayout linlaHeaderProgress;

    public GameDetailRetriever(
            Context context,
            String hometeam, TableLayout homeTl, TableLayout homeNamesCol,
            String awayteam, TableLayout awayTl, TableLayout awayNamesCol){
        mHomeTeam = hometeam;
        mAwayTeam = awayteam;
        mHomeDetailTable = homeTl;
        mAwayDetailTable = awayTl;
        mHomeNamesCol = homeNamesCol;
        mAwayNamesCol = awayNamesCol;
        mContext = context;
        linlaHeaderProgress = (LinearLayout)((Activity) mContext)
                .findViewById(R.id.linlaHeaderProgress);
    }

    private PlayerInfo createPlayerInfo(JSONArray playerJSON) throws JSONException {
        PlayerInfo player = new PlayerInfo();
        player.team = Helper.CodeNameTeam.get(playerJSON.getString(2));

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

        return  player;
    }

    @Override
    protected Map<String, List<PlayerInfo>> doInBackground(String... params) {
        Map<String, List<PlayerInfo>> playersInfo = new HashMap<>();

        try {
            mRange = params[1];
            String gameId = params[0];
            String seasonType = "Regular+Season";
            if (gameId.startsWith("003"))
                seasonType = "All+Star";
            String myUrl =
                    "http://stats.nba.com/stats/boxscoretraditionalv2?" +
                    "EndPeriod=0&EndRange=" + params[1]
                    + "&GameID=" + gameId
                    + "&RangeType=2&Season=2016-17"
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
        }

        return playersInfo;
    }@Override
    protected void onPreExecute() {
        super.onPreExecute();
        mHomeDetailTable.removeAllViews();
        mHomeNamesCol.removeAllViews();
        mAwayDetailTable.removeAllViews();
        mAwayNamesCol.removeAllViews();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
    }

     private void fillStats(PlayerInfo stats, TableLayout namesCol, TableLayout boxScore, boolean isOdd)
    {
        TableRow nameRow = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.name_col, null);

        if(stats.name == null) {
            ((TextView) nameRow.findViewById(R.id.name)).setText("Totals");
            ((TextView) nameRow.findViewById(R.id.name)).setTypeface(Typeface.DEFAULT_BOLD);
        }
        else
            if (stats.pos.isEmpty())
                ((TextView)nameRow.findViewById(R.id.name)).setText(stats.name);
            else
                ((TextView)nameRow.findViewById(R.id.name)).setText(stats.name + " - " + stats.pos);

        TableRow row = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.detail_line, null);

        ((TextView)row.findViewById(R.id.min)).setText(stats.mins);
        ((TextView)row.findViewById(R.id.fgm)).setText(stats.fgm + "-" + stats.fga);
        ((TextView)row.findViewById(R.id.tgm)).setText(stats.tgm + "-" + stats.tga);
        ((TextView)row.findViewById(R.id.reb)).setText("" + stats.reb);
        ((TextView)row.findViewById(R.id.stl)).setText("" + stats.stl);
        ((TextView)row.findViewById(R.id.ass)).setText("" + stats.ast);
        ((TextView)row.findViewById(R.id.blk)).setText("" + stats.blk);
        ((TextView)row.findViewById(R.id.to)).setText("" + stats.to);
        ((TextView)row.findViewById(R.id.pts)).setText("" + stats.pts);
        ((TextView)row.findViewById(R.id.fouls)).setText("" + stats.fouls);

        if(isOdd) {
            nameRow.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
            row.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
        }

        namesCol.addView(nameRow);
        boxScore.addView(row);
    }
    private int getDpInPx(int dp){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private void createHeaders(String teamName, TableLayout namesCol, TableLayout body, boolean isAway)
    {
        TableRow nameHeader = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.name_col, null);
        TextView headerName = ((TextView)nameHeader.findViewById(R.id.name));
        headerName.setTextColor(mContext.getResources().getColor(R.color.cardview_light_background));
        if(isAway)
            nameHeader.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        else
            nameHeader.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));

        int dpAsPixels = getDpInPx(3);
        headerName.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        headerName.setText(teamName);
        headerName.setTypeface(null, Typeface.BOLD);
        namesCol.addView(nameHeader);

        TableRow header = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.header_line, null);
        if(isAway)
            header.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        else
            header.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        ((TextView)header.findViewById(R.id.min)).setText("MIN");
        ((TextView)header.findViewById(R.id.pts)).setText("PTS");
        ((TextView)header.findViewById(R.id.fgm)).setText("FG");
        ((TextView)header.findViewById(R.id.tgm)).setText("3PTS");
        ((TextView)header.findViewById(R.id.reb)).setText("REB");
        ((TextView)header.findViewById(R.id.stl)).setText("STL");
        ((TextView)header.findViewById(R.id.ass)).setText("ASS");
        ((TextView)header.findViewById(R.id.blk)).setText("BLK");
        ((TextView)header.findViewById(R.id.to)).setText("TO");
        ((TextView)header.findViewById(R.id.fouls)).setText("FLS");
        body.addView(header);
    }

    protected void onPostExecute(Map<String, List<PlayerInfo>> result) {
        createHeaders(mHomeTeam, mHomeNamesCol, mHomeDetailTable, false);
        createHeaders(mAwayTeam, mAwayNamesCol, mAwayDetailTable, true);

        int totalsHome = 0;
        int totalsAway = 0;
        for (List<PlayerInfo> oneTeam : result.values())
        {
            TableLayout namesCol;
            TableLayout boxScore;
            boolean isHomeTeam = oneTeam.get(0).team.equals(mHomeTeam);
            if (isHomeTeam) {
                namesCol = mHomeNamesCol;
                boxScore = mHomeDetailTable;
            } else {
                namesCol = mAwayNamesCol;
                boxScore = mAwayDetailTable;
            }
            boolean isOdd = false;
            PlayerInfo totalStats = new PlayerInfo();
            for(PlayerInfo player : oneTeam)
            {
                fillStats(player, namesCol, boxScore, isOdd);
                totalStats.pts += player.pts;
                totalStats.fgm += player.fgm;
                totalStats.fga += player.fga;
                totalStats.tgm += player.tgm;
                totalStats.tga += player.tga;
                totalStats.ast += player.ast;
                totalStats.blk += player.blk;
                totalStats.to  += player.to;
                totalStats.reb += player.reb;
                totalStats.stl += player.stl;
                totalStats.fouls += player.fouls;

                isOdd = !isOdd;
            }
            if (isHomeTeam)
                totalsHome = totalStats.pts;
            else
                totalsAway = totalStats.pts;

            fillStats(totalStats, namesCol, boxScore, isOdd);
        }
        Button overtime = (Button) ((Activity)mContext).findViewById(R.id.overtime);

        if(totalsAway > 0 && totalsAway == totalsHome && mRange.compareTo(mContext.getString(R.string.q4time)) >= 0)
            overtime.setVisibility(View.VISIBLE);

        linlaHeaderProgress.setVisibility(View.GONE);

        mHomeDetailTable.requestLayout();
        mAwayDetailTable.requestLayout();
        mHomeNamesCol.requestLayout();
        mAwayNamesCol.requestLayout();
    }
}
