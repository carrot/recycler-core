package com.carrotcreative.recyclercore_example.recyclerviews.models;

import com.carrotcreative.recyclercore.annotations.InjectController;
import com.carrotcreative.recyclercore_example.R;
import com.carrotcreative.recyclercore_example.net.github.models.GithubUser;
import com.carrotcreative.recyclercore_example.recyclerviews.controllers.UserListRecyclerController;

@InjectController(controller = UserListRecyclerController.class, layout = R.layout.element_user_list)
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
