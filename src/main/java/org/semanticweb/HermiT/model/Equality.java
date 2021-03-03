package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public class Equality
implements DLPredicate,
Serializable {
    private static final long serialVersionUID = 8308051741088513244L;
    public static final Equality INSTANCE = new Equality();

    protected Equality() {
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "==";
    }

    public String toOrderedString(Prefixes prefixes) {
        return this.toString(prefixes);
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    protected Object readResolve() {
        return INSTANCE;
    }

    public static Equality create() {
        return INSTANCE;
    }
}

