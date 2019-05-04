/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.DescriptionGraph;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.model.InterningManager;

public class ExistsDescriptionGraph
extends ExistentialConcept
implements DLPredicate {
    private static final long serialVersionUID = 7433430510725260994L;
    protected final DescriptionGraph m_descriptionGraph;
    protected final int m_vertex;
    protected static final InterningManager<ExistsDescriptionGraph> s_interningManager = new InterningManager<ExistsDescriptionGraph>(){

        @Override
        protected boolean equal(ExistsDescriptionGraph object1, ExistsDescriptionGraph object2) {
            return object1.m_descriptionGraph.equals(object2.m_descriptionGraph) && object1.m_vertex == object2.m_vertex;
        }

        @Override
        protected int getHashCode(ExistsDescriptionGraph object) {
            return object.m_descriptionGraph.hashCode() + 7 * object.m_vertex;
        }
    };

    protected ExistsDescriptionGraph(DescriptionGraph descriptionGraph, int vertex) {
        this.m_descriptionGraph = descriptionGraph;
        this.m_vertex = vertex;
    }

    public DescriptionGraph getDescriptionGraph() {
        return this.m_descriptionGraph;
    }

    public int getVertex() {
        return this.m_vertex;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public boolean isAlwaysTrue() {
        return false;
    }

    @Override
    public boolean isAlwaysFalse() {
        return false;
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "exists(" + prefixes.abbreviateIRI(this.m_descriptionGraph.getName()) + '|' + this.m_vertex + ')';
    }

    @Override
    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static ExistsDescriptionGraph create(DescriptionGraph descriptionGraph, int vertex) {
        return s_interningManager.intern(new ExistsDescriptionGraph(descriptionGraph, vertex));
    }

}

