package com.carrotcreative.recyclercore.lint.models;

import com.android.tools.lint.detector.api.Location;

/**
 * Created by kaushiksaurabh on 10/27/16.
 */

public class RCModelNode
{
    private final String mCanonicalName;
    private String mControllerCanonicalName;
    private Location mLocation;

    public RCModelNode(String canonicalName)
    {
        mCanonicalName = canonicalName;
    }

    public String getCanonicalName()
    {
        return mCanonicalName;
    }

    public void setControllerCanonicalName(String controllerCanonicalName)
    {
        mControllerCanonicalName = controllerCanonicalName;
    }

    public String getControllerCanonName()
    {
        return mControllerCanonicalName;
    }

    public Location getLocation()
    {
        return mLocation;
    }

    public void setLocation(Location location)
    {
        mLocation = location;
    }

    @Override
    public String toString()
    {
        return "(" + mCanonicalName + ", " + mControllerCanonicalName + ")";
    }
}
