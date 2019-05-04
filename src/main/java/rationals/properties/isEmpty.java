/*
 * Decompiled with CFR 0.137.
 */
package rationals.properties;

import java.util.Iterator;
import java.util.Set;
import rationals.Automaton;
import rationals.State;
import rationals.properties.UnaryTest;

public class isEmpty
implements UnaryTest {
    @Override
    public boolean test(Automaton a) {
        Iterator<State> i = a.accessibleStates().iterator();
        while (i.hasNext()) {
            if (!i.next().isTerminal()) continue;
            return false;
        }
        return true;
    }
}

