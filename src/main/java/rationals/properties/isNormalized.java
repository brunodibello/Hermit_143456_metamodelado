package rationals.properties;

import rationals.Automaton;
import rationals.State;

public class isNormalized
implements UnaryTest {
    @Override
    public boolean test(Automaton a) {
        if (a.initials().size() != 1) {
            return false;
        }
        if (a.terminals().size() != 1) {
            return false;
        }
        State e = a.initials().iterator().next();
        if (a.deltaMinusOne(e).size() > 0) {
            return false;
        }
        e = a.terminals().iterator().next();
        return a.delta(e).size() <= 0;
    }
}

