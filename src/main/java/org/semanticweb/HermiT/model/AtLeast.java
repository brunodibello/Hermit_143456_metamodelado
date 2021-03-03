package org.semanticweb.HermiT.model;

public abstract class AtLeast
extends ExistentialConcept
implements DLPredicate {
    private static final long serialVersionUID = -5450065396132818872L;
    protected final int m_number;
    protected final Role m_onRole;

    protected AtLeast(int number, Role onRole) {
        this.m_number = number;
        this.m_onRole = onRole;
    }

    public int getNumber() {
        return this.m_number;
    }

    public Role getOnRole() {
        return this.m_onRole;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public boolean isAlwaysTrue() {
        return false;
    }
}

