package com.carrotcreative.recyclercore.lint;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

public class InvalidRCModelUsageDetectorTest extends LintDetectorTestBase
{
    private static final String PATH_TEST_RESOURCES = "/src/test/java/com/";
    private static final String NO_WARNINGS = "No warnings.";

    @Override
    protected Detector getDetector()
    {
        return new InvalidRCModelUsageDetector();
    }

    @Override
    protected List<Issue> getIssues()
    {
        return Arrays.asList(InvalidRCModelUsageDetector.DUMMY_ISSUE);
    }

    @Override
    protected String getTestResourcesPath()
    {
        return PATH_TEST_RESOURCES;
    }

    public void testModelNoController() throws Exception
    {
        String[] files = {"TestModelNoController.java"};
        assertNotSame(NO_WARNINGS, lintFiles(files));
    }

    public void testModelNonBijectiveController() throws Exception
    {
        String[] files = {"TestModelDummyForBijectiveController.java", "TestModelNonBijectiveController.java", "TestControllerNonBijective.java"};
        assertNotSame(NO_WARNINGS, lintFiles(files));
    }

    public void testModelControllerWithoutAnnotation() throws Exception
    {
        String[] files = {"TestModelControllerWithoutAnnotations.java", "TestControllerWithoutAnnotations"};
        assertNotSame(NO_WARNINGS, lintFiles(files));
    }
}
