package com.carrotcreative.recyclercore.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.carrotcreative.recyclercore.inject.InjectController;
import com.carrotcreative.recyclercore.util.InstantiationUtil;
import java.util.HashMap;
import java.util.List;

public class RecyclerCoreAdapter extends RecyclerView.Adapter<RecyclerCoreController>{

    private List<RecyclerCoreModel> mModelList;
    private HashMap<Integer, RecyclerCoreModel> mRegisteredModels;

    /**
     * If the models in the adapter change, register for the new models.
     * FYI : onChanged() gets called twice - not sure why.
     */
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            registerModels();
            super.onChanged();
        }
    };

    public RecyclerCoreAdapter(List<RecyclerCoreModel> modelList)
    {
        mModelList = modelList;
        mRegisteredModels = new HashMap<>();
        registerModels();
        registerAdapterDataObserver(mDataObserver);
    }

    private void registerModels()
    {
        if(mModelList != null)
        {
            for(RecyclerCoreModel model: mModelList)
            {
                int key = model.getViewType().hashCode();
                if( ! mRegisteredModels.containsKey(key) )
                {
                    mRegisteredModels.put(key, InstantiationUtil.instantiateModel(model.getClass()));
                }
            }
        }
    }

    @Override
    public RecyclerCoreController onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        for(Integer registeredType : mRegisteredModels.keySet())
        {
            if(registeredType == viewType)
            {
                RecyclerCoreModel model = mRegisteredModels.get(registeredType);
                /**
                 * Inflate controller using the annotation methods
                 */
                InjectController injectController = model.getClass().getAnnotation(InjectController.class);
                Class controllerClass = injectController.controller();
                int layout = injectController.layout();
                RecyclerCoreController coreController = InstantiationUtil.instantiateController(controllerClass, layout, parent);
                return coreController;
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
