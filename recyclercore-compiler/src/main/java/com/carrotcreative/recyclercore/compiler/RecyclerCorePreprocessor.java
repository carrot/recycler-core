package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCController;
import com.carrotcreative.recyclercore.annotations.RCModel;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class RecyclerCorePreprocessor extends AbstractProcessor
{
    private Elements mElements;
    private Types mTypes;
    private Filer mFiler;
    private PreProcessorHelper mPreProcessorHelper;

    @Override
    public synchronized void init(ProcessingEnvironment env)
    {
        super.init(env);
        mElements = env.getElementUtils();
        mTypes = env.getTypeUtils();
        mFiler = env.getFiler();
        mPreProcessorHelper = new PreProcessorHelper(mElements, mTypes, mFiler, env);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> supportedTypes = new LinkedHashSet<>();
        supportedTypes.add(RCModel.class.getCanonicalName());
        supportedTypes.add(RCController.class.getCanonicalName());
        return supportedTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env)
    {
        Set<ModelDetails> models = mPreProcessorHelper.findRCModels(env);
        Set<ControllerDetails> controllers = mPreProcessorHelper.findControllers(env);
        Map<ModelDetails, ControllerDetails> map = mPreProcessorHelper.createModelControllerMap(models, controllers);
        try
        {
            if(!map.isEmpty())
            {
                AdapterGenHelper.brewAdapter(map).writeTo(mFiler);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
