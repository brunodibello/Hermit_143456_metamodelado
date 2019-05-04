/*
 * Decompiled with CFR 0.137.
 */
package rationals.transformations;

import java.util.HashMap;
import java.util.Set;
import rationals.Automaton;
import rationals.State;
import rationals.Transition;
import rationals.transformations.UnaryTransformation;

public class Pruner
implements UnaryTransformation {
    @Override
    public Automaton transform(Automaton a) {
        HashMap<State, State> conversion = new HashMap<State, State>();
        Automaton b = new Automaton();
        for (State e : a.accessibleAndCoAccessibleStates()) {
            conversion.put(e, b.addState(e.isInitial(), e.isTerminal()));
        }
        for (Transition t : a.delta()) {
            State bs = (State)conversion.get(t.start());
            State be = (State)conversion.get(t.end());
            if (bs == null || be == null) continue;
            b.addTransition(new Transition(bs, t.label(), be), null);
        }
        return b;
    }
}

