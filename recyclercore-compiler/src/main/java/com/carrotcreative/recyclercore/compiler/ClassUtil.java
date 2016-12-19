package com.carrotcreative.recyclercore.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Created by kaushik on 9/30/16.
 */

class ClassUtil
{
    static String getCanonicalName(ClassName className)
    {
        ClassName enclosingClass = className.enclosingClassName();
        String canonicalName;
        if(enclosingClass != null)
        {
            canonicalName = getCanonicalName(enclosingClass) + "." + className.simpleName();
        }
        else
        {
            canonicalName = className.packageName() + "." + className.simpleName();
        }
        return canonicalName;
    }
}
