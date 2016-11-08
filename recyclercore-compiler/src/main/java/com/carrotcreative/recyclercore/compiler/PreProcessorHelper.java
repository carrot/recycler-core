package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCController;
import com.carrotcreative.recyclercore.annotations.RCModel;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Created by kaushiksaurabh on 9/19/16.
 */

class PreProcessorHelper
{
    private Types mTypes;
    private ProcessingEnvironment mEnvironment;

    PreProcessorHelper(Types types, ProcessingEnvironment env)
    {
        mTypes = types;
        mEnvironment = env;
    }

    Set<ModelDetails> findRCModels(RoundEnvironment env)
    {
        Set<ModelDetails> classSet = new LinkedHashSet<>();
        for(Element element : env.getElementsAnnotatedWith(RCModel.class))
        {
            ModelDetails clsDetails = ModelDetails.fromTypeElement((TypeElement) element, mTypes);
            classSet.add(clsDetails);
        }
        return classSet;
    }

    Set<ControllerDetails> findControllers(RoundEnvironment env)
    {
        Set<ControllerDetails> extendedController = new LinkedHashSet<>();
        for(Element element : env.getElementsAnnotatedWith(RCController.class))
        {
            ControllerDetails classDetails = ControllerDetails.fromTypeElement((TypeElement) element);
            extendedController.add(classDetails);
        }
        return extendedController;
    }

    Map<ModelDetails, ControllerDetails> createModelControllerMap(Set<ModelDetails> models,
                                                                  Set<ControllerDetails>
                                                                          controllers)
    {
        Map<ModelDetails, ControllerDetails> map = new LinkedHashMap<>();
        for(ModelDetails model : models)
        {
            ControllerDetails controller = findController(controllers, model);
            if(controller != null)
            {
                map.put(model, controller);
            }
        }
        return map;
    }

    private ControllerDetails findController(Set<ControllerDetails> controller, ModelDetails model)
    {
        for(ControllerDetails controllerClassDetails : controller)
        {
            if(controllerClassDetails.getCanonicalName().equals(model.getControllerCanonicalName()))
            {
                return controllerClassDetails;
            }
        }
        error("No controller found for model %s", model.getClassName());
        return null;
    }

    private void error(String message, Object... args)
    {
        if(args.length > 0)
        {
            message = String.format(message, args);
        }
        mEnvironment.getMessager().printMessage(ERROR, message);
    }
}
