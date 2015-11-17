package com.carrotcreative.recyclercore_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.carrotcreative.recyclercore.adapter.RecyclerCoreAdapter;
import com.carrotcreative.recyclercore.widget.ProgressRecyclerViewLayout;
import com.carrotcreative.recyclercore_example.net.github.Github;
import com.carrotcreative.recyclercore_example.net.github.models.GithubUser;
import com.carrotcreative.recyclercore_example.recyclerviews.models.UserListRecyclerModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
{
    private ProgressRecyclerViewLayout mRecyclerViewLayout;
    private RecyclerCoreAdapter mRecyclerCoreAdapter;
    private ArrayList<Object> mRecyclerCoreModels = new ArrayList<>();
    private Button mTryAgainButton;
    private Button mLoadAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerViewLayout = (ProgressRecyclerViewLayout) findViewById(R.id.recycler_view_layout);

        /**
         * Error View
         */
        View errorView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.element_error_state, null, false);
        mRecyclerViewLayout.setErrorView(errorView);

        mTryAgainButton = (Button) errorView.findViewById(R.id.try_again);
        mTryAgainButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRecyclerViewLayout.setErrorStateEnabled( ! mRecyclerViewLayout.isErrorStateEnabled());
                loadData();
            }
        });

        /**
         * Empty View
         */
        View emptyView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.element_empty_state, null, false);
        mRecyclerViewLayout.setEmptyStateView(emptyView);
        mLoadAgain = (Button) emptyView.findViewById(R.id.load_again);
        mLoadAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_error:
                mRecyclerViewLayout.setErrorStateEnabled(true);
                return true;

            case R.id.action_empty:
            {
                if(! mRecyclerCoreModels.isEmpty())
                {
                    mRecyclerCoreModels.clear();
                    mRecyclerCoreAdapter.notifyDataSetChanged();
                }
                return true;
            }
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadData();
    }

    private void loadData()
    {
        loadUsers("carrot");
    }

    private void loadUsers(String organization)
    {
        Github.api().getUsers(
                organization,
                new Callback<GithubUser[]>()
                {
                    @Override
                    public void success(GithubUser[] githubUsers, Response response)
                    {
                        prepareUsers(githubUsers);
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        mRecyclerViewLayout.setErrorStateEnabled(true);
                    }
                }
        );
    }

    private void prepareUsers(GithubUser[] githubUsers)
    {
        mRecyclerCoreModels.clear();
        // Converting all GithubUser objects
        for(GithubUser githubUser : githubUsers)
        {
            mRecyclerCoreModels.add(
                    new UserListRecyclerModel()
                            .setGithubUser(githubUser)
            );
        }

        if(mRecyclerCoreAdapter == null)
        {
            mRecyclerCoreAdapter = new RecyclerCoreAdapter(mRecyclerCoreModels);
            mRecyclerViewLayout.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerViewLayout.setAdapter(mRecyclerCoreAdapter);
        }
        else
        {
            mRecyclerCoreAdapter.notifyDataSetChanged();
        }
    }
}
