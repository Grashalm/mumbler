package mumbler.graal.type;

import static java.util.Arrays.asList;

import java.util.List;

public class MumblerList {
    public final Object car;
    public final MumblerList cdr;

    protected MumblerList() {
        this.car = null;
        this.cdr = null;
    }

    private MumblerList(Object car, MumblerList cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public static MumblerList list(Object... objs) {
        return list(asList(objs));
    }

    public static MumblerList list(List<Object> objs) {
        MumblerList l = MumblerNull.EMPTY;
        for (int i=objs.size()-1; i>=0; i--) {
            l = l.cons(objs.get(i));
        }
        return l;
    }

    public MumblerList cons(Object obj) {
        return new MumblerList(obj, this);
    }

    public long length() {
        if (this == MumblerNull.EMPTY) {
            return 0;
        }

        long len = 1;
        MumblerList l = this.cdr;
        while (l != MumblerNull.EMPTY) {
            len++;
            l = l.cdr;
        }
        return len;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MumblerList)) {
            return false;
        }
        if (this == MumblerNull.EMPTY && other == MumblerNull.EMPTY) {
            return true;
        }

        MumblerList that = (MumblerList) other;
        if (this.cdr == MumblerNull.EMPTY && that.cdr != MumblerNull.EMPTY) {
            return false;
        }
        return this.car.equals(that.car) && this.cdr.equals(that.cdr);
    }

    @Override
    public String toString() {
        if (this == MumblerNull.EMPTY) {
            return "()";
        }

        StringBuilder b = new StringBuilder("(" + this.car);
        MumblerList rest = this.cdr;
        while (rest != null && rest != MumblerNull.EMPTY) {
            b.append(" ");
            b.append(rest.car);
            rest = rest.cdr;
        }
        b.append(")");
        return b.toString();
    }
}
