package com.carrotcreative.recyclercore.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by kaushiksaurabh on 11/18/15.
 */
public class RecyclerCoreRecyclerView extends RecyclerView
{
    public RecyclerCoreRecyclerView(Context context)
    {
        super(context);
    }

    public RecyclerCoreRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RecyclerCoreRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        super.setAdapter(adapter);
        throw new IllegalStateException("Invalid usage of setAdapter. Use " +
                ProgressRecyclerViewLayout.class.getSimpleName() +
                ".setAdapter(RecyclerCoreBaseAdapter adapter) to Set the adapter");
    }

    void setCoreAdapter(Adapter adapter)
    {
        super.setAdapter(adapter);
    }
}
