package sample.rcmodel;

import com.carrotcreative.recyclercore.annotations.RCModel;

import sample.rcmodel.TestControllerNonBijective;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

/**
 * A Model that connects to a controller that is non bijective
 */
@RCModel(controller = TestControllerNonBijective.class)
public class TestModelNonBijectiveController
{
}
