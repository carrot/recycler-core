package com.carrotcreative.recyclercore_example.recyclerviews.models;

import com.carrotcreative.recyclercore.annotations.RCModel;
import com.carrotcreative.recyclercore_example.net.github.models.GithubUser;
import com.carrotcreative.recyclercore_example.recyclerviews.controllers.UserListRecyclerController;

@RCModel(controller = UserListRecyclerController.class)
public class UserListRecyclerModel
{
    GithubUser mGithubUser;

    public GithubUser getGithubUser()
    {
        return mGithubUser;
    }

    public UserListRecyclerModel setGithubUser(GithubUser githubUser)
    {
        mGithubUser = githubUser;
        return this;
    }
}
