package com.carrotcreative.recyclercore.util;

import android.view.View;

import com.carrotcreative.recyclercore.adapter.RecyclerCoreController;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstantiationUtil
{
    private InstantiationUtil() {}

    public static RecyclerCoreController instantiateController(Class<?> controllerClass, View itemView)
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
