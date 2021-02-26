package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;

public class Variable
extends Term {
    private static final long serialVersionUID = -1943457771102512887L;
    protected final String m_name;
    protected static final InterningManager<Variable> s_interningManager = new InterningManager<Variable>(){

        @Override
        protected boolean equal(Variable object1, Variable object2) {
            return object1.m_name.equals(object2.m_name);
        }

        @Override
        protected int getHashCode(Variable object) {
            return object.m_name.hashCode();
        }
    };

    protected Variable(String name) {
        this.m_name = name;
    }

    public String getName() {
        return this.m_name;
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return this.m_name;
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static Variable create(String name) {
        return s_interningManager.intern(new Variable(name));
    }

}

