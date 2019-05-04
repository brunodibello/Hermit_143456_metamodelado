/*
 * Decompiled with CFR 0.137.
 */
package rationals.expr;

import rationals.expr.RationalExpr;

public class Letter
extends RationalExpr {
    private final Object label;

    public Letter(Object o) {
        this.label = o;
    }

    public boolean equals(Object obj) {
        Letter lt = (Letter)obj;
        if (lt == null) {
            return false;
        }
        return lt.label == null ? this.label == null : lt.label.equals(this.label);
    }

    public int hashCode() {
        return this.label.hashCode();
    }

    public String toString() {
        return this.label.toString();
    }
}

