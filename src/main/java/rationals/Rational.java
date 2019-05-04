/*
 * Decompiled with CFR 0.137.
 */
package rationals;

import java.util.Set;
import rationals.State;
import rationals.Transition;

public interface Rational {
    public State addState(boolean var1, boolean var2);

    public Set<Object> alphabet();

    public Set<State> states();

    public Set<State> initials();

    public Set<State> terminals();

    public Set<State> accessibleStates();

    public Set<State> coAccessibleStates();

    public Set<State> accessibleAndCoAccessibleStates();

    public Set<Transition> delta();

    public Set<Transition> delta(State var1, Object var2);

    public Set<Transition> delta(State var1);

    public Set<Transition> deltaFrom(State var1, State var2);

    public Set<Transition> deltaMinusOne(State var1, Object var2);

    public boolean addTransition(Transition var1);

    public boolean validTransition(Transition var1);

    public boolean addTransition(Transition var1, String var2);

    public Set<Transition> deltaMinusOne(State var1);
}

