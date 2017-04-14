package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by takomar on 11/12/16.
 */

public class Standings extends AsyncTask<Date, Integer, Map<Integer, List<TeamStanding>> > {


    private final View mRootView;
    private final TableLayout mWest;
    private final Context mContext;
    private TableLayout mEast;
    private LinearLayout linlaHeaderProgress;
    private Date mDate;

    public Standings(View rootFragmentView, Context context){
        mContext = context;
        mRootView =rootFragmentView;
        mEast = (TableLayout) mRootView.findViewById(R.id.standingsEast);
        mWest = (TableLayout) mRootView.findViewById(R.id.standingsWest);
        linlaHeaderProgress = (LinearLayout)mRootView.findViewById(R.id.linlaHeaderProgress);
    }

    @Override
    protected Map<Integer, List<TeamStanding>> doInBackground(Date... params) {
        Map<Integer, List<TeamStanding>> playersInfo = new HashMap<>();
        mDate = (Date) params[0].clone();

        Document doc = null;
        try {
            String day   = (String) DateFormat.format("dd",   mDate);
            String month = (String) DateFormat.format("MM",   mDate);
            String year  = (String) DateFormat.format("yyyy", mDate);

            String url = "http://www.basketball-reference.com/friv/standings.fcgi?" +
                         "month=" + month + "&day=" + day +
                         "&year=" + year + "&lg_id=NBA";
            doc = Jsoup.connect(url).get();

            playersInfo.put(0, new ArrayList<TeamStanding>());
            Element table = doc.getElementById("standings_e");
            for (org.jsoup.nodes.Element body : table.getElementsByTag("tbody")) {
                for (org.jsoup.nodes.Element row : body.getElementsByTag("tr")) {
                    TeamStanding teamStd = new TeamStanding();
                    Elements team = row.getElementsByAttributeValue("data-stat", "team_name");
                    if (team.size() == 1) {
                        teamStd.name = team.get(0).getElementsByTag("a").get(0).text();
                        //Log.v("Standings", team.get(0).getElementsByTag("a").get(0).text());
                    }
                    Elements wins = row.getElementsByAttributeValue("data-stat", "wins");
                    teamStd.wins = wins.get(0).text();
                    //Log.v("Standings", wins.get(0).text());
                    Elements losses = row.getElementsByAttributeValue("data-stat", "losses");
                    teamStd.losses = losses.get(0).text();
                    //Log.v("Standings", losses.get(0).text());
                    Elements pct = row.getElementsByAttributeValue("data-stat", "win_loss_pct");
                    teamStd.pct = pct.get(0).text();

                    playersInfo.get(0).add(teamStd);
                }
            }

            playersInfo.put(1, new ArrayList<TeamStanding>());
            table = doc.getElementById("standings_w");
            for (org.jsoup.nodes.Element body : table.getElementsByTag("tbody")) {
                for (org.jsoup.nodes.Element row : body.getElementsByTag("tr")) {
                    TeamStanding teamStd = new TeamStanding();
                    Elements team = row.getElementsByAttributeValue("data-stat", "team_name");
                    if (team.size() == 1) {
                        teamStd.name = team.get(0).getElementsByTag("a").get(0).text();
                        //Log.v("Standings", team.get(0).getElementsByTag("a").get(0).text());
                    }
                    Elements wins = row.getElementsByAttributeValue("data-stat", "wins");
                    teamStd.wins = wins.get(0).text();
                    //Log.v("Standings", wins.get(0).text());
                    Elements losses = row.getElementsByAttributeValue("data-stat", "losses");
                    teamStd.losses = losses.get(0).text();
                    //Log.v("Standings", losses.get(0).text());
                    Elements pct = row.getElementsByAttributeValue("data-stat", "win_loss_pct");
                    teamStd.pct = pct.get(0).text();

                    playersInfo.get(1).add(teamStd);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playersInfo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mEast.removeAllViews();
        mWest.removeAllViews();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
    }
    private void createHeaders(int conf, TableLayout body)
    {
        TableRow header = (TableRow) LayoutInflater.from(mContext)
                                                       .inflate(R.layout.standings_header, null);
        header.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));

        TextView headerName = ((TextView)header.findViewById(R.id.conf));
        headerName.setTextColor(mContext.getResources().getColor(R.color.cardview_light_background));
        headerName.setText(conf == 0 ? "East Conference" : "West Conference");
        headerName.setTypeface(null, Typeface.BOLD);

        ((TextView)header.findViewById(R.id.wins)).setText("Wins");
        ((TextView)header.findViewById(R.id.losses)).setText("Losses");
        ((TextView)header.findViewById(R.id.wlpct)).setText("W-L %");
        body.addView(header);
    }

    private void fillStats(TeamStanding stats, TableLayout body, int line)
    {
       TableRow row = (TableRow) LayoutInflater.from(mContext)
                                                .inflate(R.layout.standings_detail, null);
        if(line == 9)
            body.addView((TableRow) LayoutInflater.from(mContext)
                    .inflate(R.layout.standings_detail, null)); //separation of qualified

        ((TextView)row.findViewById(R.id.team)).setText(stats.name);
        ((TextView)row.findViewById(R.id.wins)).setText(stats.wins);
        ((TextView)row.findViewById(R.id.losses)).setText(stats.losses);
        ((TextView)row.findViewById(R.id.wlpct)).setText(stats.pct);
        body.addView(row);
    }
    protected void onPostExecute(Map<Integer, List<TeamStanding>> result) {
        createHeaders(0, mEast);
        createHeaders(1, mWest);

        int i =0;
        for (TeamStanding tStanding : result.get(0))
            fillStats(tStanding, mEast, ++i);


        i = 0;
        for (TeamStanding tStanding : result.get(1))
            fillStats(tStanding, mWest, ++i);
        linlaHeaderProgress.setVisibility(View.GONE);
        mEast.requestLayout();
        mWest.requestLayout();
    }
}
