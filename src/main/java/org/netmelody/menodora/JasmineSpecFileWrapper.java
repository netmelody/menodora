package org.netmelody.menodora;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.runner.Description;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.FunctionNode;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ScriptOrFnNode;
import org.mozilla.javascript.Token;

public class JasmineSpecFileWrapper {

    private final File specFile;

    public JasmineSpecFileWrapper(File specFile) {
        this.specFile = specFile;
    }

    public Description getDescription() {
        final Description description = Description.createSuiteDescription(specFile.getPath());
        ScriptOrFnNode tree = parseJavascript(this.specFile);
        new Describer(description).appendDescriptionOf(tree);
        return description;
    }
    
    private ScriptOrFnNode parseJavascript(File file) {
        CompilerEnvirons compilerEnv = new CompilerEnvirons();
        ErrorReporter errorReporter = compilerEnv.getErrorReporter();

        Parser parser = new Parser(compilerEnv, errorReporter);
        String sourceURI;

        try {
            sourceURI = file.getCanonicalPath();
        } catch (IOException e) {
            sourceURI = file.toString();
        }

        try {
            return parser.parse(new FileReader(file), sourceURI, 1);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static final class Describer {
        
        private Description description;

        public void appendDescriptionOf(ScriptOrFnNode tree) {
            parseFunction(tree);
        }

        public Describer(Description description) {
            this.description = description;
        }

        public void parseFunction(ScriptOrFnNode node) {
            Stack<ScriptOrFnNode> functions = new Stack<ScriptOrFnNode>();
            int functionCount = node.getFunctionCount();
            for (int i = functionCount - 1; i >= 0; i--) {
                FunctionNode functionNode = node.getFunctionNode(i);
                functions.push(functionNode);
            }
            parse(node, functions);
        }
        
        private void parse(Node node, Stack<ScriptOrFnNode> functions) {
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
                    description.addChild(Description.createTestDescription(Object.class, node.getNext().getString()));
                }
                System.out.println(node.getString());
            }
            if (node.getType() == Token.STRING) {
                System.out.println(node.getString());
            }
            if (node.getType() == Token.FUNCTION && !(node instanceof ScriptOrFnNode)) {
                parseFunction(functions.pop());
            }
            
            parse(node.getFirstChild(), functions);
            description = parentDesc;
            
            parse(node.getNext(), functions);
        }
    }
}
