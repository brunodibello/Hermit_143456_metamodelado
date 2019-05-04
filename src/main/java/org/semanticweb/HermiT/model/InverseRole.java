/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.Role;
import org.semanticweb.HermiT.model.Term;

public class InverseRole
extends Role {
    private static final long serialVersionUID = 3351701933728011998L;
    protected final AtomicRole m_inverseOf;
    protected static final InterningManager<InverseRole> s_interningManager = new InterningManager<InverseRole>(){

        @Override
        protected boolean equal(InverseRole object1, InverseRole object2) {
            return object1.m_inverseOf == object2.m_inverseOf;
        }

        @Override
        protected int getHashCode(InverseRole object) {
            return - object.m_inverseOf.hashCode();
        }
    };

    public InverseRole(AtomicRole inverseOf) {
        this.m_inverseOf = inverseOf;
    }

    public AtomicRole getInverseOf() {
        return this.m_inverseOf;
    }

    @Override
    public Role getInverse() {
        return this.m_inverseOf;
    }

    @Override
    public Atom getRoleAssertion(Term term0, Term term1) {
        return Atom.create(this.m_inverseOf, term1, term0);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "inv(" + this.m_inverseOf.toString(prefixes) + ")";
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static InverseRole create(AtomicRole inverseOf) {
        return s_interningManager.intern(new InverseRole(inverseOf));
    }

}

