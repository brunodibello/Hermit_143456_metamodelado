package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;

public class AtLeastDataRange
extends AtLeast {
    private static final long serialVersionUID = 4326267535193393030L;
    protected final LiteralDataRange m_toDataRange;
    protected static final InterningManager<AtLeastDataRange> s_interningManager = new InterningManager<AtLeastDataRange>(){

        @Override
        protected boolean equal(AtLeastDataRange object1, AtLeastDataRange object2) {
            return object1.m_number == object2.m_number && object1.m_onRole == object2.m_onRole && object1.m_toDataRange == object2.m_toDataRange;
        }

        @Override
        protected int getHashCode(AtLeastDataRange object) {
            return (object.m_number * 7 + object.m_onRole.hashCode()) * 7 + object.m_toDataRange.hashCode();
        }
    };

    protected AtLeastDataRange(int number, Role onRole, LiteralDataRange toConcept) {
        super(number, onRole);
        this.m_toDataRange = toConcept;
    }

    public LiteralDataRange getToDataRange() {
        return this.m_toDataRange;
    }

    @Override
    public boolean isAlwaysFalse() {
        return this.m_toDataRange.isAlwaysFalse();
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "atLeast(" + this.m_number + ' ' + this.m_onRole.toString(prefixes) + ' ' + this.m_toDataRange.toString(prefixes) + ')';
    }

    public static AtLeastDataRange create(int number, Role onRole, LiteralDataRange toDataRange) {
        return s_interningManager.intern(new AtLeastDataRange(number, onRole, toDataRange));
    }

}

