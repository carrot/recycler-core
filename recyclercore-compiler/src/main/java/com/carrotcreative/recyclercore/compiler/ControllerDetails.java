package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCController;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;

/**
 * Created by kaushik on 9/30/16.
 */

class ControllerDetails
{
    private final ClassName mClassName;
    private final String mCanonicalName;
    private final int mLayoutId;

    private ControllerDetails(ClassName className, String canonicalName, int layoutId)
    {
        mClassName = className;
        mCanonicalName = canonicalName;
        mLayoutId = layoutId;
    }

    public ClassName getClassName()
    {
        return mClassName;
    }

    public String getCanonicalName()
    {
        return mCanonicalName;
    }

    public int getLayoutId()
    {
        return mLayoutId;
    }

    static ControllerDetails fromTypeElement(TypeElement elem)
    {
        ClassName clsName = ClassName.get(elem);
        String canonName = ClassUtil.getCanonicalName(clsName);
        RCController controller = elem.getAnnotation(RCController.class);
        return new ControllerDetails(clsName, canonName, controller.layout());
    }
}
