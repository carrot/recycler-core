package com.carrotcreative.recyclercore_example.recyclerviews.controllers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrotcreative.recyclercore.adapter.RecyclerCoreController;
import com.carrotcreative.recyclercore_example.R;
import com.carrotcreative.recyclercore_example.net.github.models.GithubUser;
import com.carrotcreative.recyclercore_example.recyclerviews.models.UserListRecyclerModel;
import com.squareup.picasso.Picasso;

public class UserListRecyclerController extends RecyclerCoreController<UserListRecyclerModel>
{
    private ImageView mUserImage;
    private TextView mUsername;

    public UserListRecyclerController(View itemView)
    {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View rootView)
    {
        mUserImage = (ImageView) rootView.findViewById(R.id.user_image);
        mUsername = (TextView) rootView.findViewById(R.id.user_name);
    }

    @Override
    public void bind(UserListRecyclerModel model)
    {
        GithubUser githubUser = model.getGithubUser();

        // Text
        mUsername.setText(githubUser.login);

        // Image
        Picasso.with(getContext())
                .load(githubUser.avatar_url)
                .fit()
                .centerCrop()
                .into(mUserImage);
    }
}
