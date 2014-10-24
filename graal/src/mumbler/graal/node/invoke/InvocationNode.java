package mumbler.graal.node.invoke;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.type.MumblerFunction;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class InvocationNode extends MumblerNode {
    @Child private MumblerNode functionNode;
    @Children private final MumblerNode[] argumentsNodes;
    @Child IndirectCallNode callNode;

    public InvocationNode(MumblerNode functionNode,
            MumblerNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentsNodes = argumentNodes;
        this.callNode = Truffle.getRuntime().createIndirectCallNode();
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        MumblerFunction function = this.getFunction(virtualFrame);

        Object[] arguments = new Object[this.argumentsNodes.length];
        for (int i=0; i<arguments.length; i++) {
            arguments[i] = this.argumentsNodes[i].execute(virtualFrame);
        }

        return this.callNode.call(virtualFrame, function.callTarget, arguments);
    }

    private MumblerFunction getFunction(VirtualFrame virtualFrame) {
        try {
            return this.functionNode.executeMumblerFunction(virtualFrame);
        } catch (UnexpectedResultException e) {
            throw new UnsupportedSpecializationException(this,
                    new Node[] {this.functionNode}, e);
        }
    }
}
