/*
 * Decompiled with CFR 0.137.
 */
package rationals;

import java.util.Set;
import rationals.State;

public interface StateFactory {
    public State create(boolean var1, boolean var2);

    public Set<State> stateSet();

    public Object clone() throws CloneNotSupportedException;
}

