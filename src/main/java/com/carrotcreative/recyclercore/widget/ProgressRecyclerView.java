package com.carrotcreative.recyclercore.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.carrotcreative.recyclercore.R;
import com.carrotcreative.recyclercore.adapter.RecyclerCoreAdapter;
import com.carrotcreative.recyclercore.adapter.RecyclerCoreModel;

import java.util.ArrayList;

public class ProgressRecyclerView extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    public ProgressRecyclerView(Context context)
    {
        this(context, null);
    }

    public ProgressRecyclerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ProgressRecyclerView(Context context, AttributeSet attrs, int defStyleAttr)
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
        setAdapter(new RecyclerCoreAdapter(models, mRecyclerView.getContext()));
    }

    /** Wrapper for {@link android.support.v7.widget.RecyclerView#setLayoutManager} */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager)
    {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /** Wrapper for {@link RecyclerView#getLayoutManager()} */
    public RecyclerView.LayoutManager getLayoutManager()
    {
        return mRecyclerView.getLayoutManager();
    }

    /** Wrapper for {@link android.support.v7.widget.RecyclerView#setAdapter} */
    public void setAdapter(RecyclerView.Adapter adapter)
    {
        if(mRecyclerView.getAdapter() != adapter)
        {
            if(mRecyclerView.getAdapter() != null)
            {
                mRecyclerView.getAdapter().unregisterAdapterDataObserver(mDataSetObserver);
            }

            mRecyclerView.setAdapter(adapter);
            setProgressBarVisibility();
            adapter.registerAdapterDataObserver(mDataSetObserver);
        }
    }

    /** Wrapper for {@link RecyclerView#getAdapter()} */
    public RecyclerView.Adapter getAdapter()
    {
        return mRecyclerView.getAdapter();
    }

    /**
     * An observer that manages the visibility state of the ProgressBar
     */
    private RecyclerView.AdapterDataObserver mDataSetObserver = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            setProgressBarVisibility();
        }
    };

    private void setProgressBarVisibility()
    {
        if(mRecyclerView.getAdapter().getItemCount() > 0)
        {
            mProgressBar.setVisibility(GONE);
        }
        else
        {
            mProgressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if(mRecyclerView != null && mRecyclerView.getAdapter() != null)
        {
            mRecyclerView.getAdapter().unregisterAdapterDataObserver(mDataSetObserver);
        }
    }
}
