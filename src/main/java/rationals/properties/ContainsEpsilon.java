/*
 * Decompiled with CFR 0.137.
 */
package rationals.properties;

import java.util.Iterator;
import java.util.Set;
import rationals.Automaton;
import rationals.State;
import rationals.StateFactory;
import rationals.properties.UnaryTest;
import rationals.transformations.TransformationsToolBox;

public class ContainsEpsilon
implements UnaryTest {
    @Override
    public boolean test(Automaton a) {
        Iterator<State> i = a.initials().iterator();
        Set<State> s = a.getStateFactory().stateSet();
        while (i.hasNext()) {
            State st = i.next();
            if (st.isTerminal()) {
                return true;
            }
            s.add(st);
            Set<State> cl = TransformationsToolBox.epsilonClosure(s, a);
            if (!TransformationsToolBox.containsATerminalState(cl)) continue;
            return true;
        }
        return false;
    }
}

