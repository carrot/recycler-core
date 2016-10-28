package com.carrotcreative.recyclercore.lint;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

public class InvalidRCModelUsageJavaDetectorTest extends LintDetectorTestBase
{
    private static final String PATH_TEST_RESOURCES = "/src/test/java/sample/rcmodel/";
    private static final String NO_WARNINGS = "No warnings.";

    @Override
    protected Detector getDetector()
    {
        return new InvalidRCModelUsageJavaDetector();
    }

    @Override
    protected List<Issue> getIssues()
    {
        return Arrays.asList(InvalidRCModelUsageJavaDetector.ISSUE_CONTROLLER_WITHOUT_ANNOTATION);
    }

    @Override
    protected String getTestResourcesPath()
    {
        return PATH_TEST_RESOURCES;
    }

    /**
     * We have a model but this model is not associated with any controller. Lint should give
     * a warning.
     * TODO: Does not work as expected
     *
     * @throws Exception
     */
    public void testModelNoController() throws Exception
    {
        String[] files = {"TestModelNoController.java"};
        String res = lintFiles(files);
        assertSame(NO_WARNINGS, res);
    }

    public void testModelNonBijectiveController() throws Exception
    {
        String[] files = {
                "TestModelDummyForBijectiveController.java",
                "TestModelNonBijectiveController.java",
                "TestControllerNonBijective.java"
        };
        assertSame(NO_WARNINGS, lintFiles(files));
    }

    /**
     * We have a Model that uses the RCModel annotation but the controller associated with
     * the model does not used the @RCController annotations.
     * TODO: Lint should throw an error
     *
     * @throws Exception
     */
    public void testModelControllerWithoutAnnotation() throws Exception
    {
        String[] files = {
                "TestModelControllerWithoutAnnotations.java",
                "TestControllerWithoutAnnotations.java"
        };
        assertNotSame(NO_WARNINGS, lintFiles(files));
    }
}
