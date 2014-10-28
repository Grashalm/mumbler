package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.type.MumblerFunction;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LambdaNode extends MumblerNode {
    private final MumblerFunction function;

    public LambdaNode(MumblerFunction function) {
        this.function = function;
    }

    @Override
    @Specialization
    public MumblerFunction executeMumblerFunction(VirtualFrame virtualFrame) {
        return this.function;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.function;
    }
}
