package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public class NodeIDLessEqualThan
implements DLPredicate,
Serializable {
    private static final long serialVersionUID = 5572346926189452451L;
    public static final NodeIDLessEqualThan INSTANCE = new NodeIDLessEqualThan();

    protected NodeIDLessEqualThan() {
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "<=";
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    protected Object readResolve() {
        return INSTANCE;
    }
}

