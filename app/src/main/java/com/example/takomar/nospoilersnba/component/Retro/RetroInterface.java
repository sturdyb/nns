package com.example.takomar.nospoilersnba.component.Retro;

import com.example.takomar.nospoilersnba.component.Retro.Game.GameDetails;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.NbaGames;
import com.example.takomar.nospoilersnba.component.Retro.Standings.Standings;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by takomar on 9/24/17.
 */

public interface RetroInterface {
        @Headers("User-Agent: http://stats.nba.com/scores/")
        @GET("data/v2015/json/mobile_teams/nba/2017/league/00_full_schedule_week.json")
        Call<NbaGames> getAllGames();

        @Headers("User-Agent: http://stats.nba.com/scores/")
        @GET("data/v2015/json/mobile_teams/nba/2017/scores/gamedetail/{gameId}_gamedetail.json")
        Call<GameDetails> getGameDetails(@Path("gameId") String gameId);

        @Headers("User-Agent: http://stats.nba.com/scores/")
        @GET("prod/v1/{date}/standings_conference.json")
        Call<Standings> getStandings(@Path("date") String date);
    }
