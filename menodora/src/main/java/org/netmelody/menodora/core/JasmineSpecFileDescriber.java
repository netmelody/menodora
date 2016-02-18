package org.netmelody.menodora.core;

import java.io.InputStreamReader;
import java.util.Stack;

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

public final class JasmineSpecFileDescriber {

    private final Description description;

    public JasmineSpecFileDescriber(String specResource, Class<?> suiteClass) {
        description = Description.createSuiteDescription(specResource);
        AstRoot tree = parseJavascript(specResource, suiteClass);
        tree.visit(new SpecNodeVisitor(suiteClass, description));
    }

    public Description getDescription() {
        return description;
    }
    
    private AstRoot parseJavascript(String specResource, Class<?> suiteClass) {
        CompilerEnvirons compilerEnv = new CompilerEnvirons();
        ErrorReporter errorReporter = compilerEnv.getErrorReporter();

        Parser parser = new Parser(compilerEnv, errorReporter);
        try {
            return parser.parse(new InputStreamReader(suiteClass.getResourceAsStream(specResource)), specResource, 1);
        } catch (Exception e) {
            throw new IllegalStateException(specResource, e);
        }
    }
    
    private static class SpecNodeVisitor implements NodeVisitor {
        private static class NodeDesc {
            int depth;
            Description description;
            
            public static NodeDesc of(int nodeDepth, Description nodeDescription) {
                final NodeDesc result = new NodeDesc();
                result.depth = nodeDepth;
                result.description = nodeDescription;
                return result;
            }
        }
        
        private final Class<?> suiteClass;
        private final Stack<NodeDesc> stack = new Stack<NodeDesc>();
        
        public SpecNodeVisitor(Class<?> suiteClass, Description rootDescription) {
            this.suiteClass = suiteClass;
            stack.push(NodeDesc.of(0, rootDescription));
        }
        
        @Override
        public boolean visit(AstNode node) {
            if (node.getType() != Token.NAME) {
                return true;
            }
            
            if ("describe".equals(node.getString())) {
                final String desc = ((StringLiteral)((FunctionCall)node.getParent()).getArguments().get(0)).getValue();
                Description suiteDesc = Description.createSuiteDescription(desc);
                
                while (stack.peek().depth >= node.depth()) {
                    stack.pop();
                }
                stack.peek().description.addChild(suiteDesc);
                stack.push(NodeDesc.of(node.depth(), suiteDesc));
                return true;
            }
            
            if ("it".equals(node.getString())) {
                final String desc = ((StringLiteral)((FunctionCall)node.getParent()).getArguments().get(0)).getValue();
                Description testDescription = Description.createTestDescription(this.suiteClass, desc);
                
                while (stack.peek().depth >= node.depth()) {
                    stack.pop();
                }
                stack.peek().description.addChild(testDescription);
                return false;
            }
            
            return true;
        }
    }
}
