package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;

public class NegatedAtomicRole {
    protected final AtomicRole m_negatedAtomicRole;
    protected static final InterningManager<NegatedAtomicRole> s_interningManager = new InterningManager<NegatedAtomicRole>(){

        @Override
        protected boolean equal(NegatedAtomicRole object1, NegatedAtomicRole object2) {
            return object1.m_negatedAtomicRole == object2.m_negatedAtomicRole;
        }

        @Override
        protected int getHashCode(NegatedAtomicRole object) {
            return - object.m_negatedAtomicRole.hashCode();
        }
    };

    public NegatedAtomicRole(AtomicRole negatedAtomicRole) {
        this.m_negatedAtomicRole = negatedAtomicRole;
    }

    public AtomicRole getNegatedAtomicRole() {
        return this.m_negatedAtomicRole;
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    public String toString(Prefixes prefixes) {
        return "not(" + this.m_negatedAtomicRole.toString(prefixes) + ")";
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static NegatedAtomicRole create(AtomicRole negatedAtomicRole) {
        return s_interningManager.intern(new NegatedAtomicRole(negatedAtomicRole));
    }

}

