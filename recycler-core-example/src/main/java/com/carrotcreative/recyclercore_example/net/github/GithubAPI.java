package com.carrotcreative.recyclercore_example.net.github;

import com.carrotcreative.recyclercore_example.net.github.models.GithubUser;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GithubAPI
{
    String BASE_URL = "https://api.github.com";

    @GET("/orgs/{organization}/members")
    void getUsers(
            @Path("organization") String organization,
            Callback<GithubUser[]> callback
    );
}
