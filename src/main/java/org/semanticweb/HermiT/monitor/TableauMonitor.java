package org.semanticweb.HermiT.monitor;

import org.semanticweb.HermiT.model.AnnotatedEquality;
import org.semanticweb.HermiT.model.DataRange;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.tableau.BranchingPoint;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator;
import org.semanticweb.HermiT.tableau.DatatypeManager;
import org.semanticweb.HermiT.tableau.GroundDisjunction;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.ReasoningTaskDescription;
import org.semanticweb.HermiT.tableau.Tableau;

public interface TableauMonitor {
    public void setTableau(Tableau var1);

    public void isSatisfiableStarted(ReasoningTaskDescription var1);

    public void isSatisfiableFinished(ReasoningTaskDescription var1, boolean var2);

    public void tableauCleared();

    public void saturateStarted();

    public void saturateFinished(boolean var1);

    public void iterationStarted();

    public void iterationFinished();

    public void dlClauseMatchedStarted(DLClauseEvaluator var1, int var2);

    public void dlClauseMatchedFinished(DLClauseEvaluator var1, int var2);

    public void addFactStarted(Object[] var1, boolean var2);

    public void addFactFinished(Object[] var1, boolean var2, boolean var3);

    public void mergeStarted(Node var1, Node var2);

    public void nodePruned(Node var1);

    public void mergeFactStarted(Node var1, Node var2, Object[] var3, Object[] var4);

    public void mergeFactFinished(Node var1, Node var2, Object[] var3, Object[] var4);

    public void mergeFinished(Node var1, Node var2);

    public /* varargs */ void clashDetectionStarted(Object[] ... var1);

    public /* varargs */ void clashDetectionFinished(Object[] ... var1);

    public void clashDetected();

    public void backtrackToStarted(BranchingPoint var1);

    public void tupleRemoved(Object[] var1);

    public void backtrackToFinished(BranchingPoint var1);

    public void groundDisjunctionDerived(GroundDisjunction var1);

    public void processGroundDisjunctionStarted(GroundDisjunction var1);

    public void groundDisjunctionSatisfied(GroundDisjunction var1);

    public void processGroundDisjunctionFinished(GroundDisjunction var1);

    public void disjunctProcessingStarted(GroundDisjunction var1, int var2);

    public void disjunctProcessingFinished(GroundDisjunction var1, int var2);

    public void pushBranchingPointStarted(BranchingPoint var1);

    public void pushBranchingPointFinished(BranchingPoint var1);

    public void startNextBranchingPointStarted(BranchingPoint var1);

    public void startNextBranchingPointFinished(BranchingPoint var1);

    public void existentialExpansionStarted(ExistentialConcept var1, Node var2);

    public void existentialExpansionFinished(ExistentialConcept var1, Node var2);

    public void existentialSatisfied(ExistentialConcept var1, Node var2);

    public void nominalIntorductionStarted(Node var1, Node var2, AnnotatedEquality var3, Node var4, Node var5);

    public void nominalIntorductionFinished(Node var1, Node var2, AnnotatedEquality var3, Node var4, Node var5);

    public void descriptionGraphCheckingStarted(int var1, int var2, int var3, int var4, int var5, int var6);

    public void descriptionGraphCheckingFinished(int var1, int var2, int var3, int var4, int var5, int var6);

    public void nodeCreated(Node var1);

    public void nodeDestroyed(Node var1);

    public void unknownDatatypeRestrictionDetectionStarted(DataRange var1, Node var2, DataRange var3, Node var4);

    public void unknownDatatypeRestrictionDetectionFinished(DataRange var1, Node var2, DataRange var3, Node var4);

    public void datatypeCheckingStarted();

    public void datatypeCheckingFinished(boolean var1);

    public void datatypeConjunctionCheckingStarted(DatatypeManager.DConjunction var1);

    public void datatypeConjunctionCheckingFinished(DatatypeManager.DConjunction var1, boolean var2);

    public void blockingValidationStarted();

    public void blockingValidationFinished(int var1);

    public void possibleInstanceIsInstance();

    public void possibleInstanceIsNotInstance();
}

