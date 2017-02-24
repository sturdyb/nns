package com.example.takomar.nospoilersnba;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.DialerFilter;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by takomar on 11/12/16.
 */

public class TeamGamesRetriever extends GamesRetriever {

    public TeamGamesRetriever(Context context, FavGamesAdaptor gamesAdaptor, boolean forCache) {
        super(context, gamesAdaptor, forCache);
    }

    @Override
    protected List<GameInfo> doInBackground(Date... params) {
        mDate = (Date) params[0].clone();
        Log.v("Spoilwtf", this.toString() + " date " + dateFormatUrl.format(mDate) + (mForCache ? " y" : " n"));
        List<GameInfo> gamesToday = retrieveGames(mDate);
        if (gamesToday == null)
            return null;

        List<GameInfo> favGames = new ArrayList<>();
        for (GameInfo gameInfo : gamesToday)
            if (Helper.isTeamFavorite(mContext, gameInfo.visitorTeam) ||
                    Helper.isTeamFavorite(mContext, gameInfo.homeTeam)) {
                Log.v("Spoilwtf", this.toString() + gameInfo.homeTeam + " " + gameInfo.visitorTeam + " " + gameInfo.gameDate.toString());
                favGames.add(gameInfo);
            }

        return favGames;
    }

    @Override
    protected void onPostExecute(List<GameInfo> result) {

        if (result != null &&!isCancelled())
            if (mForCache)
                mGamesAdaptor.fillCache(result, mDate);
            else
                mGamesAdaptor.addGames(result, mDate);
      //  Log.v("SpoilDbg", "End" + UrlHelper.dateFormatUrl.format(mDate));
    }

}
