package com.carrotcreative.recyclercore.lint;

import com.android.tools.lint.detector.api.Issue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

public class LintRegistryTest
{
    private LintRegistry mLintRegistry;

    @Before
    public void setUp()
    {
        mLintRegistry = new LintRegistry();
    }

    @Test
    public void test_issuesCount()
    {
        int size = mLintRegistry.getIssues().size();
        Assert.assertEquals(size, 1);
    }

    @Test
    public void test_validIssues()
    {
        List<Issue> issues = mLintRegistry.getIssues();
        Assert.assertTrue(issues.contains(InvalidRCModelUsageJavaDetector.ISSUE_CONTROLLER_WITHOUT_ANNOTATION));
    }
}
