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
    private List mModelList;

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

    /**
     * Keeps a mapping for the view and its on click listener set by the adapter
     */
    private SparseArray<OnItemClickListener> mClickListenerSparseArray = new SparseArray<>();

    /**
     * Holds the reference to the click listener passed by the user
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * Interface for an item click in the recycler view
     */
    public interface OnItemClickListener
    {
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param view   The view that was clicked
         * @param position The position of the view in the adapter.
         */
        void onItemClick(View view, int position);
    }

    public RecyclerCoreAdapter(List modelList)
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
        bindClickListeners(holder.itemView, position);
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

    /**
     *
     * Note that this clickListener is set before the {@link RecyclerCoreController#bind(Object)}
     * is called, so if you set your on click listener,
     * you will be overwriting the clickListener and this callback will never be called.
     *
     * @param viewId          The view id inside the item view, for which the click listener will
     *                        be attach. If the view id is not present for item in the view, then
     *                        the click listener will be ignored.
     * @param onClickListener
     */
    public void setOnItemViewClickListener(int viewId, OnItemClickListener onClickListener)
    {
        mClickListenerSparseArray.put(viewId, onClickListener);
    }

    /**
     * Similar to {@link #setOnItemViewClickListener(int, OnItemClickListener)} but takes in an
     * array of view ids
     *
     * @param viewIds
     * @param onClickListener
     */
    public void setOnItemViewClickListener(int[] viewIds, OnItemClickListener onClickListener)
    {
        for(int i = 0; i < viewIds.length; i++)
        {
            mClickListenerSparseArray.put(viewIds[i], onClickListener);
        }
    }

    /**
     * Note that this clickListener in {@link RecyclerCoreController#itemView} is set before the
     * {@link RecyclerCoreController#bind(Object)}
     * is called, so if you set your on click listener,
     * you will be overwriting the clickListener and this callback will never be called.
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     *
     * @param view The root view of the recycler view holder
     * @param pos The position in the recycler view items
     */
    private void bindClickListeners(View view, final int pos)
    {
        bindOnItemClickListener(view, pos);
        //Search and find the click listeners for the viewId's passed by the user
        for(int i = 0; i< mClickListenerSparseArray.size(); i++)
        {
            final int key = mClickListenerSparseArray.keyAt(i);
            final View foundView = view.findViewById(key);
            if(foundView != null)
            {
                foundView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mClickListenerSparseArray.get(key).onItemClick(foundView, pos);
                    }
                });
            }
        }
    }

    /**
     * The different between this and {@link #bindClickListeners(View, int)} is that this function
     * only binds the listener to the {@link RecyclerCoreController#itemView}
     *
     * @param view The root view of the recycler view holder
     * @param pos  The position in the recycler view items
     */
    private void bindOnItemClickListener(final View view, final int pos)
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            }
        });
    }

    /**
     * A helper function to return the item at the given position in the adapter
     * {@link OnItemClickListener#onItemClick(View, int)} should be sufficient as they return the
     * position of the item clicked, but in case when reference to the models is not preserved,
     * this function can be used to get the model at a specified position
     *
     * @param position The position from which the model is required.
     * @return The object set in the @mModelList at the given position.
     */
    public Object getItemAtPosition(int position)
    {
        return mModelList.get(position);
    }
}
