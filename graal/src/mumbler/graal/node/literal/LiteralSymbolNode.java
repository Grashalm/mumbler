package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.type.MumblerSymbol;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LiteralSymbolNode extends MumblerNode {
    private final MumblerSymbol symbol;

    public LiteralSymbolNode(MumblerSymbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.symbol;
    }

    @Override
    public MumblerSymbol executeMumblerSymbol(VirtualFrame virtualFrame) {
        return this.symbol;
    }
}
