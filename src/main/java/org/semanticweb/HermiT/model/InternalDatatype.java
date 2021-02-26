package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;

public class InternalDatatype
extends AtomicDataRange
implements DLPredicate {
    private static final long serialVersionUID = -1078274072706143620L;
    protected final String m_iri;
    protected static final InterningManager<InternalDatatype> s_interningManager = new InterningManager<InternalDatatype>(){

        @Override
        protected boolean equal(InternalDatatype object1, InternalDatatype object2) {
            return object1.m_iri.equals(object2.m_iri);
        }

        @Override
        protected int getHashCode(InternalDatatype object) {
            return object.m_iri.hashCode();
        }
    };
    public static final InternalDatatype RDFS_LITERAL = InternalDatatype.create("http://www.w3.org/2000/01/rdf-schema#Literal");

    protected InternalDatatype(String iri) {
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
    public LiteralDataRange getNegation() {
        return AtomicNegationDataRange.create(this);
    }

    @Override
    public boolean isAlwaysTrue() {
        return this == RDFS_LITERAL;
    }

    @Override
    public boolean isAlwaysFalse() {
        return false;
    }

    @Override
    public boolean isInternalDatatype() {
        return true;
    }

    @Override
    public String toString(Prefixes prefixes) {
        return prefixes.abbreviateIRI(this.m_iri);
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static InternalDatatype create(String uri) {
        return s_interningManager.intern(new InternalDatatype(uri));
    }

}

