package mumbler.graal.node;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class SymbolNode extends MumblerNode {
    private final FrameSlot slot;

    public SymbolNode(FrameSlot slot) {
        this.slot = slot;
    }




    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return virtualFrame.getValue(this.slot);
    }
}
