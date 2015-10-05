package com.carrotcreative.recyclercore.widget;

import android.content.Context;
import android.support.annotation.IntDef;
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

import java.util.ArrayList;

public class ProgressRecyclerViewLayout extends RelativeLayout
{
    private FrameLayout mContainer;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar mProgressBar;
    private View mErrorStateView;
    private View mEmptyStateView;
    private ViewVisibilityInstanceState mPrevViewVisibilityState;

    /**
     * Data observer to check for the empty states.
     */
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            checkEmptyState();
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

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(mAdapter != null)
        {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
    }

    private void findViews()
    {
        mContainer = (FrameLayout) findViewById(R.id.progress_recycler_view_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.progress_recycler_view_recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_recycler_view_progress_bar);
    }

    private void setDefaultLayoutManager()
    {
        LinearLayoutManager manager = new LinearLayoutManager(mRecyclerView.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    private void setDefaultAdapter()
    {
        ArrayList models = new ArrayList<>();
        mRecyclerView.setAdapter(new RecyclerCoreAdapter(models));
    }

    /** Wrapper for {@link android.support.v7.widget.RecyclerView#setLayoutManager} */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager)
    {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /** Wrapper for {@link android.support.v7.widget.RecyclerView#setAdapter} */
    public void setAdapter(RecyclerCoreAdapter adapter)
    {
        /**
         * Need to reset the visibility, in case the adapter is set multiple times.
         */
        resetViewVisibility();

        /**
         * deregister the previous observer.
         */
        if(mAdapter != null && mAdapter != adapter)
        {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }

        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
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

    // ========================================== //
    // =============  Error State =============== //
    // ========================================== //

    /**
     *
     * @return True if the errorStateView is set and is Visible.
     */
    public boolean isErrorStateEnabled()
    {
        if(mErrorStateView != null && mErrorStateView.getVisibility() == View.VISIBLE)
        {
            return true;
        }
        return false;
    }

    /**
     * A methods to show/hide the error state. When #setErrorStateEnabled(true) is called
     * after #setErrorStateEnabled(false), the view is reset to what is was before is was enabled.
     *
     * @param enable True to show the error state
     *               False to hide the error state.
     *               Throws and #IllegalStateException if the error view is not set.
     *               Set the error view using #setErrorView
     *               Note: that the error view is set only if the empty state is not enabled.
     *               Only one of them can be active at one time.
     */
    public void setErrorStateEnabled(boolean enable)
    {
        if(mErrorStateView == null)
        {
            throw new IllegalStateException("Trying to setErrorStateEnabled without setting the error state View.");
        }

        if(enable)
        {
            if(! isErrorStateEnabled() && ! isEmptyStateEnabled())
            {
                saveCurrentViewState();
                mErrorStateView.setVisibility(VISIBLE);
                mRecyclerView.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
                if(mEmptyStateView != null)
                {
                    mEmptyStateView.setVisibility(GONE);
                }
            }
        }
        else
        {
            restorePreviousViewState();
        }
    }

    /**
     *
     * @param errorView The error view that will be displayed when we call the method
     *                  #setErrorStateEnabled true.
     */
    public void setErrorView(View errorView)
    {
        if(mErrorStateView == null)
        {
            mErrorStateView = errorView;
            mContainer.addView(mErrorStateView);
            mErrorStateView.setVisibility(GONE);
        }

        if(mErrorStateView != errorView)
        {
            mContainer.removeView(mErrorStateView);
            mErrorStateView = errorView;
            mContainer.addView(mErrorStateView);
            mErrorStateView.setVisibility(GONE);
        }
    }

    // ========================================== //
    // =============  Empty State =============== //
    // ========================================== //

    private void checkEmptyState()
    {
        if(mAdapter != null)
        {
            if(mAdapter.getItemCount() == 0)
            {
                setEmptyStateEnabled(true);
            }
            else
            {
                setEmptyStateEnabled(false);
            }
        }
    }

    /**
     *
     * @param emptyStateView The view that will be displayed when there are no items to
     *                       display in the adapter.
     *                       Note that either the ErrorState view or the EmptyState view
     *                       can be shown at one time.
     *                       If the errorState is enabled then the empty view will not be set,
     *                       and vice versa.
     */
    public void setEmptyStateView(View emptyStateView)
    {
        if(mEmptyStateView == null)
        {
            mEmptyStateView = emptyStateView;
            mContainer.addView(mEmptyStateView);
            mEmptyStateView.setVisibility(GONE);
        }

        if(mEmptyStateView != emptyStateView)
        {
            mContainer.removeView(mEmptyStateView);
            mEmptyStateView = emptyStateView;
            mContainer.addView(mEmptyStateView);
            mEmptyStateView.setVisibility(GONE);
        }
    }

    /**
     *
     * @return True if the EmptyStateView is set and is Visible.
     */
    private boolean isEmptyStateEnabled()
    {
        if(mEmptyStateView != null && mEmptyStateView.getVisibility() == View.VISIBLE)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * A methods to show/hide the empty state. When #setEmptyStateEnabled(true) is called
     * after #setEmptyStateEnabled(false), the view is reset to what is was before is was enabled.
     *
     * @param enable True to show the empty state
     *               False to hide the empty state.
     *               Throws and #IllegalStateException if the empty view is not set.
     *               Set the empty view using #setEmptyStateView
     */
    private void setEmptyStateEnabled(boolean enable)
    {
        /**
         * If the empty state view is not set, do an early return.
         */
        if(mEmptyStateView == null)
        {
            return;
        }

        if(enable)
        {
            if(! isEmptyStateEnabled() && ! isErrorStateEnabled())
            {
                saveCurrentViewState();
                mEmptyStateView.setVisibility(VISIBLE);
                mRecyclerView.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
                if(mErrorStateView != null)
                {
                    mErrorStateView.setVisibility(GONE);
                }
            }
        }
        else
        {
            resetViewVisibility();
            mProgressBar.setVisibility(GONE);
        }
    }

    // ============================================== //
    // ============= Helper Functions =============== //
    // ============================================= //

    private void resetViewVisibility()
    {
        mPrevViewVisibilityState = null;
        mRecyclerView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        if(mEmptyStateView != null)
        {
            mEmptyStateView.setVisibility(GONE);
        }

        if(mErrorStateView != null)
        {
            mErrorStateView.setVisibility(GONE);
        }
    }

    private void saveCurrentViewState()
    {
        if(mPrevViewVisibilityState == null)
        {
            mPrevViewVisibilityState = new ViewVisibilityInstanceState();
        }

        mPrevViewVisibilityState.setProgressViewVisibility(mProgressBar.getVisibility());
        mPrevViewVisibilityState.setRecyclerViewVisibility(mRecyclerView.getVisibility());

        /**
         * If error view is not set, set the visibility to gone.
         */
        if(mErrorStateView != null)
        {
            mPrevViewVisibilityState.setErrorViewVisibility(mErrorStateView.getVisibility());
        }
        else
        {
            mPrevViewVisibilityState.setErrorViewVisibility(View.GONE);
        }

        /**
         * If empty view is not set, set the visibility to gone.
         */
        if(mEmptyStateView != null)
        {
            mPrevViewVisibilityState.setEmptyViewVisibility(mEmptyStateView.getVisibility());
        }
        else
        {
            mPrevViewVisibilityState.setEmptyViewVisibility(View.GONE);
        }
    }

    private void restorePreviousViewState()
    {
        if(mPrevViewVisibilityState != null)
        {
            setCurrentVisibilityState(mPrevViewVisibilityState);
        }
    }

    private void setCurrentVisibilityState(ViewVisibilityInstanceState currentState)
    {
        mRecyclerView.setVisibility(currentState.getRecyclerViewVisibility());
        mProgressBar.setVisibility(currentState.getProgressViewVisibility());
        if(mEmptyStateView != null)
        {
            mEmptyStateView.setVisibility(currentState.getEmptyViewVisibility());
        }

        if(mErrorStateView != null)
        {
            mErrorStateView.setVisibility(currentState.getErrorViewVisibility());
        }
    }

    /**
     * Add Visibility annotation for lint checks.
     */
    @IntDef({VISIBLE, INVISIBLE, GONE})
    public @interface Visibility {}

    /**
     * A Helper class that wraps the visibility of the views.
     */
    private static class ViewVisibilityInstanceState
    {
        private int mRecyclerViewVisibility;
        private int mProgressViewVisibility;
        private int mErrorViewVisibility;
        private int mEmptyViewVisibility;

        @Visibility
        public int getEmptyViewVisibility()
        {
            return mEmptyViewVisibility;
        }

        public void setEmptyViewVisibility(@Visibility int emptyViewVisibility)
        {
            mEmptyViewVisibility = emptyViewVisibility;
        }

        @Visibility
        public int getErrorViewVisibility()
        {
            return mErrorViewVisibility;
        }

        public void setErrorViewVisibility(@Visibility int errorViewVisibility)
        {
            mErrorViewVisibility = errorViewVisibility;
        }

        @Visibility
        public int getRecyclerViewVisibility()
        {
            return mRecyclerViewVisibility;
        }

        public void setRecyclerViewVisibility(@Visibility int recyclerViewVisibility)
        {
            mRecyclerViewVisibility = recyclerViewVisibility;
        }

        @Visibility
        public int getProgressViewVisibility()
        {
            return mProgressViewVisibility;
        }

        public void setProgressViewVisibility(@Visibility int progressViewVisibility)
        {
            mProgressViewVisibility = progressViewVisibility;
        }
    }
}
