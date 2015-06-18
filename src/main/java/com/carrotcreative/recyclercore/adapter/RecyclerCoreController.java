package com.carrotcreative.recyclercore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerCoreController<Model extends RecyclerCoreModel> extends RecyclerView.ViewHolder {

    protected Context mContext;

    public RecyclerCoreController(View itemView, Context context)
    {
        super(itemView);
        mContext = context;
    }

    public abstract void bind(Model model);

}
