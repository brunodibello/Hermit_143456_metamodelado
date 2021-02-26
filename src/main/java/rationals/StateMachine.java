package rationals;

import java.util.List;
import java.util.Set;

public interface StateMachine {
    public Set<Object> alphabet();

    public StateFactory getStateFactory();

    public Set<Transition> delta(State var1, Object var2);

    public Set<Transition> delta(State var1);

    public Set<Transition> delta(Set<State> var1);

    public Set<State> steps(Set<State> var1, List<?> var2);

    public Set<State> step(Set<State> var1, Object var2);

    public Set<State> initials();

    public Set<Transition> deltaMinusOne(State var1);
}

