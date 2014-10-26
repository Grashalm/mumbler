package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

// TODO: Add NodeInfo
public class BooleanNode extends MumblerNode {

    private final boolean bool;

    public BooleanNode(boolean bool) {
        this.bool = bool;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.bool;
    }

    @Override
    public boolean executeBoolean(VirtualFrame virtualFrame) {
        return this.bool;
    }
}
