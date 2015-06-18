package com.carrotcreative.recyclercore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class RecyclerCoreModel {

    public String getViewType()
    {
        return getClass().getCanonicalName();
    }

    public abstract RecyclerCoreController buildController(LayoutInflater inflater, ViewGroup parent, Context context);

}
