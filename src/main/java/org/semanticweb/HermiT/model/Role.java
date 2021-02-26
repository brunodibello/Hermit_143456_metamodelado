package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public abstract class Role
implements Serializable {
    private static final long serialVersionUID = -6487260817445541931L;

    public abstract Role getInverse();

    public abstract Atom getRoleAssertion(Term var1, Term var2);

    public abstract String toString(Prefixes var1);

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }
}

