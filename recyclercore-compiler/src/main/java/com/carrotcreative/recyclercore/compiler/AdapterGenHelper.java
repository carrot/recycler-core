package com.carrotcreative.recyclercore.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

/**
 * Created by kaushik on 9/30/16.
 */

class AdapterGenHelper
{
    private static final String PACKAGE = "com.carrotcreative.recyclercore";
    private static final String CLASSNAME = "RecyclerCoreAdapter";
    private static final ClassName VIEW = ClassName.get("android.view", "View");
    private static final ClassName VIEW_GROUP = ClassName.get("android.view", "ViewGroup");
    private static final ClassName LAYOUT_INFLATER = ClassName.get("android.view", "LayoutInflater");
    private static final ClassName RCAdapter = ClassName.get("com.carrotcreative.recyclercore.adapter", "RecyclerCoreBaseAdapter");
    private static final ClassName RCController = ClassName.get("com.carrotcreative.recyclercore.adapter", "RecyclerCoreController");

    static JavaFile brewAdapter(Map<ModelDetails, ControllerDetails> map)
    {
        ParameterSpec param = ParameterSpec.builder(List.class, "list").build();
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(param)
                .addStatement("super(list)")
                .addStatement("initModelToViewType()")
                .build();

        // Create a factory method for controller, that takes the parent view, controller class and returns a new object.
        ParameterizedTypeName controllerParam = ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class));
        ParameterSpec viewGroupParam = ParameterSpec.builder(VIEW_GROUP, "parent").build();
        MethodSpec.Builder newInstanceControllerMethod = MethodSpec.methodBuilder("newInstanceController")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(RCController)
                .addParameter(controllerParam, "clazz")
                .addParameter(viewGroupParam)
                .addStatement("$T view = null", VIEW);
        {
            String statement = "view = $T.from(parent.getContext()).inflate($L, parent, false)";
            newInstanceControllerMethod.beginControlFlow("switch(clazz.getCanonicalName())");
            for(ControllerDetails controller : map.values())
            {
                ClassName returnCls = ClassName.get(controller.getPackageName(), controller.getClassName());
                String canonicalName = controller.getPackageName() + "." + controller.getClassName();
                newInstanceControllerMethod
                        .addCode("case $S:\n", canonicalName )
                        .addStatement(statement, LAYOUT_INFLATER, controller.getLayoutId())
                        .addStatement("return new $T(view)", returnCls);
            }
            newInstanceControllerMethod.endControlFlow();
            newInstanceControllerMethod.addStatement("throw new IllegalStateException(\"Unregistered Controller requested :\")");
        }

        // initModelToViewType that maps the model to its viewtype
        MethodSpec.Builder initModelToViewTypeBuilder = MethodSpec.methodBuilder("initModelToViewType")
                .addModifiers(Modifier.PRIVATE);
        int index = 0;
        String putModelStatement = "mModelViewTypeMap.put($T.class,$L)";
        String putControllerStatement = "mControllerSparseArray.put($L, $T.class);";
        for(ModelDetails model : map.keySet())
        {
            ClassName modelCls = ClassName.get(model.getPackageName(), model.getClassName());
            initModelToViewTypeBuilder.addStatement(putModelStatement, modelCls, index);
            ClassName controllerCls = ClassName.get(model.getControllerPackageName(), model.getControllerClassName());
            initModelToViewTypeBuilder.addStatement(putControllerStatement, index, controllerCls);
            index++;
        }

        TypeSpec.Builder result = TypeSpec.classBuilder(CLASSNAME)
                .addModifiers(Modifier.PUBLIC)
                .superclass(RCAdapter)
                .addMethod(constructor)
                .addMethod(initModelToViewTypeBuilder.build())
                .addMethod(newInstanceControllerMethod.build());

        return JavaFile.builder(PACKAGE, result.build())
                .addFileComment("Generated code from Recycler Core. Do not modify!")
                .build();
    }
}
