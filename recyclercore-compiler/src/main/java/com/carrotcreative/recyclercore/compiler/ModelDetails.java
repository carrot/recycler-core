package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCModel;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Created by kaushiksaurabh on 9/20/16.
 */

class ModelDetails
{
    private final ClassName mClassName;
    private final String mCanonicalName;
    private final String mControllerCanonicalName;

    private ModelDetails(ClassName className, String cannonicalName, String controllerCanonicalName)
    {
        mClassName = className;
        mCanonicalName = cannonicalName;
        mControllerCanonicalName = controllerCanonicalName;
    }

    public ClassName getClassName()
    {
        return mClassName;
    }

    public String getCanonicalName()
    {
        return mCanonicalName;
    }

    public String getControllerCanonicalName()
    {
        return mControllerCanonicalName;
    }

    static ModelDetails fromTypeElement(TypeElement typeElement, Types typesUtil)
    {
        ClassName clsName = ClassName.get(typeElement);
        String cannonName = ClassUtil.getCanonicalName(clsName);
        String controllerCanonicalName = null;
        try
        {
            RCModel annotation = typeElement.getAnnotation(RCModel.class);
            annotation.controller();
        }
        catch(MirroredTypeException ex)
        {
            TypeMirror typeMirror = ex.getTypeMirror();
            TypeElement tElement = (TypeElement) typesUtil.asElement(typeMirror);
            ClassName controllerCls = ClassName.get(tElement);
            controllerCanonicalName = ClassUtil.getCanonicalName(controllerCls);
        }

        return new ModelDetails(clsName, cannonName, controllerCanonicalName);
    }
}
