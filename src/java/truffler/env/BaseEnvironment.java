package truffler.env;

import truffler.Fn;
import truffler.form.SymbolForm;

public class BaseEnvironment {
    private static abstract class BuiltinFn implements Fn {
        private final String name;

        public BuiltinFn(String name) {
            this.name = name;
        }

        @Override
        public Object eval(Environment env) {
            return this;
        }

        @Override
        public String toString() {
            return "<procedure: " + this.name;
        }
    }

    private static final Fn PLUS = new BuiltinFn("PLUS") {
            @Override
            public Object apply(Object... args) {
                long sum = 0;
                for (Object arg : args) {
                    sum += (Long) arg;
                }
                return sum;
            }
        };

    private static final Fn MINUS = new BuiltinFn("MINUS") {
            @Override
            public Object apply(Object... args) {
                switch (args.length) {
                case 1:
                    return -((Long) args[0]);
                default:
                    long diff = (Long) args[0];
                    for (int i=1; i<args.length; i++) {
                        diff -= (Long) args[i];
                    }
                    return diff;
                }
            }
        };

    private static final Fn MULT = new BuiltinFn("MULT") {
            @Override
            public Object apply(Object... args) {
                long product = 1;
                for (Object arg : args) {
                    product *= (Long) arg;
                }
                return product;
            }
        };

    private static final Fn DIV = new BuiltinFn("DIV") {
            @Override
            public Object apply(Object... args) {
                if (args.length == 1) {
                    return 1 / (Long) args[0];
                }
                long quotient = (Long) args[0];
                for (int i=1; i<args.length; i++) {
                    quotient /= (Long) args[i];
                }
                return quotient;
            }
        };

    private static final Fn EQUALS = new BuiltinFn("EQUALS") {
            @Override
            public Object apply(Object... args) {
                Long last = (Long) args[0];
                for (Object arg : args) {
                    Long current = (Long) arg;
                    if (!last.equals(current)) {
                        return false;
                    } else {
                        last = current;
                    }
                }
                return true;
            }
        };

    public static Environment getBaseEnvironment() {
        Environment env = new Environment();
        env.putValue(new SymbolForm("+"), PLUS);
        env.putValue(new SymbolForm("-"), MINUS);
        env.putValue(new SymbolForm("*"), MULT);
        env.putValue(new SymbolForm("/"), DIV);
        env.putValue(new SymbolForm("="), EQUALS);
        return env;
    }
}
