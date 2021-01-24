/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.GroundDisjunction;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public class BranchingPoint
implements Serializable {
    private static final long serialVersionUID = 7306881534568051692L;
    protected final int m_level;
    protected final Node m_lastTableauNode;
    protected final Node m_lastMergedOrPrunedNode;
    protected final GroundDisjunction m_firstGroundDisjunction;
    protected final GroundDisjunction m_firstUnprocessedGroundDisjunction;

    public BranchingPoint(Tableau tableau) {
        this.m_level = tableau.m_currentBranchingPoint + 1;
        this.m_lastTableauNode = tableau.m_lastTableauNode;
        this.m_lastMergedOrPrunedNode = tableau.m_lastMergedOrPrunedNode;
        this.m_firstGroundDisjunction = tableau.m_firstGroundDisjunction;
        this.m_firstUnprocessedGroundDisjunction = tableau.m_firstUnprocessedGroundDisjunction;
    }

    public int getLevel() {
        return this.m_level;
    }

    public void startNextChoice(Tableau tableau, DependencySet clashDependencySet) {
    }
    
    public boolean canStartNextChoice() {
    	return true;
    }
}

