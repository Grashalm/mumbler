package mumbler.graal.node.special;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.type.MumblerNull;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.utilities.ConditionProfile;

public class IfNode extends MumblerNode {
    @Child private MumblerNode testNode;
    @Child private MumblerNode thenNode;
    @Child private MumblerNode elseNode;

    private final ConditionProfile conditionProfile =
            ConditionProfile.createBinaryProfile();

    public IfNode(MumblerNode testNode, MumblerNode thenNode,
            MumblerNode elseNode) {
        this.testNode = testNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (this.conditionProfile.profile(this.testResult(virtualFrame))) {
            return this.thenNode.execute(virtualFrame);
        } else {
            return this.elseNode.execute(virtualFrame);
        }
    }

    private boolean testResult(VirtualFrame virtualFrame) {
        try {
            return this.testNode.executeBoolean(virtualFrame);
        } catch (UnexpectedResultException e) {
            Object result = this.testNode.execute(virtualFrame);
            // All objects are truthy except the empty list.
            return result != MumblerNull.EMPTY;
        }
    }
}
