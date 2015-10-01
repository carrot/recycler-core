package com.carrotcreative.recyclercore.adapter;

public abstract class RecyclerCoreModel {

    public String getViewType()
    {
        return getClass().getCanonicalName();
    }
}
