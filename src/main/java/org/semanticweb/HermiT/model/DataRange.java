package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public abstract class DataRange
implements Serializable {
    private static final long serialVersionUID = 352467050584766830L;

    public abstract boolean isAlwaysTrue();

    public abstract boolean isAlwaysFalse();

    public int getArity() {
        return 1;
    }

    public abstract String toString(Prefixes var1);

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }
}

