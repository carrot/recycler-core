package com.carrotcreative.recyclercore.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrotcreative.recyclercore.adapter.RecyclerCoreController;
import com.carrotcreative.recyclercore.adapter.RecyclerCoreModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstantiationUtil
{
    private InstantiationUtil() {}

    /**
     *
     * @param modelClass The model class for which we need to instantiate an object.
     * @return
     */
    public static RecyclerCoreModel instantiateModel(Class<?> modelClass)
    {
        String className = modelClass.getCanonicalName();
        Constructor<?>[] consArray = modelClass.getConstructors();
        Constructor noArgConstructor = null;
        for (Constructor constructor : consArray)
        {
            int argCount = constructor.getParameterTypes().length;
            if (argCount == 0)
            {
                noArgConstructor = constructor;
                break;
            }
        }

        RecyclerCoreModel modelInstance = null;
        if (noArgConstructor != null)
        {
            try
            {
                modelInstance = (RecyclerCoreModel) noArgConstructor.newInstance();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        if(modelInstance == null)
        {
            throw new IllegalArgumentException("class " + className + " does not have a public no-argument constructor!");
        }

        return modelInstance;
    }

    public static RecyclerCoreController instantiateController(Class<?> controllerClass, int resource, ViewGroup parent)
    {
        String className = controllerClass.getCanonicalName();
        Constructor<?>[] consArray = controllerClass.getConstructors();
        Constructor defaultConstructor = null;
        for (Constructor constructor : consArray)
        {
            int argCount = constructor.getParameterTypes().length;
            if (argCount == 1)
            {
                defaultConstructor = constructor;
                break;
            }
        }

        RecyclerCoreController controllerInstance = null;
        if (defaultConstructor != null)
        {
            try
            {
                Context context = parent.getContext();
                View itemView = LayoutInflater.from(context).inflate(resource, parent, false);
                controllerInstance = (RecyclerCoreController) defaultConstructor.newInstance(itemView);
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        if(controllerInstance == null)
        {
            throw new IllegalArgumentException("class " + className + " does not have a valid public constructor! " +
                    "The constructor should take only View as an argument.");
        }

        return controllerInstance;
    }
}
