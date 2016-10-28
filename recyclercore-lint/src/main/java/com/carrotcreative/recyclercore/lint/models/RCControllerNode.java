package com.carrotcreative.recyclercore.lint.models;

/**
 * Created by kaushiksaurabh on 10/27/16.
 */

public class RCControllerNode
{
    private String mCanonicalName;

    public RCControllerNode(String canonicalName)
    {
        mCanonicalName = canonicalName;
    }

    public String getCanonicalName()
    {
        return mCanonicalName;
    }

    @Override
    public String toString()
    {
        return mCanonicalName;
    }
}
