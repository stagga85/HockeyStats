package com.stats.shottracker.api;

import com.stats.shottracker.models.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Start of network api.
 */

public interface ApiClient {

    @GET("/")
    Call<List<Team>> listAllTeams();

}
