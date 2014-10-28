package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

// TODO: Add NodeInfo
public class BooleanNode extends MumblerNode {
    public static final BooleanNode TRUE = new BooleanNode(true);
    public static final BooleanNode FALSE = new BooleanNode(false);

    private final boolean bool;

    private BooleanNode(boolean bool) {
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
