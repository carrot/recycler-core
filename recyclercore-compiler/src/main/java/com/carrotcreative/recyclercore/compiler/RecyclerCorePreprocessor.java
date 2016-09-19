package com.carrotcreative.recyclercore.compiler;

import com.carrotcreative.recyclercore.annotations.RCController;
import com.carrotcreative.recyclercore.annotations.RCModel;
import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
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
    public synchronized void init(ProcessingEnvironment processingEnvironment)
    {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mTypes = processingEnvironment.getTypeUtils();
        mFiler = processingEnvironment.getFiler();
        mPreProcessorHelper = new PreProcessorHelper(mElements, mTypes, mFiler);
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
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        return false;
    }
}
