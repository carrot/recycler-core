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
    private List<RecyclerCoreModel> mModelList;

    /**
     * Keeps a mapping between the Model class and its corresponding #InjectController, so
     * that we dont have to use reflection every time we need the Controller.
     */
    private HashMap<Class<? extends RecyclerCoreModel>, InjectController> mMapModelController = new HashMap<>();

    /**
     * Keeps a mapping between Controller and its view type. Used in #getItemViewType
     * for fast lookup and return of the view type.
     */
    private HashMap<Class<? extends RecyclerCoreController>, Integer> mMapControllerViewType = new HashMap<>();

    /**
     * A parse array for fast lookup, used in #onBindViewHolder to get the InjectController
     * from the viewType.
     */
    private SparseArray<InjectController> mInjectControllerSparseArray = new SparseArray<>();

    public RecyclerCoreAdapter(List<RecyclerCoreModel> modelList)
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
        return getViewType(model);
    }

    private int getViewType(RecyclerCoreModel model)
    {
        InjectController injectedController = mMapModelController.get(model.getClass());
        if(injectedController == null)
        {
            injectedController = InstantiationUtil.getInjectedController(model);
            mMapModelController.put(model.getClass(), injectedController);
        }

        Class classController = injectedController.controller();
        Integer viewType = mMapControllerViewType.get(classController);
        if(viewType == null)
        {
            viewType = mInjectControllerSparseArray.size() + 1;
            mMapControllerViewType.put(classController, viewType);
            mInjectControllerSparseArray.put(viewType, injectedController);
        }

        return viewType;
    }
}
