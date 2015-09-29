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
import com.carrotcreative.recyclercore.adapter.RecyclerCoreModel;

import java.util.ArrayList;

public class ProgressRecyclerViewLayout extends RelativeLayout
{
    private FrameLayout mContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mErrorStateView;
    private ViewVisibilityInstanceState mPrevViewVisibilityInstanceState;

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

    // ========================================== //
    // =============  Error State =============== //
    // ========================================== //

    public boolean isShowingErrorState()
    {
        if(mErrorStateView != null && mErrorStateView.getVisibility() == View.VISIBLE)
        {
            return true;
        }
        return false;
    }

    public void setErrorStateEnabled(boolean show)
    {
        if(mErrorStateView == null)
        {
            throw new IllegalStateException("Trying to setErrorStateEnabled without setting the error state View.");
        }

        if(show)
        {
            if(! isShowingErrorState())
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

    public void setErrorStateEnabled(View errorView, boolean show)
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

        setErrorStateEnabled(show);
    }

    private void saveCurrentViewState()
    {
        if(mPrevViewVisibilityInstanceState == null)
        {
            mPrevViewVisibilityInstanceState = new ViewVisibilityInstanceState();
        }

        mPrevViewVisibilityInstanceState.setProgressViewVisibility(mProgressBar.getVisibility());
        mPrevViewVisibilityInstanceState.setRecyclerViewVisibility(mRecyclerView.getVisibility());
        mPrevViewVisibilityInstanceState.setErrorViewVisibility(mErrorStateView.getVisibility());
    }

    private void restorePreviousViewState()
    {
        if(mPrevViewVisibilityInstanceState != null)
        {
            mRecyclerView.setVisibility(mPrevViewVisibilityInstanceState.getRecyclerViewVisibility());
            mProgressBar.setVisibility(mPrevViewVisibilityInstanceState.getProgressViewVisibility());
            mErrorStateView.setVisibility(mPrevViewVisibilityInstanceState.getErrorViewVisibility());
        }
    }

    /**
     * Add Visibility annotation for lint checks.
     */
    @IntDef({VISIBLE, INVISIBLE, GONE})
    public @interface Visibility {}

    /**
     * It wrap View visibility states.
     */
    private static class ViewVisibilityInstanceState
    {
        private int mRecyclerViewVisibility;
        private int mProgressViewVisibility;
        private int mErrorViewVisibility;

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
