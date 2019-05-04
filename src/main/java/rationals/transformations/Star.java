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

public class Star
implements UnaryTransformation {
    @Override
    public Automaton transform(Automaton a) {
        if (a.delta().size() == 0) {
            return Automaton.epsilonAutomaton();
        }
        Automaton b = new Automaton();
        State ni = b.addState(true, true);
        State nt = b.addState(true, true);
        HashMap<State, State> map = new HashMap<State, State>();
        for (State i : a.states()) {
            map.put(i, b.addState(false, false));
        }
        for (Transition t : a.delta()) {
            b.addTransition(new Transition((State)map.get(t.start()), t.label(), (State)map.get(t.end())), null);
            if (t.start().isInitial() && t.end().isTerminal()) {
                b.addTransition(new Transition(ni, t.label(), nt), null);
                b.addTransition(new Transition(nt, t.label(), ni), null);
                continue;
            }
            if (t.start().isInitial()) {
                b.addTransition(new Transition(ni, t.label(), (State)map.get(t.end())), null);
                b.addTransition(new Transition(nt, t.label(), (State)map.get(t.end())), null);
                continue;
            }
            if (!t.end().isTerminal()) continue;
            b.addTransition(new Transition((State)map.get(t.start()), t.label(), nt), null);
            b.addTransition(new Transition((State)map.get(t.start()), t.label(), ni), null);
        }
        return b;
    }
}

