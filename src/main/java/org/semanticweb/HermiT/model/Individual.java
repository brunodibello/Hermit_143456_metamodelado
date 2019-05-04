/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.Term;

public class Individual
extends Term {
    private static final long serialVersionUID = 2791684055390160959L;
    protected final String m_uri;
    protected static final InterningManager<Individual> s_interningManager = new InterningManager<Individual>(){

        @Override
        protected boolean equal(Individual object1, Individual object2) {
            return object1.m_uri.equals(object2.m_uri);
        }

        @Override
        protected int getHashCode(Individual object) {
            return object.m_uri.hashCode();
        }
    };

    protected Individual(String uri) {
        this.m_uri = uri;
    }

    public String getIRI() {
        return this.m_uri;
    }

    public boolean isAnonymous() {
        return this.m_uri.startsWith("internal:anonymous#");
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return prefixes.abbreviateIRI(this.m_uri);
    }

    public static Individual create(String uri) {
        return s_interningManager.intern(new Individual(uri));
    }

    public static Individual createAnonymous(String id) {
        return Individual.create(Individual.getAnonymousURI(id));
    }

    public static String getAnonymousURI(String id) {
        return "internal:anonymous#" + id;
    }

}

