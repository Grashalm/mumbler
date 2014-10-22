package mumbler.graal;

import mumbler.graal.type.MumblerFunction;
import mumbler.graal.type.MumblerList;
import mumbler.graal.type.MumblerNull;
import mumbler.graal.type.MumblerSymbol;

import com.oracle.truffle.api.dsl.TypeCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({long.class, boolean.class, MumblerFunction.class,
    MumblerSymbol.class, MumblerNull.class, MumblerList.class})
public abstract class MumblerTypes {
    @TypeCheck
    public boolean isMumblerNull(Object value) {
        return value == MumblerNull.EMPTY;
    }

    @TypeCast
    public MumblerNull asMumblerNull(Object value) {
        assert this.isMumblerNull(value);
        return MumblerNull.EMPTY;
    }
}
