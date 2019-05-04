/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.AtomicNegationConcept;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.LiteralConcept;

public class AtomicConcept
extends LiteralConcept
implements DLPredicate {
    private static final long serialVersionUID = -1078274072706143620L;
    protected final String m_iri;
    protected static final InterningManager<AtomicConcept> s_interningManager = new InterningManager<AtomicConcept>(){

        @Override
        protected boolean equal(AtomicConcept object1, AtomicConcept object2) {
            return object1.m_iri.equals(object2.m_iri);
        }

        @Override
        protected int getHashCode(AtomicConcept object) {
            return object.m_iri.hashCode();
        }
    };
    public static final AtomicConcept THING = AtomicConcept.create("http://www.w3.org/2002/07/owl#Thing");
    public static final AtomicConcept NOTHING = AtomicConcept.create("http://www.w3.org/2002/07/owl#Nothing");
    public static final AtomicConcept INTERNAL_NAMED = AtomicConcept.create("internal:nam#Named");

    protected AtomicConcept(String iri) {
        this.m_iri = iri;
    }

    public String getIRI() {
        return this.m_iri;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public LiteralConcept getNegation() {
        if (this == THING) {
            return NOTHING;
        }
        if (this == NOTHING) {
            return THING;
        }
        return AtomicNegationConcept.create(this);
    }

    @Override
    public boolean isAlwaysTrue() {
        return this == THING;
    }

    @Override
    public boolean isAlwaysFalse() {
        return this == NOTHING;
    }

    @Override
    public String toString(Prefixes prefixes) {
        return prefixes.abbreviateIRI(this.m_iri);
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static AtomicConcept create(String uri) {
        return s_interningManager.intern(new AtomicConcept(uri));
    }

}

