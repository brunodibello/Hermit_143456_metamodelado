/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import java.io.Serializable;
import java.util.Set;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.ExistsDescriptionGraph;
import org.semanticweb.HermiT.model.Variable;

public class DescriptionGraph
implements DLPredicate,
Serializable {
    private static final long serialVersionUID = -6098910060520673164L;
    protected final String m_name;
    protected final AtomicConcept[] m_atomicConceptsByVertices;
    protected final Edge[] m_edges;
    protected final Set<AtomicConcept> m_startConcepts;

    public DescriptionGraph(String name, AtomicConcept[] atomicConceptsByVertices, Edge[] edges, Set<AtomicConcept> startConcepts) {
        this.m_name = name;
        this.m_atomicConceptsByVertices = atomicConceptsByVertices;
        this.m_edges = edges;
        this.m_startConcepts = startConcepts;
    }

    public String getName() {
        return this.m_name;
    }

    @Override
    public int getArity() {
        return this.m_atomicConceptsByVertices.length;
    }

    public AtomicConcept getAtomicConceptForVertex(int vertex) {
        return this.m_atomicConceptsByVertices[vertex];
    }

    public int getNumberOfVertices() {
        return this.m_atomicConceptsByVertices.length;
    }

    public int getNumberOfEdges() {
        return this.m_edges.length;
    }

    public Edge getEdge(int edgeIndex) {
        return this.m_edges[edgeIndex];
    }

    public Set<AtomicConcept> getStartConcepts() {
        return this.m_startConcepts;
    }

    public void produceStartDLClauses(Set<DLClause> resultingDLClauses) {
        Variable X = Variable.create("X");
        for (AtomicConcept startAtomicConcept : this.m_startConcepts) {
            Atom[] antecedent = new Atom[]{Atom.create(startAtomicConcept, X)};
            int numberOfVerticesWithStartConcept = 0;
            for (AtomicConcept vertexConcept : this.m_atomicConceptsByVertices) {
                if (!vertexConcept.equals(startAtomicConcept)) continue;
                ++numberOfVerticesWithStartConcept;
            }
            int index = 0;
            Atom[] consequent = new Atom[numberOfVerticesWithStartConcept];
            for (int vertex = 0; vertex < this.m_atomicConceptsByVertices.length; ++vertex) {
                if (!this.m_atomicConceptsByVertices[vertex].equals(startAtomicConcept)) continue;
                consequent[index++] = Atom.create(ExistsDescriptionGraph.create(this, vertex), X);
            }
            resultingDLClauses.add(DLClause.create(consequent, antecedent));
        }
    }

    @Override
    public String toString(Prefixes ns) {
        return ns.abbreviateIRI(this.m_name);
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    public String getTextRepresentation() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('[');
        buffer.append('\n');
        for (int vertex = 0; vertex < this.m_atomicConceptsByVertices.length; ++vertex) {
            buffer.append("   ");
            buffer.append(vertex);
            buffer.append(" --> ");
            buffer.append(this.m_atomicConceptsByVertices[vertex].getIRI());
            buffer.append('\n');
        }
        buffer.append('\n');
        for (Edge edge : this.m_edges) {
            buffer.append("  ");
            buffer.append(edge.getFromVertex());
            buffer.append(" -- ");
            buffer.append(edge.getAtomicRole().getIRI());
            buffer.append(" --> ");
            buffer.append(edge.getToVertex());
            buffer.append('\n');
        }
        buffer.append('\n');
        for (AtomicConcept atomicConcept : this.m_startConcepts) {
            buffer.append("  ");
            buffer.append(atomicConcept.getIRI());
            buffer.append('\n');
        }
        buffer.append(']');
        return buffer.toString();
    }

    public static class Edge
    implements Serializable {
        private static final long serialVersionUID = -2407275128459101707L;
        protected final AtomicRole m_atomicRole;
        protected final int m_fromVertex;
        protected final int m_toVertex;

        public Edge(AtomicRole atomicRole, int fromVertex, int toVertex) {
            this.m_atomicRole = atomicRole;
            this.m_fromVertex = fromVertex;
            this.m_toVertex = toVertex;
        }

        public AtomicRole getAtomicRole() {
            return this.m_atomicRole;
        }

        public int getFromVertex() {
            return this.m_fromVertex;
        }

        public int getToVertex() {
            return this.m_toVertex;
        }

        public int hashCode() {
            return this.m_fromVertex + 7 * this.m_toVertex + 11 * this.m_atomicRole.hashCode();
        }

        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }
            if (!(that instanceof Edge)) {
                return false;
            }
            Edge thatEdge = (Edge)that;
            return this.m_atomicRole.equals(thatEdge.m_atomicRole) && this.m_fromVertex == thatEdge.m_fromVertex && this.m_toVertex == thatEdge.m_toVertex;
        }
    }

}

