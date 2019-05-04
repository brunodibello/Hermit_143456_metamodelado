/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.model.Role;

public class AnnotatedEquality
implements DLPredicate,
Serializable {
    private static final long serialVersionUID = 7197886700065386931L;
    protected final int m_cardinality;
    protected final Role m_onRole;
    protected final LiteralConcept m_toConcept;
    protected static final InterningManager<AnnotatedEquality> s_interningManager = new InterningManager<AnnotatedEquality>(){

        @Override
        protected boolean equal(AnnotatedEquality object1, AnnotatedEquality object2) {
            return object1.m_cardinality == object2.m_cardinality && object1.m_onRole == object2.m_onRole && object1.m_toConcept == object2.m_toConcept;
        }

        @Override
        protected int getHashCode(AnnotatedEquality object) {
            return object.m_cardinality + object.m_onRole.hashCode() + object.m_toConcept.hashCode();
        }
    };

    protected AnnotatedEquality(int cardinality, Role onRole, LiteralConcept toConcept) {
        this.m_cardinality = cardinality;
        this.m_onRole = onRole;
        this.m_toConcept = toConcept;
    }

    public int getCaridnality() {
        return this.m_cardinality;
    }

    public Role getOnRole() {
        return this.m_onRole;
    }

    public LiteralConcept getToConcept() {
        return this.m_toConcept;
    }

    @Override
    public int getArity() {
        return 3;
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "==@atMost(" + this.m_cardinality + " " + this.m_onRole.toString(prefixes) + " " + this.m_toConcept.toString(prefixes) + ")";
    }

    public static AnnotatedEquality create(int cardinality, Role onRole, LiteralConcept toConcept) {
        return s_interningManager.intern(new AnnotatedEquality(cardinality, onRole, toConcept));
    }

}

