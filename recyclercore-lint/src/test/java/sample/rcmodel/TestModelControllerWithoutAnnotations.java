package sample.rcmodel;

import com.carrotcreative.recyclercore.annotations.RCModel;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

/**
 * A Model that binds to a controller that does not implement @RCController
 */
@RCModel(controller = TestControllerWithoutAnnotations.class)
public class TestModelControllerWithoutAnnotations
{
}
