package com.example.takomar.nospoilersnba.component.Retro;

import com.example.takomar.nospoilersnba.component.Retro.Game.GameDetails;
import com.example.takomar.nospoilersnba.component.Retro.Schedule.NbaGames;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by takomar on 9/24/17.
 */

public interface RetroInterface {
        @Headers("User-Agent: http://stats.nba.com/scores/")
        @GET("league/00_full_schedule_week.json")
        Call<NbaGames> getAllGames();

        @Headers("User-Agent: http://stats.nba.com/scores/")
        @GET("scores/gamedetail/{gameId}_gamedetail.json")
        Call<GameDetails> getGameDetails(@Path("gameId") String gameId);
    }
