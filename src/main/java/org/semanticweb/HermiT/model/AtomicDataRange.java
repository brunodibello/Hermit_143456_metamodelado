/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.model.LiteralDataRange;

public abstract class AtomicDataRange
extends LiteralDataRange {
    private static final long serialVersionUID = 8843660377807760406L;

    @Override
    public abstract LiteralDataRange getNegation();
}

