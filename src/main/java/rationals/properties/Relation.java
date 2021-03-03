package rationals.properties;

import java.util.Set;
import rationals.Automaton;
import rationals.State;

public interface Relation {
    public void setAutomata(Automaton var1, Automaton var2);

    public boolean equivalence(State var1, State var2);

    public boolean equivalence(Set<State> var1, Set<State> var2);
}

