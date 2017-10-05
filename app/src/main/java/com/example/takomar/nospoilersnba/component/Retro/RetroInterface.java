package com.example.takomar.nospoilersnba.component.Retro;

import com.example.takomar.nospoilersnba.component.Retro.Schedule.NbaGames;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by takomar on 9/24/17.
 */

public interface RetroInterface {
        @Headers("User-Agent: http://stats.nba.com/scores/")
        @GET("00_full_schedule_week.json")
        Call<NbaGames> getAllGames();

    }
