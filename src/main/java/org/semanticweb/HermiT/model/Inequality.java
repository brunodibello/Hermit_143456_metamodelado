/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.DLPredicate;

public class Inequality
implements DLPredicate,
Serializable {
    private static final long serialVersionUID = 296924110684230279L;
    public static final Inequality INSTANCE = new Inequality();

    protected Inequality() {
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "!=";
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    protected Object readResolve() {
        return INSTANCE;
    }

    public static Inequality create() {
        return INSTANCE;
    }
}

