package org.netmelody.menodora.core;

import java.io.InputStreamReader;
import java.util.Stack;

import org.junit.runner.Description;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.ScriptNode;

public final class JasmineSpecFileDescriber {

    private final Description description;

    public JasmineSpecFileDescriber(String specResource, Class<?> suiteClass) {
        description = Description.createSuiteDescription(specResource);
        AstRoot tree = parseJavascript(specResource, suiteClass);
        new Describer(description, suiteClass).appendDescriptionOf(tree);
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
    
    private static final class Describer {
        
        private final Class<?> suiteClass;
        private Description description;
        
        public void appendDescriptionOf(AstRoot tree) {
            parseFunction(tree);
        }

        public Describer(Description description, Class<?> suiteClass) {
            this.description = description;
            this.suiteClass = suiteClass;
        }

        public void parseFunction(ScriptNode node) {
            Stack<ScriptNode> functions = new Stack<ScriptNode>();
            int functionCount = node.getFunctionCount();
            for (int i = functionCount - 1; i >= 0; i--) {
                FunctionNode functionNode = node.getFunctionNode(i);
                functions.push(functionNode);
            }
            parse(node, functions);
        }
        
        private void parse(Node node, Stack<ScriptNode> functions) {
            if (node == null) {
                return;
            }
            Description parentDesc = description;
            if (node.getType() == Token.NAME ) {
                if ("describe".equals(node.getString())) {
                    final Description suiteDesc = Description.createSuiteDescription(node.getNext().getString());
                    description.addChild(suiteDesc);
                    description = suiteDesc;
                }
                if ("it".equals(node.getString())) {
                    description.addChild(Description.createTestDescription(this.suiteClass,
                                                                           node.getNext().getString()));
                }
            }
            if (node.getType() == Token.FUNCTION && !(node instanceof FunctionNode) && !(node instanceof AstRoot)) {
                parseFunction(functions.pop());
            }
            
            parse(node.getFirstChild(), functions);
            parse(node.getNext(), functions);
            description = parentDesc;
        }
    }
}
