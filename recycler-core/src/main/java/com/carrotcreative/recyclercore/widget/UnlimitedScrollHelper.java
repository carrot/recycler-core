package com.carrotcreative.recyclercore.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by kaushiksaurabh on 7/19/16.
 */

final class UnlimitedScrollHelper
{
    private static final String TAG = UnlimitedScrollHelper.class.getSimpleName();

    static final int INVALID_DISTANCE_FROM_BOTTOM_TO_LOAD_MORE = - 1;
    private int mDistanceFromBottomToRefresh = INVALID_DISTANCE_FROM_BOTTOM_TO_LOAD_MORE;
    private int mLastVisibleItemPosition;
    private ProgressRecyclerViewLayout.OnLoadPointListener mLoadPointListener = new
            ProgressRecyclerViewLayout.OnLoadPointListener()
            {
                @Override
                public void onReachedLoadPoint()
                {
                    //Dummy to avoid null check
                }
            };

    void setDistanceFromBottomToRefresh(int value)
    {
        mDistanceFromBottomToRefresh = value;
    }

    void setLoadPointListener(@NonNull ProgressRecyclerViewLayout.OnLoadPointListener
                                      loadPointListener)
    {
        mLoadPointListener = loadPointListener;
    }

    void onScrolled(RecyclerView.LayoutManager layoutManager, int totalItemCount)
    {
        // If the distance is invalid, do nothing, return
        if(mDistanceFromBottomToRefresh <= INVALID_DISTANCE_FROM_BOTTOM_TO_LOAD_MORE)
        {
            return;
        }
        if(layoutManager instanceof LinearLayoutManager)
        {
            LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
            int lastVisiblePos = llm.findLastVisibleItemPosition();
            if(lastVisiblePos != mLastVisibleItemPosition)
            {
                mLastVisibleItemPosition = lastVisiblePos;
                if(totalItemCount - (mLastVisibleItemPosition + 1) <= mDistanceFromBottomToRefresh)
                {
                    mLoadPointListener.onReachedLoadPoint();
                }
            }
        }
        else if(layoutManager instanceof StaggeredGridLayoutManager)
        {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) layoutManager;
            int spanCount = sglm.getSpanCount();
            // Avoid division by zero error
            if(spanCount == 0)
            {
                return;
            }
            // maxNoOfRows is total no of rows, so its 1 indexed.
            int maxNoOfRows = getMaxRows(totalItemCount, spanCount);
            int maxPositionReached = getMax(sglm.findLastVisibleItemPositions(null));
            // unlike maxNoOfRows, rows are zero indexed, just like position
            int lastRowReached = maxPositionReached / spanCount;
            if(lastRowReached != mLastVisibleItemPosition)
            {
                mLastVisibleItemPosition = lastRowReached;
                if(maxNoOfRows - (mLastVisibleItemPosition + 1) <= mDistanceFromBottomToRefresh)
                {
                    mLoadPointListener.onReachedLoadPoint();
                }
            }
        }
    }

    private static int getMax(int[] arr)
    {
        int max = arr[0];
        for(int i = 1; i < arr.length; i++)
        {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    /**
     *
     * @param totalCount Total count of items.
     * @param spanCount The {@link StaggeredGridLayoutManager#getSpanCount()} value
     * @return
     */
    private static int getMaxRows(int totalCount, int spanCount)
    {
        int remainder = totalCount % spanCount;
        int rows = totalCount / spanCount;
        if(remainder == 0)
        {
            return rows;
        }
        else
        {
            return rows + 1;
        }
    }
}
