package mumbler.graal.type;

import mumbler.graal.node.MumblerRootNode;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;

public class MumblerFunction {
    public final RootCallTarget callTarget;

    private MumblerFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }

    public static MumblerFunction create(MumblerRootNode rootNode) {
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(
                rootNode);
        return new MumblerFunction(callTarget);
    }
}
