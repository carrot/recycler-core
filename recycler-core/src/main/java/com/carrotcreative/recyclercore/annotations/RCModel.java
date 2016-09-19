package com.carrotcreative.recyclercore.annotations;

import com.carrotcreative.recyclercore.adapter.RecyclerCoreController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kaushiksaurabh on 9/19/16.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface RCModel
{
    Class<? extends RecyclerCoreController> controller();
}
