/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.InverseRole;
import org.semanticweb.HermiT.model.Role;
import org.semanticweb.HermiT.model.Term;

public class AtomicRole
extends Role
implements DLPredicate {
    private static final long serialVersionUID = 3766087788313643809L;
    protected final String m_iri;
    protected static final InterningManager<AtomicRole> s_interningManager = new InterningManager<AtomicRole>(){

        @Override
        protected boolean equal(AtomicRole object1, AtomicRole object2) {
            return object1.m_iri.equals(object2.m_iri);
        }

        @Override
        protected int getHashCode(AtomicRole object) {
            return object.m_iri.hashCode();
        }
    };
    public static final AtomicRole TOP_OBJECT_ROLE = AtomicRole.create("http://www.w3.org/2002/07/owl#topObjectProperty");
    public static final AtomicRole BOTTOM_OBJECT_ROLE = AtomicRole.create("http://www.w3.org/2002/07/owl#bottomObjectProperty");
    public static final AtomicRole TOP_DATA_ROLE = AtomicRole.create("http://www.w3.org/2002/07/owl#topDataProperty");
    public static final AtomicRole BOTTOM_DATA_ROLE = AtomicRole.create("http://www.w3.org/2002/07/owl#bottomDataProperty");

    protected AtomicRole(String iri) {
        this.m_iri = iri;
    }

    public String getIRI() {
        return this.m_iri;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public Role getInverse() {
        if (this == TOP_OBJECT_ROLE || this == BOTTOM_OBJECT_ROLE) {
            return this;
        }
        return InverseRole.create(this);
    }

    @Override
    public Atom getRoleAssertion(Term term0, Term term1) {
        return Atom.create(this, term0, term1);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return prefixes.abbreviateIRI(this.m_iri);
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static AtomicRole create(String iri) {
        return s_interningManager.intern(new AtomicRole(iri));
    }

}

