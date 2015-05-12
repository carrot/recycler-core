package com.carrotcreative.recyclercore.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

public abstract class RecyclerCoreAdapter extends RecyclerView.Adapter<RecyclerCoreController>{

    protected Activity mActivity;
    private List<RecyclerCoreModel> mModelList;
    private HashMap<Integer, RecyclerCoreModel> mRegisteredModels;

    public RecyclerCoreAdapter(List<RecyclerCoreModel> modelList, Activity activity)
    {
        mModelList = modelList;
        mActivity = activity;
        mRegisteredModels = new HashMap<>();
        registerModels();
    }

    /**
     * In this function you should call {@link #registerModel}
     * to register all models to this adapter
     */
    protected abstract void registerModels();

    /**
     * This function will register a single model to the adapter
     */
    protected void registerModel(RecyclerCoreModel model)
    {
        int key = model.getViewType().hashCode();
        if( ! mRegisteredModels.containsKey(key))
        {
            mRegisteredModels.put(key, model);
        }
        else
        {
            // Throw an exception -- a model is either being registered twice, or there is
            // a collision with the hashCodes of two models
            throw new IllegalStateException("There was an issue registering " + model.getViewType() + " with the adapter.");
        }
    }

    @Override
    public RecyclerCoreController onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mActivity);

        for(Integer registeredType : mRegisteredModels.keySet())
        {
            if(registeredType == viewType)
            {
                RecyclerCoreModel model = mRegisteredModels.get(registeredType);
                return model.buildController(inflater, parent, mActivity);
            }
        }

        // Model hasn't been registered
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerCoreController holder, int position)
    {
        RecyclerCoreModel model = mModelList.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount()
    {
        return mModelList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        RecyclerCoreModel model = mModelList.get(position);
        String modelViewType = model.getViewType();
        return modelViewType.hashCode();
    }

}
