package org.semanticweb.HermiT.model;

public abstract class AtomicDataRange
extends LiteralDataRange {
    private static final long serialVersionUID = 8843660377807760406L;

    @Override
    public abstract LiteralDataRange getNegation();
}

