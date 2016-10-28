package com.carrotcreative.recyclercore.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

public class LintRegistry extends IssueRegistry
{
    private static final List<Issue> mIssues = Arrays.asList(
            InvalidRCModelUsageJavaDetector.ISSUE_CONTROLLER_WITHOUT_ANNOTATION
    );

    @Override
    public List<Issue> getIssues()
    {
        return mIssues;
    }
}
