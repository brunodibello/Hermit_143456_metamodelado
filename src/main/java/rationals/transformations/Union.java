/*
 * Decompiled with CFR 0.137.
 */
package rationals.transformations;

import java.util.HashMap;
import java.util.Set;
import rationals.Automaton;
import rationals.State;
import rationals.Transition;
import rationals.transformations.BinaryTransformation;

public class Union
implements BinaryTransformation {
    @Override
    public Automaton transform(Automaton a, Automaton b) {
        Automaton ap = (Automaton)a.clone();
        HashMap<State, State> map = new HashMap<State, State>();
        for (State e : b.states()) {
            map.put(e, ap.addState(e.isInitial(), e.isTerminal()));
        }
        for (Transition t : b.delta()) {
            ap.addTransition(new Transition((State)map.get(t.start()), t.label(), (State)map.get(t.end())), null);
        }
        return ap;
    }
}

