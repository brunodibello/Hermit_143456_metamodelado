/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import org.semanticweb.HermiT.model.AnnotatedEquality;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.model.Inequality;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.monitor.TableauMonitor;
import org.semanticweb.HermiT.tableau.BranchingPoint;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.DependencySetFactory;
import org.semanticweb.HermiT.tableau.ExtensionManager;
import org.semanticweb.HermiT.tableau.GroundDisjunction;
import org.semanticweb.HermiT.tableau.GroundDisjunctionHeader;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.PermanentDependencySet;
import org.semanticweb.HermiT.tableau.Tableau;

public final class DisjunctionBranchingPoint
extends BranchingPoint {
    private static final long serialVersionUID = -8855083430836162354L;
    protected final GroundDisjunction m_groundDisjunction;
    protected final int[] m_sortedDisjunctIndexes;
    protected int m_currentIndex;

    public DisjunctionBranchingPoint(Tableau tableau, GroundDisjunction groundDisjunction, int[] sortedDisjunctIndexes) {
        super(tableau);
        this.m_groundDisjunction = groundDisjunction;
        this.m_sortedDisjunctIndexes = sortedDisjunctIndexes;
    }

    @Override
    public void startNextChoice(Tableau tableau, DependencySet clashDependencySet) {
        if (tableau.m_useDisjunctionLearning) {
            this.m_groundDisjunction.getGroundDisjunctionHeader().increaseNumberOfBacktrackings(this.m_sortedDisjunctIndexes[this.m_currentIndex]);
        }
        ++this.m_currentIndex;
        int currentDisjunctIndex = this.m_sortedDisjunctIndexes[this.m_currentIndex];
        if (tableau.m_tableauMonitor != null) {
            tableau.m_tableauMonitor.disjunctProcessingStarted(this.m_groundDisjunction, currentDisjunctIndex);
        }
        PermanentDependencySet dependencySet = tableau.getDependencySetFactory().getPermanent(clashDependencySet);
        if (this.m_currentIndex + 1 == this.m_groundDisjunction.getNumberOfDisjuncts()) {
            dependencySet = tableau.getDependencySetFactory().removeBranchingPoint(dependencySet, this.m_level);
        }
        for (int previousIndex = 0; previousIndex < this.m_currentIndex; ++previousIndex) {
            int previousDisjunctIndex = this.m_sortedDisjunctIndexes[previousIndex];
            DLPredicate dlPredicate = this.m_groundDisjunction.getDLPredicate(previousDisjunctIndex);
            if (Equality.INSTANCE.equals(dlPredicate) || dlPredicate instanceof AnnotatedEquality) {
                tableau.m_extensionManager.addAssertion(Inequality.INSTANCE, this.m_groundDisjunction.getArgument(previousDisjunctIndex, 0), this.m_groundDisjunction.getArgument(previousDisjunctIndex, 1), dependencySet, false);
                continue;
            }
            if (!(dlPredicate instanceof AtomicConcept)) continue;
            tableau.m_extensionManager.addConceptAssertion(((AtomicConcept)dlPredicate).getNegation(), this.m_groundDisjunction.getArgument(previousDisjunctIndex, 0), dependencySet, false);
        }
        this.m_groundDisjunction.addDisjunctToTableau(tableau, currentDisjunctIndex, dependencySet);
        if (tableau.m_tableauMonitor != null) {
            tableau.m_tableauMonitor.disjunctProcessingFinished(this.m_groundDisjunction, currentDisjunctIndex);
        }
    }
}

