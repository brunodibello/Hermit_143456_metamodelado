package rationals;

import java.util.Set;

public interface StateFactory {
    public State create(boolean var1, boolean var2);

    public Set<State> stateSet();

    public Object clone() throws CloneNotSupportedException;
}

