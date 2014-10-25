package mumbler.graal.node.special;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.ReadArgumentNode;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class LambdaNode extends MumblerNode {
    @Children private final MumblerNode[] bodyNodes;

    public LambdaNode(MumblerNode[] bodyNodes) {
        this.bodyNodes = bodyNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        int secondToLast = this.bodyNodes.length - 2;
        for (int i=0; i<secondToLast; i++) {
            this.bodyNodes[i].execute(virtualFrame);
        }
        return this.bodyNodes[secondToLast + 1].execute(virtualFrame);
    }

    public static DefineNode addFormalParameter(int index,
            FrameSlot frameSlot) {
        return DefineNodeFactory.create(new ReadArgumentNode(index), frameSlot);
    }

    public static LambdaNode lambda(FrameSlot[] formalParameters,
            MumblerNode[] bodyNodes) {
        MumblerNode[] allBodyNodes = new MumblerNode[formalParameters.length
                                                     + bodyNodes.length];
        for (int i=0; i<formalParameters.length; i++) {
            allBodyNodes[i] = addFormalParameter(i, formalParameters[i]);
        }
        System.arraycopy(bodyNodes, 0, allBodyNodes, formalParameters.length,
                bodyNodes.length);

        return new LambdaNode(allBodyNodes);
    }
}
