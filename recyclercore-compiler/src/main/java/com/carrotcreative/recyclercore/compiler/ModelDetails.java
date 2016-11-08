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

    /**
     * When the annotation value is a Class, accessing the class value is not straightforward.
     * During the pre processing state, Javac does not load classes that are in the source.
     * So when we call annotation.controller() it fails with MirroredTypeException.
     * There are two possible workarounds for this.
     * <p>
     * Method 1. use getAnnotationMirrors : When getAnnotationMirrors is used it returns a set of
     * AnnotationMirror instances, one for each annotation.
     * Then locate the correct AnnotationMirror for the required Annotation.
     * Then extract the Annotation Value fron the mirror.
     * <p>
     * Method 2. Catch the MirroredTypeException. The exception contains the TypeMirror of the
     * class we need.
     * We use the second method.
     *
     * ref : https://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
     *
     * @param typeElement Represents a class or Interface program element
     * @param typesUtil   Provides utility methods for operating on types.
     * @return new instance of ModelDetails class
     */
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
