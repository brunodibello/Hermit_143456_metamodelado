/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.DataRange;

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

