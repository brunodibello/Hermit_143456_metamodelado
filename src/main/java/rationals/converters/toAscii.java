/*
 * Decompiled with CFR 0.137.
 */
package rationals.converters;

import java.util.Iterator;
import java.util.Set;
import rationals.Automaton;
import rationals.State;
import rationals.Transition;
import rationals.converters.ToString;

public class toAscii
implements ToString {
    @Override
    public String toString(Automaton a) {
        StringBuffer sb = new StringBuffer();
        sb.append("A = ").append(a.alphabet().toString()).append("\n");
        sb.append("Q = ").append(a.states().toString()).append("\n");
        sb.append("I = ").append(a.initials().toString()).append("\n");
        sb.append("T = ").append(a.terminals().toString()).append("\n");
        sb.append("delta = [\n");
        Iterator<Transition> i = a.delta().iterator();
        while (i.hasNext()) {
            sb.append(i.next()).append("\n");
        }
        sb.append("]\n");
        return sb.toString();
    }
}

