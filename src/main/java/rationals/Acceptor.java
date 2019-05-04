/*
 * Decompiled with CFR 0.137.
 */
package rationals;

import java.util.List;
import java.util.Set;
import rationals.State;

public interface Acceptor {
    public Set<State> steps(List<?> var1);
}

