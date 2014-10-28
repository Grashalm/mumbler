package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.invoke.InvocationNode;
import mumbler.graal.type.MumblerList;
import mumbler.graal.type.MumblerNull;

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

    public InvocationNode convertToInvocationNode() {
        MumblerNode[] body = new MumblerNode[this.list.cdr.length()];
        MumblerList l = this.list.cdr;
        int i = 0;
        while (l != MumblerNull.EMPTY) {
            body[i] = (MumblerNode) l.car;
            if (body[i] instanceof LiteralListNode) {
                body[i] = ((LiteralListNode) body[i]).convertToInvocationNode();
            }
            i++;
            l = l.cdr;
        }
        return new InvocationNode((MumblerNode) this.list.car, body);
    }
}
