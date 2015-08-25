package com.carrotcreative.recyclercore.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.carrotcreative.recyclercore.R;
import com.carrotcreative.recyclercore.adapter.RecyclerCoreAdapter;
import com.carrotcreative.recyclercore.adapter.RecyclerCoreModel;

import java.util.ArrayList;

public class ProgressRecyclerViewLayout extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FrameLayout mEmptyStateContainer;

    public ProgressRecyclerViewLayout(Context context)
    {
        this(context, null);
    }

    public ProgressRecyclerViewLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ProgressRecyclerViewLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        // Inflating the View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.progress_recycler_view, this, true);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        findViews();
        setDefaultLayoutManager();
        setDefaultAdapter();
    }

    private void findViews()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.progress_recycler_view_recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_recycler_view_progress_bar);
        mEmptyStateContainer = (FrameLayout) findViewById(R.id.progress_recycler_view_empty_state_container);
    }

    private void setDefaultLayoutManager()
    {
        LinearLayoutManager manager = new LinearLayoutManager(mRecyclerView.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    private void setDefaultAdapter()
    {
        ArrayList<RecyclerCoreModel> models = new ArrayList<>();
        mRecyclerView.setAdapter(new RecyclerCoreAdapter(models));
    }

    /** Wrapper for {@link android.support.v7.widget.RecyclerView#setLayoutManager} */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager)
    {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /** Wrapper for {@link android.support.v7.widget.RecyclerView#setAdapter} */
    public void setAdapter(RecyclerView.Adapter adapter)
    {
        mRecyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(GONE);
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener scrollListener)
    {
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    public boolean contains(RecyclerView recyclerView)
    {
        if(mRecyclerView == recyclerView)
        {
            return true;
        }

        return false;
    }

    public void scrollRecyclerViewBy(int dx, int dy)
    {
        mRecyclerView.scrollBy(dy, dy);
    }

    public void scrollRecyclerViewToTop()
    {
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    public void scrollRecyclerViewToBottom()
    {
        if(mRecyclerView.getAdapter().getItemCount() > 0)
        {
            mRecyclerView.getLayoutManager().scrollToPosition(mRecyclerView.getAdapter().getItemCount()-1);
        }
    }

    public void stopScroll()
    {
        mRecyclerView.stopScroll();
    }

    // =================================== //
    // ========== Empty States =========== //
    // =================================== //

    public void setEmptyStateView(View emptyStateView)
    {
        mEmptyStateContainer.addView(emptyStateView);
    }

    public void setEmptyStateVisible(boolean visible)
    {
        if(visible)
        {
            mEmptyStateContainer.setVisibility(VISIBLE);
        }
        else
        {
            mEmptyStateContainer.setVisibility(GONE);
        }
    }
}
