package com.carrotcreative.recyclercore.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrotcreative.recyclercore.inject.InjectController;
import com.carrotcreative.recyclercore.util.InstantiationUtil;

import java.util.HashMap;
import java.util.List;

public class RecyclerCoreAdapter extends RecyclerView.Adapter<RecyclerCoreController>
{
    /**
     * The list of #RecyclerCoreModel attached to this Adapter.
     */
    private List<Object> mModelList;

    /**
     * Keeps a mapping between the Model class and its viewType
     */
    private HashMap<Class<?>, Integer> mModelViewTypeMap = new HashMap<>();

    /**
     * Keeps a mapping between the viewType and its corresponding #InjectController
     * A parse array for fast lookup, used in #onBindViewHolder to get the InjectController
     * from the viewType.
     */
    private SparseArray<InjectController> mInjectControllerSparseArray = new SparseArray<>();

    public RecyclerCoreAdapter(List<Object> modelList)
    {
        mModelList = modelList;
    }

    @Override
    public RecyclerCoreController onCreateViewHolder(ViewGroup parent, int viewType)
    {
        InjectController injectController = mInjectControllerSparseArray.get(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(injectController.layout(),
                parent, false);
        return InstantiationUtil.instantiateController(injectController.controller(), view );
    }

    @Override
    public void onBindViewHolder(RecyclerCoreController holder, int position)
    {
        Object model = mModelList.get(position);
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
        Object model = mModelList.get(position);
        return getViewType(model);
    }

    private int getViewType(Object model)
    {
        Integer viewType = mModelViewTypeMap.get(model.getClass());
        if(viewType == null)
        {
            viewType = mInjectControllerSparseArray.size() + 1;
            InjectController injectController = getInjectedController(model);
            mInjectControllerSparseArray.put(viewType, injectController);
            mModelViewTypeMap.put(model.getClass(), viewType);
        }
        return viewType;
    }

    private InjectController getInjectedController(Object model)
    {
        InjectController injectedController = model.getClass().getAnnotation(InjectController.class);
        if(injectedController == null)
        {
            throw new IllegalStateException(" class " + model.getClass().getCanonicalName() + " " +
                    " does not implement InjectController annotation");
        }

        return injectedController;
    }
}
