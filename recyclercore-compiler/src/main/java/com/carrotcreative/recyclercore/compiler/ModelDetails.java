package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCModel;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by kaushiksaurabh on 9/20/16.
 */

class ModelDetails
{
    private final String mClassName;
    private final String mPackageName;
    private final String mControllerClassName;
    private final String mControllerPackageName;

    private ModelDetails(String className, String packageName, String controllerClassName,
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

    static ModelDetails fromTypeElement(TypeElement typeElement, Elements elementsUtil, Types
            typesUtil)
    {
        String pkgName = ClassUtil.getPackageName(typeElement, elementsUtil);
        String clsName = ClassUtil.getClassName(typeElement, pkgName);
        String contPkgName = null;
        String contClsName = null;
        try
        {
            RCModel annotation = typeElement.getAnnotation(RCModel.class);
            annotation.controller();
        }
        catch(MirroredTypeException ex)
        {
            TypeMirror typeMirror = ex.getTypeMirror();
            TypeElement tElement = (TypeElement) typesUtil.asElement(typeMirror);
            contPkgName = ClassUtil.getPackageName(tElement, elementsUtil);
            contClsName = ClassUtil.getClassName(tElement, contPkgName);
        }
        return new ModelDetails(clsName, pkgName, contClsName, contPkgName);
    }
}
