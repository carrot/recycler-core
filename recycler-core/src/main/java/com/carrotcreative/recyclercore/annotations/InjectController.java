package com.carrotcreative.recyclercore.annotations;

import com.carrotcreative.recyclercore.adapter.RecyclerCoreController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kaushiksaurabh on 10/1/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectController
{
    Class<? extends RecyclerCoreController> controller();

    int layout();
}
