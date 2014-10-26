package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.type.MumblerList;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LiteralListNode extends MumblerNode {
    private final MumblerList list;

    public LiteralListNode(MumblerList list) {
        this.list = list;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.list;
    }

    @Override
    public MumblerList executeMumblerList(VirtualFrame virtualFrame) {
        return this.list;
    }
}
