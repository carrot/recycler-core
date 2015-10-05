package com.carrotcreative.recyclercore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerCoreController<Model> extends RecyclerView.ViewHolder {

    private Context mContext;

    public RecyclerCoreController(View itemView)
    {
        super(itemView);
        mContext = itemView.getContext();
    }

    /**
     * A helper method to get the context in the controller.
     * @return The context of the view.
     */
    public Context getContext()
    {
        return mContext;
    }

    public abstract void bind(Model model);

}
