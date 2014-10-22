package mumbler.graal.fromsimple;

import mumbler.graal.fromsimple.env.Environment;

public abstract class Function implements Evaluatable {
    @Override
    public Object eval(Environment env) {
        return this;
    }

    public abstract Object apply(Object... args);
}
