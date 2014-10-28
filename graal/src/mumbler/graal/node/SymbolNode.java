package mumbler.graal.node;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class SymbolNode extends MumblerNode {
    public abstract FrameSlot getSlot();

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return virtualFrame.getLong(this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return virtualFrame.getBoolean(this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return virtualFrame.getObject(this.getSlot());
    }

    @Specialization(contains = {"readLong", "readBoolean", "readObject"})
    protected Object read(VirtualFrame virtualFrame) {
        return virtualFrame.getValue(this.getSlot());
    }
}
