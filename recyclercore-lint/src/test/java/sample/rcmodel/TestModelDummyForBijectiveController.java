package sample.rcmodel;

import com.carrotcreative.recyclercore.annotations.RCModel;

import sample.rcmodel.TestControllerNonBijective;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

/**
 * A dummy object to make the Controller non bijective
 */
@RCModel(controller = TestControllerNonBijective.class)
public class TestModelDummyForBijectiveController
{
}
