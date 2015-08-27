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
    private RecyclerView.Adapter mAdapter;
    private View mEmptyStateView;

    private boolean mEmptyStateVisible = false;

    /**
     * An observer to listen for changes in the data and check if it needs to display the
     * empty state.
     */
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            setEmptyStateEnabled(mEmptyStateVisible);
        }
    };

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
        mAdapter = adapter;

        /**
         * Register the dataset observer
         */
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(GONE);
        setEmptyStateEnabled(mEmptyStateVisible);
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

    /**
     *
     * @param emptyStateView The view that will be shown then the item count in the adapter is 0
     */
    public void setEmptyStateView(View emptyStateView)
    {
        /**
         * If there is already an emplty state view, replace it.
         */
        if(mEmptyStateView != null)
        {
            mEmptyStateContainer.removeView(mEmptyStateView);
        }
        mEmptyStateView = emptyStateView;
        mEmptyStateContainer.addView(emptyStateView);
    }

    /**
     *
     * @param visible true if you want to show the empty state, false other wise.
     *                This will show the empty state only if the item count in adapter is 0.
     *                If you want to skip item count, use #forceShowEmptyState()
     */
    public void setEmptyStateEnabled(boolean visible)
    {
        mEmptyStateVisible = visible;

        /**
         * If adapter not set do not show the empty state.
         */
        if(mAdapter == null || mAdapter.getItemCount() != 0 || visible == false)
        {
            if(mEmptyStateContainer.getVisibility() == VISIBLE)
            {
                mEmptyStateContainer.setVisibility(GONE);
            }
        }
        else if(mAdapter.getItemCount() == 0)
        {
            showEmptyState();
        }
    }

    /**
     * The empty state is shown if the number of items in the adapter are 0.
     * If you want to force show the Empty state, call this functions.
     * Please keep in mind that when you set the Adapter, the empty state internal
     * state is changed.
     */
    public void forceShowEmptyState()
    {
        showEmptyState();
    }

    private void showEmptyState()
    {
        if(mEmptyStateVisible)
        {
            mEmptyStateContainer.setVisibility(VISIBLE);
        }
        else
        {
            mEmptyStateContainer.setVisibility(GONE);
        }
    }
}
