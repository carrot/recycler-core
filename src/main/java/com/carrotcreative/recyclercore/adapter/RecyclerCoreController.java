package com.carrotcreative.recyclercore.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerCoreController<Model extends RecyclerCoreModel> extends RecyclerView.ViewHolder {

    protected Activity mActivity;

    public RecyclerCoreController(View itemView, Activity activity)
    {
        super(itemView);
        mActivity = activity;
    }

    public abstract void bind(Model model);

}
