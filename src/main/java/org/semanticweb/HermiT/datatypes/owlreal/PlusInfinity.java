/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.owlreal;

final class PlusInfinity
extends Number {
    private static final long serialVersionUID = -205551124673073593L;
    public static final PlusInfinity INSTANCE = new PlusInfinity();

    private PlusInfinity() {
    }

    public String toString() {
        return "+INF";
    }

    @Override
    public double doubleValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float floatValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int intValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long longValue() {
        throw new UnsupportedOperationException();
    }

    protected Object readResolve() {
        return INSTANCE;
    }
}

