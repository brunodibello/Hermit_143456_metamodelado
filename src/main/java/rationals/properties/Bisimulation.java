/*
 * Decompiled with CFR 0.137.
 */
package rationals.properties;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import rationals.Automaton;
import rationals.Couple;
import rationals.State;
import rationals.Transition;
import rationals.properties.Relation;

public class Bisimulation
implements Relation {
    private Automaton a1;
    private Automaton a2;
    private Set<Couple> exp;

    public Bisimulation(Automaton automaton, Automaton automaton2) {
        this.setAutomata(automaton, automaton2);
    }

    public Bisimulation() {
    }

    @Override
    public void setAutomata(Automaton a1, Automaton a2) {
        this.a1 = a1;
        this.a2 = a2;
        this.exp = new HashSet<Couple>();
    }

    @Override
    public boolean equivalence(State q0a, State q0b) {
        Couple cpl = new Couple(q0a, q0b);
        if (this.exp.contains(cpl)) {
            return true;
        }
        this.exp.add(cpl);
        Set<Transition> tas = this.a1.delta(q0a);
        Set<Transition> tbs = this.a2.delta(q0b);
        for (Transition tr : tas) {
            State ea = tr.end();
            Set<Transition> tbsl = this.a2.delta(q0b, tr.label());
            if (tbsl.isEmpty()) {
                return false;
            }
            Iterator<Transition> trb = tbsl.iterator();
            while (trb.hasNext()) {
                Transition tb = trb.next();
                tbs.remove(tb);
                State eb = tb.end();
                if (this.equivalence(ea, eb) || trb.hasNext()) continue;
                return false;
            }
        }
        if (!tbs.isEmpty()) {
            this.exp.remove(cpl);
            return false;
        }
        return true;
    }

    @Override
    public boolean equivalence(Set<State> nsa, Set<State> nsb) {
        for (State sa : nsa) {
            for (State sb : nsb) {
                if (this.equivalence(sa, sb)) continue;
                return false;
            }
        }
        return true;
    }
}

