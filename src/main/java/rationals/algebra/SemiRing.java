/*
 * Decompiled with CFR 0.137.
 */
package rationals.algebra;

public interface SemiRing {
    public SemiRing plus(SemiRing var1);

    public SemiRing mult(SemiRing var1);

    public SemiRing one();

    public SemiRing zero();
}

