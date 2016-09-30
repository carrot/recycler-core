package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCController;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by kaushik on 9/30/16.
 */

class ControllerDetails
{
    String mClassName;
    String mPackageName;
    int mLayoutId;

    private ControllerDetails(String className, String packageName, int layoutId)
    {
        mClassName = className;
        mPackageName = packageName;
        mLayoutId = layoutId;
    }

    public String getClassName()
    {
        return mClassName;
    }

    public String getPackageName()
    {
        return mPackageName;
    }

    public int getLayoutId()
    {
        return mLayoutId;
    }

    static ControllerDetails fromTypeElement(TypeElement elem, Elements elementsUtil)
    {
        String pkgName = ClassUtil.getPackageName(elem, elementsUtil);
        String clsName = ClassUtil.getClassName(elem, pkgName);
        RCController controller = elem.getAnnotation(RCController.class);
        return new ControllerDetails(clsName, pkgName, controller.layout());
    }
}
