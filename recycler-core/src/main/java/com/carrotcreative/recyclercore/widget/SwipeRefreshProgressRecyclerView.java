package com.carrotcreative.recyclercore.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by kaushiksaurabh on 7/19/16.
 */

public class SwipeRefreshProgressRecyclerView extends SwipeRefreshLayout
{
    private ProgressRecyclerViewLayout mProgressRecyclerViewLayout;

    public SwipeRefreshProgressRecyclerView(Context context)
    {
        super(context);
        init();
    }

    public SwipeRefreshProgressRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        mProgressRecyclerViewLayout = new ProgressRecyclerViewLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mProgressRecyclerViewLayout.setLayoutParams(layoutParams);
        addView(mProgressRecyclerViewLayout);
    }

    /**
     *
     * @return
     */
    public ProgressRecyclerViewLayout getProgressRecyclerViewLayout()
    {
        return mProgressRecyclerViewLayout;
    }
}
