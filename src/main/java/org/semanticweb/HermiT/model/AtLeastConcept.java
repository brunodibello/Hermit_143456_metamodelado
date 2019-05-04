/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.AtLeast;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.model.Role;

public class AtLeastConcept
extends AtLeast {
    private static final long serialVersionUID = 4326267535193393030L;
    protected final LiteralConcept m_toConcept;
    protected static final InterningManager<AtLeastConcept> s_interningManager = new InterningManager<AtLeastConcept>(){

        @Override
        protected boolean equal(AtLeastConcept object1, AtLeastConcept object2) {
            return object1.m_number == object2.m_number && object1.m_onRole == object2.m_onRole && object1.m_toConcept == object2.m_toConcept;
        }

        @Override
        protected int getHashCode(AtLeastConcept object) {
            return (object.m_number * 7 + object.m_onRole.hashCode()) * 7 + object.m_toConcept.hashCode();
        }
    };

    protected AtLeastConcept(int number, Role onRole, LiteralConcept toConcept) {
        super(number, onRole);
        this.m_toConcept = toConcept;
    }

    public LiteralConcept getToConcept() {
        return this.m_toConcept;
    }

    @Override
    public boolean isAlwaysFalse() {
        return this.m_toConcept.isAlwaysFalse();
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "atLeast(" + this.m_number + ' ' + this.m_onRole.toString(prefixes) + ' ' + this.m_toConcept.toString(prefixes) + ')';
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static AtLeastConcept create(int number, Role onRole, LiteralConcept toConcept) {
        return s_interningManager.intern(new AtLeastConcept(number, onRole, toConcept));
    }

}

