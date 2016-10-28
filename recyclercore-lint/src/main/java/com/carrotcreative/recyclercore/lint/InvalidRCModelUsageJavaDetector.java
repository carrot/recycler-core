package com.carrotcreative.recyclercore.lint;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.carrotcreative.recyclercore.annotations.RCController;
import com.carrotcreative.recyclercore.annotations.RCModel;
import com.carrotcreative.recyclercore.lint.models.RCControllerNode;
import com.carrotcreative.recyclercore.lint.models.RCModelNode;

import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

import java.io.File;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import lombok.ast.Annotation;
import lombok.ast.AnnotationElement;
import lombok.ast.AnnotationValue;
import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ClassLiteral;
import lombok.ast.CompilationUnit;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.Modifiers;
import lombok.ast.Node;
import lombok.ast.TypeReference;

/**
 * Created by kaushiksaurabh on 10/18/16.
 */

public class InvalidRCModelUsageJavaDetector extends Detector implements Detector.JavaScanner
{
    // Issue 1 : Model has controller but controller does not use @RCController annotation
    // Issue 2 : Model is associated with controller that is associated with another Model
    // Issue 3 : Model is added in the Recycler Core adapter List, but the model class does not
    // use @RCModel annotations

    // Issue 1: Model has controller but controller does not use @RCController annotation
    public static final Issue ISSUE_CONTROLLER_WITHOUT_ANNOTATION = Issue.create(
            "ControllerWithoutAnnotation",
            "RCModel is using a RCController that does not use the @RCController annotation",
            "explanation TODO",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(InvalidRCModelUsageJavaDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    private Set<RCModelNode> mModelNodeSet = new HashSet<>();
    private Set<RCControllerNode> mControllerNodeSet = new HashSet<>();

    private static String getCanonicalName(ClassDeclaration classNode)
    {
        Stack<String> stack = new Stack<>();
        while(classNode != null)
        {
            stack.push(classNode.astName().toString());
            Node parent = classNode.getParent();
            if(parent instanceof ClassDeclaration)
            {
                classNode = (ClassDeclaration) parent;
            }
            else
            {
                if(parent instanceof CompilationUnit)
                {
                    CompilationUnit pkg = (CompilationUnit) parent;
                    String pkgName = pkg.astPackageDeclaration().getPackageName();
                    stack.push(pkgName);
                }
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        while(! stack.isEmpty())
        {
            sb.append(stack.pop());
            if(! stack.isEmpty())
            {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    private static String getCanonicalName(ClassLiteral classLiteral)
    {
        TypeReference typeRef = classLiteral.astTypeReference();
        Object nat = typeRef.getNativeNode();
        if(nat instanceof SingleTypeReference)
        {
            SingleTypeReference str = (SingleTypeReference) nat;
            TypeBinding tb = str.resolvedType;
            return new String(tb.qualifiedPackageName()) + "." + new String(tb
                    .qualifiedSourceName());
        }
        else if(nat instanceof QualifiedTypeReference)
        {
            QualifiedTypeReference qtr = (QualifiedTypeReference) nat;
            TypeBinding tb = qtr.resolvedType;
            return new String(tb.qualifiedPackageName()) + "." + new String(tb
                    .qualifiedSourceName());
        }
        return null;
    }

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file)
    {
        // returns true if detector applies to this file
        return true;
    }

    @Override
    public void afterCheckProject(@NonNull Context context)
    {
        super.afterCheckProject(context);
        checkModelsControllerIsAnnotated(mModelNodeSet, mControllerNodeSet, context);
    }

    @Override
    public Speed getSpeed()
    {
        return Speed.FAST;
    }

    @Override
    public AstVisitor createJavaVisitor(@NonNull final JavaContext context)
    {
        // Lombok hook into traversing our Java tree
        return new RCModelChecker(context, mModelNodeSet, mControllerNodeSet);
    }

    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes()
    {
        return Collections.<Class<? extends Node>>singletonList(Annotation.class);
    }

    @Override
    public EnumSet<Scope> getApplicableFiles()
    {
        // defines scope of our detector
        return Scope.JAVA_FILE_SCOPE;
    }

    private void checkModelsControllerIsAnnotated(Set<RCModelNode> modelSet,
                                                  Set<RCControllerNode> controllerSet, Context
                                                          context)
    {
        for(RCModelNode modelNode : modelSet)
        {
            String controllerCanonName = modelNode.getControllerCanonName();
            boolean found = false;
            for(RCControllerNode controllerNode : controllerSet)
            {
                if(controllerNode.getCanonicalName().equals(controllerCanonName))
                {
                    found = true;
                    break;
                }
            }
            if(! found)
            {
                context.report(ISSUE_CONTROLLER_WITHOUT_ANNOTATION, modelNode.getLocation(),
                        "Model Uses a Controller that does not use @RCController annotation");
            }
        }
    }

    private static class RCModelChecker extends ForwardingAstVisitor
    {
        private final JavaContext mContext;
        private final Set<RCModelNode> mRCModelNodeSet;
        private final Set<RCControllerNode> mRCControllerNodeSet;

        RCModelChecker(JavaContext context, Set<RCModelNode> modelNodeSet, Set<RCControllerNode>
                controllerNodeSet)
        {
            mContext = context;
            mRCModelNodeSet = modelNodeSet;
            mRCControllerNodeSet = controllerNodeSet;
        }

        @Override
        public boolean visitAnnotation(Annotation node)
        {
            String type = node.astAnnotationTypeReference().getTypeName();
            if(RCModel.class.getSimpleName().equals(type))
            {
                Node parent = node.getParent();
                if(parent instanceof Modifiers)
                {
                    parent = parent.getParent();
                    if(parent instanceof ClassDeclaration)
                    {
                        ClassDeclaration modelClass = (ClassDeclaration) parent;
                        String modelCanonicalName = getCanonicalName(modelClass);
                        RCModelNode modelNode = new RCModelNode(modelCanonicalName);
                        modelNode.setLocation(mContext.getLocation(modelClass));
                        for(AnnotationElement element : node.astElements())
                        {
                            AnnotationValue valueNode = element.astValue();
                            if(valueNode instanceof ClassLiteral)
                            {
                                ClassLiteral literal = (ClassLiteral) valueNode;
                                String canonName = getCanonicalName(literal);
                                if(canonName != null)
                                {
                                    modelNode.setControllerCanonicalName(canonName);
                                }
                            }
                        }
                        mRCModelNodeSet.add(modelNode);
                    }
                }
            }
            else if(RCController.class.getSimpleName().equals(type))
            {
                // TODO:
            }
            return super.visitAnnotation(node);
        }
    }
}
