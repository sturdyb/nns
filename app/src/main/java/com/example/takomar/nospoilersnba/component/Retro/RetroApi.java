package com.example.takomar.nospoilersnba.component.Retro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by takomar on 9/24/17.
 */

public class RetroApi {
    //    http://stats.nba.com/stats/boxscoresummaryv2?GameID=0021600590
    public static final String BASE_URL = "https://data.nba.com/data/10s/v2015/json/mobile_teams/nba/2017/league/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
