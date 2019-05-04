/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.AtomicDataRange;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.LiteralDataRange;

public class AtomicNegationDataRange
extends LiteralDataRange {
    protected final AtomicDataRange m_negatedDataRange;
    protected static final InterningManager<AtomicNegationDataRange> s_interningManager = new InterningManager<AtomicNegationDataRange>(){

        @Override
        protected boolean equal(AtomicNegationDataRange object1, AtomicNegationDataRange object2) {
            return object1.m_negatedDataRange == object2.m_negatedDataRange;
        }

        @Override
        protected int getHashCode(AtomicNegationDataRange object) {
            return - object.m_negatedDataRange.hashCode();
        }
    };

    protected AtomicNegationDataRange(AtomicDataRange negatedDataRange) {
        this.m_negatedDataRange = negatedDataRange;
    }

    public AtomicDataRange getNegatedDataRange() {
        return this.m_negatedDataRange;
    }

    @Override
    public LiteralDataRange getNegation() {
        return this.m_negatedDataRange;
    }

    @Override
    public boolean isAlwaysTrue() {
        return this.m_negatedDataRange.isAlwaysFalse();
    }

    @Override
    public boolean isAlwaysFalse() {
        return this.m_negatedDataRange.isAlwaysTrue();
    }

    @Override
    public boolean isNegatedInternalDatatype() {
        return this.m_negatedDataRange.isInternalDatatype();
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "not(" + this.m_negatedDataRange.toString(prefixes) + ")";
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static AtomicNegationDataRange create(AtomicDataRange negatedDataRange) {
        return s_interningManager.intern(new AtomicNegationDataRange(negatedDataRange));
    }

}

