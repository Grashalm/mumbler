package mumbler.graal.fromsimple;

import mumbler.graal.fromsimple.env.Environment;

public interface Evaluatable {
    public Object eval(Environment env);
}
