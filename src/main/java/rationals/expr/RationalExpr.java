/*
 * Decompiled with CFR 0.137.
 */
package rationals.expr;

import rationals.algebra.SemiRing;
import rationals.expr.Plus;
import rationals.expr.Product;

public abstract class RationalExpr
implements SemiRing {
    static final RationalExpr zero = new RationalExpr(){

        @Override
        public SemiRing mult(SemiRing s1) {
            return zero;
        }

        @Override
        public SemiRing plus(SemiRing s1) {
            return s1;
        }

        public boolean equals(Object o) {
            return this == o;
        }

        public int hashCode() {
            return -1;
        }

        public String toString() {
            return "0";
        }
    };
    static final RationalExpr epsilon;
    static final RationalExpr one;

    @Override
    public final SemiRing one() {
        return one;
    }

    @Override
    public final SemiRing zero() {
        return zero;
    }

    @Override
    public SemiRing mult(SemiRing s2) {
        if (s2 == zero) {
            return zero;
        }
        if (s2 == epsilon) {
            return this;
        }
        RationalExpr re = (RationalExpr)s2;
        return new Product(this, re);
    }

    @Override
    public SemiRing plus(SemiRing s2) {
        if (s2 == zero) {
            return this;
        }
        return new Plus(this, (RationalExpr)s2);
    }

    static {
        one = epsilon = new RationalExpr(){

            public boolean equals(Object o) {
                return o == epsilon;
            }

            @Override
            public SemiRing mult(SemiRing s2) {
                return s2;
            }

            public String toString() {
                return "1";
            }

            public int hashCode() {
                return 0;
            }
        };
    }

}

