package mumbler.graal.node;

import com.oracle.truffle.api.frame.VirtualFrame;

// TODO: Add @NodeInfo
public class NumberNode extends MumblerNode {

    private final long num;

    public NumberNode(long num) {
        this.num = num;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.num;
    }

    @Override
    public long executeLong(VirtualFrame virtualFrame) {
        return this.num;
    }
}
