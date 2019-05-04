/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.existentials;

import java.util.List;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.DLOntology;
import org.semanticweb.HermiT.model.DataRange;
import org.semanticweb.HermiT.model.Variable;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public interface ExistentialExpansionStrategy {
    public void initialize(Tableau var1);

    public void additionalDLOntologySet(DLOntology var1);

    public void additionalDLOntologyCleared();

    public void clear();

    public boolean expandExistentials(boolean var1);

    public void assertionAdded(Concept var1, Node var2, boolean var3);

    public void assertionAdded(DataRange var1, Node var2, boolean var3);

    public void assertionCoreSet(Concept var1, Node var2);

    public void assertionCoreSet(DataRange var1, Node var2);

    public void assertionRemoved(Concept var1, Node var2, boolean var3);

    public void assertionRemoved(DataRange var1, Node var2, boolean var3);

    public void assertionAdded(AtomicRole var1, Node var2, Node var3, boolean var4);

    public void assertionCoreSet(AtomicRole var1, Node var2, Node var3);

    public void assertionRemoved(AtomicRole var1, Node var2, Node var3, boolean var4);

    public void nodesMerged(Node var1, Node var2);

    public void nodesUnmerged(Node var1, Node var2);

    public void nodeStatusChanged(Node var1);

    public void nodeInitialized(Node var1);

    public void nodeDestroyed(Node var1);

    public void branchingPointPushed();

    public void backtrack();

    public void modelFound();

    public boolean isDeterministic();

    public boolean isExact();

    public void dlClauseBodyCompiled(List<DLClauseEvaluator.Worker> var1, DLClause var2, List<Variable> var3, Object[] var4, boolean[] var5);
}

