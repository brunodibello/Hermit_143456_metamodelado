/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.blocking;

import org.semanticweb.HermiT.tableau.Node;

public abstract class BlockingSignature {
    protected BlockingSignature m_nextEntry = null;

    public final BlockingSignature getNextEntry() {
        return this.m_nextEntry;
    }

    public void setNextEntry(BlockingSignature nextEntry) {
        this.m_nextEntry = nextEntry;
    }

    public abstract boolean blocksNode(Node var1);
}

