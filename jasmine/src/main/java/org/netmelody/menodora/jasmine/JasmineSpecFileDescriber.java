package org.netmelody.menodora.jasmine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

public final class JasmineSpecFileDescriber implements Describable {

    private final String specResource;
    private final Class<?> suiteClass;

    public JasmineSpecFileDescriber(String specResource, Class<?> suiteClass) {
        this.specResource = specResource;
        this.suiteClass = suiteClass;
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(specResource);
        AstRoot tree = parseJavascript(specResource, suiteClass);
        tree.visit(new SpecNodeVisitor(suiteClass, description));
        return description;
    }

    private AstRoot parseJavascript(String specResource, Class<?> suiteClass) {
        CompilerEnvirons compilerEnv = new CompilerEnvirons();
        ErrorReporter errorReporter = compilerEnv.getErrorReporter();

        Parser parser = new Parser(compilerEnv, errorReporter);
        try {
            return parser.parse(new InputStreamReader(suiteClass.getClassLoader().getResourceAsStream(specResource)), specResource, 1);
        } catch (IOException e) {
            throw new IllegalStateException(specResource, e);
        }
    }

    private static class SpecNodeVisitor implements NodeVisitor {
        private static class NodeDesc {
            public final int depth;
            public final Description description;

            public NodeDesc(int depth, Description description) {
                this.depth = depth;
                this.description = description;
            }
        }

        private final Class<?> suiteClass;
        private final Deque<NodeDesc> nodeStack = new ArrayDeque<NodeDesc>();

        public SpecNodeVisitor(Class<?> suiteClass, Description rootDescription) {
            this.suiteClass = suiteClass;
            this.nodeStack.push(new NodeDesc(0, rootDescription));
        }

        @Override
        public boolean visit(AstNode node) {
            if (node.getType() != Token.NAME || node.getParent().getType() != Token.CALL) {
                return true;
            }

            String nodeValue = node.getString();
            if ("describe".equals(nodeValue)) {
                String description = firstStringArgumentOf(node);
                Description suiteDescription = Description.createSuiteDescription(description);

                revertStackTo(node.depth());
                nodeStack.peek().description.addChild(suiteDescription);
                nodeStack.push(new NodeDesc(node.depth(), suiteDescription));
                return true;
            } else if ("it".equals(nodeValue)) {
                String description = firstStringArgumentOf(node);
                Description testDescription = Description.createTestDescription(suiteClass, description);

                revertStackTo(node.depth());
                nodeStack.peek().description.addChild(testDescription);
                return false;
            }

            return true;
        }

        public static String firstStringArgumentOf(AstNode node) {
            return ((StringLiteral) ((FunctionCall) node.getParent()).getArguments().get(0)).getValue();
        }

        public void revertStackTo(int nodeDepth) {
            while (nodeStack.peek().depth >= nodeDepth) {
                nodeStack.pop();
            }
        }

    }
}
