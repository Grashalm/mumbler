package mumbler.graal.node;

import mumbler.graal.node.special.BeginNode;
import mumbler.graal.node.special.DefineNode;
import mumbler.graal.node.special.DefineNodeFactory;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class MumblerRootNode extends RootNode {
    @Child private final BeginNode bodyNode;

    private MumblerRootNode(BeginNode bodyNode,
            FrameDescriptor frameDescriptor) {
        super(null, frameDescriptor);
        this.bodyNode = bodyNode;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.bodyNode.execute(virtualFrame);
    }

    public static DefineNode addFormalParameter(int index,
            FrameSlot frameSlot) {
        return DefineNodeFactory.create(new ReadArgumentNode(index), frameSlot);
    }

    public static MumblerRootNode lambda(FrameSlot[] formalParameters,
            MumblerNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        MumblerNode[] allBodyNodes = new MumblerNode[formalParameters.length
                                                     + bodyNodes.length];
        for (int i=0; i<formalParameters.length; i++) {
            allBodyNodes[i] = addFormalParameter(i, formalParameters[i]);
        }
        System.arraycopy(bodyNodes, 0, allBodyNodes, formalParameters.length,
                bodyNodes.length);

        return new MumblerRootNode(new BeginNode(allBodyNodes),
                frameDescriptor);
    }
}
