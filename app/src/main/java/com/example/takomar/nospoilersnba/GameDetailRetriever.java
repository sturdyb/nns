package com.example.takomar.nospoilersnba;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
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
    }
    @Override
    protected Map<String, List<PlayerInfo>> doInBackground(String... params) {
        Map<String, List<PlayerInfo>> playersInfo = new HashMap<>();

        try {
            String myUrl = "http://stats.nba.com/stats/boxscoretraditionalv2?EndPeriod=0&EndRange="
                    + params[1]
                    +"&GameID=" + params[0]
                    + "&RangeType=2&Season=2016-17&SeasonType=Regular+Season&StartPeriod=0&StartRange=0";
            URL url = new URL(myUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Referer", "http://stats.nba.com/scores/");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
            }

            if (buffer.length() != 0) {
                JSONObject forecastJson = new JSONObject(buffer.toString());
                JSONArray myResults = forecastJson.getJSONArray("resultSets");
                JSONObject games = myResults.getJSONObject(0);
                String gameHeader = games.getString("name");
                
                if (gameHeader.equals("PlayerStats"))
                {
                    JSONArray rows = games.getJSONArray("rowSet");
                    for (int i = 0; i < rows.length(); ++i) {

                        PlayerInfo player = new PlayerInfo();
                        JSONArray currentGame = rows.getJSONArray(i);
                        player.team = currentGame.getString(2);

                        String name = currentGame.getString(5);
                        int family = name.indexOf(' ');
                        player.name = name.charAt(0) +". " + name.substring(family + 1);

                        player.pos = currentGame.getString(6);
                        player.mins = currentGame.getString(8);
                        player.fgm = currentGame.getInt(9);
                        player.fga = currentGame.getInt(10);
                        player.tgm = currentGame.getInt(12);
                        player.tga = currentGame.getInt(13);
                        player.reb = currentGame.getInt(20);
                        player.ast = currentGame.getInt(21);
                        player.stl = currentGame.getInt(22);
                        player.blk = currentGame.getInt(23);
                        player.to = currentGame.getInt(24);
                        player.pts = currentGame.getInt(26);
                        player.fouls = currentGame.getInt(25);
                        if (playersInfo.get(player.team) == null)
                            playersInfo.put(player.team, new ArrayList<PlayerInfo>());

                        playersInfo.get(player.team).add(player);
                    }
                }
            }


        } catch (IOException e) {
            Log.e("TTTAG", "Error ", e);

        } catch (JSONException e) {
            Log.e("TTTAG", e.getMessage(), e);
            e.printStackTrace();
        }
        return playersInfo;
    }

    protected void onPostExecute(Map<String, List<PlayerInfo>> result) {

        for (List<PlayerInfo> oneTeam : result.values())
        {

            TableLayout namesCol;
            TableLayout boxScore;
            if (oneTeam.get(0).team.equals(mHomeTeam))
            {
                namesCol = mHomeNamesCol;
                boxScore = mHomeDetailTable;
            }
            else
            {
                namesCol = mAwayNamesCol;
                boxScore = mAwayDetailTable;
            }

            int i = 0;
            for(PlayerInfo player : oneTeam)
            {
                TableRow nameRow = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.name_col, null);
                if (player.pos.isEmpty())
                    ((TextView)nameRow.findViewById(R.id.name)).setText(player.name);
                else
                    ((TextView)nameRow.findViewById(R.id.name)).setText(player.name + " - " + player.pos);
                TableRow row = (TableRow) LayoutInflater.from(mContext).inflate(R.layout.detail_line, null);

                ((TextView)row.findViewById(R.id.min)).setText(player.mins);
                ((TextView)row.findViewById(R.id.fgm)).setText(player.fgm + "-" + player.fga);
                ((TextView)row.findViewById(R.id.tgm)).setText(player.tgm + "-" + player.tga);

                ((TextView)row.findViewById(R.id.reb)).setText("" + player.reb);
                ((TextView)row.findViewById(R.id.stl)).setText("" + player.stl);
                ((TextView)row.findViewById(R.id.ass)).setText("" + player.ast);
                ((TextView)row.findViewById(R.id.blk)).setText("" + player.blk);
                ((TextView)row.findViewById(R.id.to)).setText("" + player.to);
                ((TextView)row.findViewById(R.id.pts)).setText("" + player.pts);
                ((TextView)row.findViewById(R.id.fouls)).setText("" + player.fouls);

                if(i % 2 != 0) {
                    nameRow.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
                    row.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
                }
                i++;

                namesCol.addView(nameRow);
                boxScore.addView(row);
            }
        }
        mHomeDetailTable.requestLayout();
        mAwayDetailTable.requestLayout();
        mHomeNamesCol.requestLayout();
        mAwayNamesCol.requestLayout();
    }

}
