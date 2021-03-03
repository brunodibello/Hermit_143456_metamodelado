package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public abstract class Concept
implements Serializable {
    private static final long serialVersionUID = -8685976675539160944L;

    public abstract boolean isAlwaysTrue();

    public abstract boolean isAlwaysFalse();

    public abstract String toString(Prefixes var1);

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }
}

