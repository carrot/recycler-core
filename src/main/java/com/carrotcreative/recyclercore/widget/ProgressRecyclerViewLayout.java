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

import java.util.ArrayDeque;
import java.util.ArrayList;

public class ProgressRecyclerViewLayout extends RelativeLayout
{
    private FrameLayout mContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mErrorStateView;
    private ArrayDeque<ViewVisibilityInstanceState> mViewVisibilityStack;

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

        mViewVisibilityStack = new ArrayDeque<>();
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
     */
    public void setErrorStateEnabled(boolean enable)
    {
        if(mErrorStateView == null)
        {
            throw new IllegalStateException("Trying to setErrorStateEnabled without setting the error state View.");
        }

        if(enable)
        {
            if(! isErrorStateEnabled())
            {
                saveCurrentViewState();
                mErrorStateView.setVisibility(VISIBLE);
                mRecyclerView.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
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

    private View mEmptyStateView;

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
    public boolean isEmptyStateEnabled()
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
    public void setEmptyStateEnabled(boolean enable)
    {
        if(mEmptyStateView == null)
        {
            throw new IllegalStateException("Trying to setEmptyStateEnabled without setting the empty state View.");
        }

        if(enable)
        {
            if(! isEmptyStateEnabled())
            {
                saveCurrentViewState();
                mErrorStateView.setVisibility(VISIBLE);
                mRecyclerView.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
            }
        }
        else
        {
            restorePreviousViewState();
        }
    }

    // ============================================== //
    // ============= Helper Functions =============== //
    // ============================================= //

    private void saveCurrentViewState()
    {
        ViewVisibilityInstanceState visibilityState = getCurrentVisibilityState();
        mViewVisibilityStack.push(visibilityState);
    }

    private void restorePreviousViewState()
    {
        ViewVisibilityInstanceState visibilityState = mViewVisibilityStack.poll();
        if(visibilityState != null)
        {
            setCurrentVisibilityState(visibilityState);
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

    private ViewVisibilityInstanceState getCurrentVisibilityState()
    {
        ViewVisibilityInstanceState visibilityState = new ViewVisibilityInstanceState();
        visibilityState.setProgressViewVisibility(mProgressBar.getVisibility());
        visibilityState.setRecyclerViewVisibility(mRecyclerView.getVisibility());

        /**
         * If error view is not set, set the visibility to gone.
         */
        if(mErrorStateView != null)
        {
            visibilityState.setErrorViewVisibility(mErrorStateView.getVisibility());
        }
        else
        {
            visibilityState.setErrorViewVisibility(View.GONE);
        }

        /**
         * If empty view is not set, set the visibility to gone.
         */
        if(mEmptyStateView != null)
        {
            visibilityState.setEmptyViewVisibility(mEmptyStateView.getVisibility());
        }
        else
        {
            visibilityState.setEmptyViewVisibility(View.GONE);
        }

        return visibilityState;
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
