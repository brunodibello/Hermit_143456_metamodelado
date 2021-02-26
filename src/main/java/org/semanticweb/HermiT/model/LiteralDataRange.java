package org.semanticweb.HermiT.model;

public abstract class LiteralDataRange
extends DataRange
implements DLPredicate {
    private static final long serialVersionUID = -2302452747339289424L;

    public abstract LiteralDataRange getNegation();

    public boolean isInternalDatatype() {
        return false;
    }

    public boolean isNegatedInternalDatatype() {
        return false;
    }
}

