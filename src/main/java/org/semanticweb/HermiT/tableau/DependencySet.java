/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

public interface DependencySet {
    public boolean containsBranchingPoint(int var1);

    public boolean isEmpty();

    public int getMaximumBranchingPoint();
}

