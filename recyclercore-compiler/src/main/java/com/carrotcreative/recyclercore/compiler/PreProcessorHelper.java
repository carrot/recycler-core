package com.carrotcreative.recyclercore.compiler;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by kaushiksaurabh on 9/19/16.
 */

public class PreProcessorHelper
{
    private Elements mElements;
    private Types mTypes;
    private Filer mFiler;

    PreProcessorHelper(Elements elements, Types types, Filer filer)
    {
        mElements = elements;
        mTypes = types;
        mFiler = filer;
    }


}
