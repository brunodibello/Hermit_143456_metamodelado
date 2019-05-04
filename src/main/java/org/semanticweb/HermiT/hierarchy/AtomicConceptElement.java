/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.hierarchy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.HermiT.model.Individual;

public class AtomicConceptElement {
    protected final Set<Individual> m_knownInstances;
    protected final Set<Individual> m_possibleInstances;

    public AtomicConceptElement(Set<Individual> known, Set<Individual> possible) {
        this.m_knownInstances = known == null ? new HashSet<Individual>() : known;
        this.m_possibleInstances = possible == null ? new HashSet<Individual>() : possible;
    }

    public boolean isKnown(Individual individual) {
        return this.m_knownInstances.contains(individual);
    }

    public boolean isPossible(Individual individual) {
        return this.m_possibleInstances.contains(individual);
    }

    public Set<Individual> getKnownInstances() {
        return this.m_knownInstances;
    }

    public Set<Individual> getPossibleInstances() {
        return this.m_possibleInstances;
    }

    public boolean hasPossibles() {
        return !this.m_possibleInstances.isEmpty();
    }

    public void setToKnown(Individual individual) {
        this.m_possibleInstances.remove(individual);
        this.m_knownInstances.add(individual);
    }

    public boolean addPossible(Individual individual) {
        return this.m_possibleInstances.add(individual);
    }

    public boolean addPossibles(Set<Individual> individuals) {
        return this.m_possibleInstances.addAll(individuals);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(" (known instances: ");
        boolean notfirst = false;
        for (Individual individual : this.m_knownInstances) {
            if (notfirst) {
                buffer.append(", ");
            }
            notfirst = true;
            buffer.append(individual);
        }
        buffer.append(" | possible instances: ");
        notfirst = false;
        for (Individual individual : this.m_possibleInstances) {
            if (notfirst) {
                buffer.append(", ");
            }
            notfirst = true;
            buffer.append(individual);
        }
        buffer.append(") ");
        return buffer.toString();
    }
}

