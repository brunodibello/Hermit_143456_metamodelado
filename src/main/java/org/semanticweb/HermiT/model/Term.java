/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public abstract class Term
implements Serializable {
    private static final long serialVersionUID = -8524194708579485033L;

    public abstract String toString(Prefixes var1);
}

