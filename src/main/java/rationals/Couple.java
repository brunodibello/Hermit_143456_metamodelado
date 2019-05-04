/*
 * Decompiled with CFR 0.137.
 */
package rationals;

import rationals.State;

public class Couple {
    private final int hash;
    private final State from;
    private final State to;

    public Couple(State from, State to) {
        this.from = from;
        this.to = to;
        this.hash = from.hashCode() << 16 ^ to.hashCode();
    }

    public State getFrom() {
        return this.from;
    }

    public State getTo() {
        return this.to;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof Couple) {
            Couple c = (Couple)o;
            return this.from.equals(c.from) && this.to.equals(c.to);
        }
        return false;
    }

    public int hashCode() {
        return this.hash;
    }
}

