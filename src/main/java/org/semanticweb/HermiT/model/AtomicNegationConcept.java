package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;

public class AtomicNegationConcept
extends LiteralConcept {
    private static final long serialVersionUID = -4635386233266966577L;
    protected final AtomicConcept m_negatedAtomicConcept;
    protected static final InterningManager<AtomicNegationConcept> s_interningManager = new InterningManager<AtomicNegationConcept>(){

        @Override
        protected boolean equal(AtomicNegationConcept object1, AtomicNegationConcept object2) {
            return object1.m_negatedAtomicConcept == object2.m_negatedAtomicConcept;
        }

        @Override
        protected int getHashCode(AtomicNegationConcept object) {
            return - object.m_negatedAtomicConcept.hashCode();
        }
    };

    protected AtomicNegationConcept(AtomicConcept negatedAtomicConcept) {
        this.m_negatedAtomicConcept = negatedAtomicConcept;
    }

    public AtomicConcept getNegatedAtomicConcept() {
        return this.m_negatedAtomicConcept;
    }

    @Override
    public LiteralConcept getNegation() {
        return this.m_negatedAtomicConcept;
    }

    @Override
    public boolean isAlwaysTrue() {
        return this.m_negatedAtomicConcept.isAlwaysFalse();
    }

    @Override
    public boolean isAlwaysFalse() {
        return this.m_negatedAtomicConcept.isAlwaysTrue();
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "not(" + this.m_negatedAtomicConcept.toString(prefixes) + ")";
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static AtomicNegationConcept create(AtomicConcept negatedAtomicConcept) {
        return s_interningManager.intern(new AtomicNegationConcept(negatedAtomicConcept));
    }

}

