package com.carrotcreative.recyclercore.lint;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.Node;

/**
 * Created by kaushiksaurabh on 10/18/16.
 */

public class InvalidRCModelUsageDetector extends Detector implements Detector.JavaScanner
{
    // Issue 1 : Model has controller but controller does not use @RCController annotation
    // Issue 2 : Model is associated with controller that is associated with another Model
    // Issue 3 : Model is added in the Recycler Core adapter List, but the model class does not
    // use @RCModel annotations

    public static final Issue DUMMY_ISSUE = Issue.create(
            "id",
            "description",
            "explanation",
            Category.CORRECTNESS,
            8, // priority
            Severity.ERROR,
            new Implementation(InvalidRCModelUsageDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file)
    {
        // returns true if detector applies to this file
        return true;
    }

    @Override
    public EnumSet<Scope> getApplicableFiles()
    {
        // defines scope of our detector
        return Scope.JAVA_FILE_SCOPE;
    }

    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes()
    {
        return Arrays.<Class<? extends Node>>asList(ClassDeclaration.class);
    }

    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context)
    {
        // Lombok hook into traversing our Java tree
        return new RCModelChecker(context);
    }

    private static class RCModelChecker extends ForwardingAstVisitor
    {
        private final JavaContext mContext;

        RCModelChecker(JavaContext context)
        {
            mContext = context;
        }

        @Override
        public boolean visitClassDeclaration(ClassDeclaration node)
        {
            System.out.println("desc=" + node.getDescription() + "::name=" + node.astName());
            // For each class declaration that is detected when traversing the AST, this method
            // is called.
            // mContext.report(DUMMY_ISSUE, Location.create(mContext.file), DUMMY_ISSUE
            //        .getBriefDescription(TextFormat.TEXT));
            return super.visitClassDeclaration(node);
        }
    }
}
