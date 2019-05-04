/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.hierarchy;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Individual;

class RoleElementManager {
    protected final Map<AtomicRole, RoleElement> m_roleToElement = new HashMap<AtomicRole, RoleElement>();

    protected RoleElementManager() {
    }

    public RoleElement getRoleElement(AtomicRole role) {
        if (this.m_roleToElement.containsKey(role)) {
            return this.m_roleToElement.get(role);
        }
        RoleElement element = new RoleElement(role);
        this.m_roleToElement.put(role, element);
        return element;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<AtomicRole, RoleElement> e : this.m_roleToElement.entrySet()) {
            buffer.append(e.getKey()).append(" -> ").append(e.getValue()).append('\n');
        }
        return buffer.toString();
    }

    public class RoleElement {
        protected final AtomicRole m_role;
        protected final Map<Individual, Set<Individual>> m_knownRelations;
        protected final Map<Individual, Set<Individual>> m_possibleRelations;

        protected RoleElement(AtomicRole role) {
            this.m_role = role;
            this.m_knownRelations = new HashMap<Individual, Set<Individual>>();
            this.m_possibleRelations = new HashMap<Individual, Set<Individual>>();
        }

        public AtomicRole getRole() {
            return this.m_role;
        }

        public boolean isKnown(Individual individual1, Individual individual2) {
            return this.m_knownRelations.containsKey(individual1) && this.m_knownRelations.get(individual1).contains(individual2);
        }

        public boolean isPossible(Individual individual1, Individual individual2) {
            return this.m_possibleRelations.containsKey(individual1) && this.m_possibleRelations.get(individual1).contains(individual2);
        }

        public Map<Individual, Set<Individual>> getKnownRelations() {
            return this.m_knownRelations;
        }

        public Map<Individual, Set<Individual>> getPossibleRelations() {
            return this.m_possibleRelations;
        }

        public boolean hasPossibles() {
            return !this.m_possibleRelations.isEmpty();
        }

        public void setToKnown(Individual individual1, Individual individual2) {
            Set<Individual> successors = this.m_possibleRelations.get(individual1);
            successors.remove(individual2);
            if (successors.isEmpty()) {
                this.m_possibleRelations.remove(individual1);
            }
            this.addKnown(individual1, individual2);
        }

        public boolean addKnown(Individual individual1, Individual individual2) {
            Set<Individual> successors = this.m_knownRelations.get(individual1);
            if (successors == null) {
                successors = new HashSet<Individual>();
                this.m_knownRelations.put(individual1, successors);
            }
            return successors.add(individual2);
        }

        public boolean addKnowns(Individual individual, Set<Individual> individuals) {
            Set<Individual> successors = this.m_knownRelations.get(individual);
            if (successors == null) {
                successors = new HashSet<Individual>();
                this.m_knownRelations.put(individual, successors);
            }
            return successors.addAll(individuals);
        }

        public boolean removeKnown(Individual individual1, Individual individual2) {
            Set<Individual> successors = this.m_knownRelations.get(individual1);
            boolean removed = false;
            if (successors != null) {
                removed = successors.remove(individual2);
                if (successors.isEmpty()) {
                    this.m_knownRelations.remove(individual1);
                }
            }
            return removed;
        }

        public boolean addPossible(Individual individual1, Individual individual2) {
            Set<Individual> successors = this.m_possibleRelations.get(individual1);
            if (successors == null) {
                successors = new HashSet<Individual>();
                this.m_possibleRelations.put(individual1, successors);
            }
            return successors.add(individual2);
        }

        public boolean removePossible(Individual individual1, Individual individual2) {
            Set<Individual> successors = this.m_possibleRelations.get(individual1);
            boolean removed = false;
            if (successors != null) {
                removed = successors.remove(individual2);
                if (successors.isEmpty()) {
                    this.m_possibleRelations.remove(individual1);
                }
            }
            return removed;
        }

        public boolean addPossibles(Individual individual, Set<Individual> individuals) {
            Set<Individual> successors = this.m_possibleRelations.get(individual);
            if (successors == null) {
                successors = new HashSet<Individual>();
                this.m_possibleRelations.put(individual, successors);
            }
            return successors.addAll(individuals);
        }

        public String toString() {
            StringBuilder buffer = new StringBuilder(this.m_role.toString()).append(" (known instances: ");
            boolean notfirst = false;
            for (Individual individual : this.m_knownRelations.keySet()) {
                for (Individual successor : this.m_knownRelations.get(individual)) {
                    if (notfirst) {
                        buffer.append(", ");
                        notfirst = true;
                    }
                    buffer.append("(").append(individual).append(", ").append(successor).append(")");
                }
            }
            buffer.append(" | possible instances: ");
            notfirst = false;
            for (Individual individual : this.m_possibleRelations.keySet()) {
                for (Individual successor : this.m_possibleRelations.get(individual)) {
                    if (notfirst) {
                        buffer.append(", ");
                        notfirst = true;
                    }
                    buffer.append("(").append(individual).append(", ").append(successor).append(")");
                }
            }
            buffer.append(") ");
            return buffer.toString();
        }
    }

}

