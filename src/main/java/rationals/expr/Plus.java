/*
 * Decompiled with CFR 0.137.
 */
package rationals.expr;

import rationals.expr.BinaryExpr;
import rationals.expr.RationalExpr;

public class Plus
extends BinaryExpr {
    public Plus(RationalExpr e, RationalExpr f) {
        super(e, f);
    }

    public String toString() {
        return this.getLeft() + "+" + this.getRight();
    }
}

