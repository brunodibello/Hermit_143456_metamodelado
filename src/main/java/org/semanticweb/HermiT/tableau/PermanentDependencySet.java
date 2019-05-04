/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import org.semanticweb.HermiT.tableau.DependencySet;

public final class PermanentDependencySet
implements DependencySet,
Serializable {
    private static final long serialVersionUID = 353039301123337446L;
    protected PermanentDependencySet m_rest = null;
    protected int m_branchingPoint = -2;
    protected PermanentDependencySet m_nextEntry = null;
    protected int m_usageCounter = 0;
    protected PermanentDependencySet m_previousUnusedSet = null;
    protected PermanentDependencySet m_nextUnusedSet = null;

    protected PermanentDependencySet() {
    }

    @Override
    public boolean containsBranchingPoint(int branchingPoint) {
        PermanentDependencySet set = this;
        while (set != null) {
            if (set.m_branchingPoint == branchingPoint) {
                return true;
            }
            set = set.m_rest;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.m_branchingPoint == -1;
    }

    @Override
    public int getMaximumBranchingPoint() {
        return this.m_branchingPoint;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        PermanentDependencySet dependencySet = this;
        while (dependencySet.m_branchingPoint != -1) {
            buffer.append(dependencySet.m_branchingPoint);
            if (dependencySet.m_rest.m_branchingPoint != -1) {
                buffer.append(',');
            }
            dependencySet = dependencySet.m_rest;
        }
        buffer.append(" }");
        return buffer.toString();
    }
}

