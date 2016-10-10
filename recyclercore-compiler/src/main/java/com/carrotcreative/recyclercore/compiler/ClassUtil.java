package com.carrotcreative.recyclercore.compiler;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by kaushik on 9/30/16.
 */

class ClassUtil
{
    static String getClassName(TypeElement type, String packageName)
    {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    static String getPackageName(TypeElement element, Elements elementUtils)
    {
        return elementUtils.getPackageOf(element).getQualifiedName().toString();
    }
}
