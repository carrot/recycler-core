package com.carrotcreative.recyclercore.compiler;

/**
 * Created by kaushiksaurabh on 9/20/16.
 */

public class ModelDetails
{
    private final String mClassName;
    private final String mPackageName;
    private final String mControllerClassName;
    private final String mControllerPackageName;

    ModelDetails(String className, String packageName, String controllerClassName,
                 String controllerPackageName)
    {
        mClassName = className;
        mPackageName = packageName;
        mControllerClassName = controllerClassName;
        mControllerPackageName = controllerPackageName;
    }

    public String getClassName()
    {
        return mClassName;
    }

    public String getPackageName()
    {
        return mPackageName;
    }

    public String getControllerClassName()
    {
        return mControllerClassName;
    }

    public String getControllerPackageName()
    {
        return mControllerPackageName;
    }
}
