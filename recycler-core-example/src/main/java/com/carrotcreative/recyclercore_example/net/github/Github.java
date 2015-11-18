package com.carrotcreative.recyclercore_example.net.github;

import retrofit.RestAdapter;

public class Github
{
    // ===== Singleton =====

    public static Github sInstance;

    public static GithubAPI api()
    {
        if(sInstance == null)
        {
            sInstance = new Github();
        }
        return sInstance.getAPI();
    }

    // ===== Class =====

    private GithubAPI mAPI;

    private Github()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GithubAPI.BASE_URL)
                .build();

        mAPI = restAdapter.create(GithubAPI.class);
    }

    private GithubAPI getAPI()
    {
        return mAPI;
    }
}
