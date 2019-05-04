/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.monitor;

import java.io.Serializable;
import org.semanticweb.HermiT.model.AnnotatedEquality;
import org.semanticweb.HermiT.model.DataRange;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.monitor.TableauMonitor;
import org.semanticweb.HermiT.tableau.BranchingPoint;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator;
import org.semanticweb.HermiT.tableau.DatatypeManager;
import org.semanticweb.HermiT.tableau.GroundDisjunction;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.ReasoningTaskDescription;
import org.semanticweb.HermiT.tableau.Tableau;

public class TableauMonitorForwarder
implements TableauMonitor,
Serializable {
    private static final long serialVersionUID = -371801782567741632L;
    protected final TableauMonitor m_forwardingTargetMonitor;
    protected boolean m_forwardingOn;

    public TableauMonitorForwarder(TableauMonitor forwardingTargetMontior) {
        this.m_forwardingTargetMonitor = forwardingTargetMontior;
    }

    public boolean isForwardingOn() {
        return this.m_forwardingOn;
    }

    public void setForwardingOn(boolean forwardingOn) {
        this.m_forwardingOn = forwardingOn;
    }

    @Override
    public void setTableau(Tableau tableau) {
        this.m_forwardingTargetMonitor.setTableau(tableau);
    }

    @Override
    public void isSatisfiableStarted(ReasoningTaskDescription reasoningTaskDescription) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.isSatisfiableStarted(reasoningTaskDescription);
        }
    }

    @Override
    public void isSatisfiableFinished(ReasoningTaskDescription reasoningTaskDescription, boolean result) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.isSatisfiableFinished(reasoningTaskDescription, result);
        }
    }

    @Override
    public void tableauCleared() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.tableauCleared();
        }
    }

    @Override
    public void saturateStarted() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.saturateStarted();
        }
    }

    @Override
    public void saturateFinished(boolean modelFound) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.saturateFinished(modelFound);
        }
    }

    @Override
    public void iterationStarted() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.iterationStarted();
        }
    }

    @Override
    public void iterationFinished() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.iterationFinished();
        }
    }

    @Override
    public void dlClauseMatchedStarted(DLClauseEvaluator dlClauseEvaluator, int dlClauseIndex) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.dlClauseMatchedStarted(dlClauseEvaluator, dlClauseIndex);
        }
    }

    @Override
    public void dlClauseMatchedFinished(DLClauseEvaluator dlClauseEvaluator, int dlClauseIndex) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.dlClauseMatchedFinished(dlClauseEvaluator, dlClauseIndex);
        }
    }

    @Override
    public void addFactStarted(Object[] tuple, boolean isCore) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.addFactStarted(tuple, isCore);
        }
    }

    @Override
    public void addFactFinished(Object[] tuple, boolean isCore, boolean factAdded) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.addFactFinished(tuple, isCore, factAdded);
        }
    }

    @Override
    public void mergeStarted(Node mergeFrom, Node mergrInto) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.mergeStarted(mergeFrom, mergrInto);
        }
    }

    @Override
    public void nodePruned(Node node) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.nodePruned(node);
        }
    }

    @Override
    public void mergeFactStarted(Node mergeFrom, Node mergeInto, Object[] sourceTuple, Object[] targetTuple) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.mergeFactStarted(mergeFrom, mergeInto, sourceTuple, targetTuple);
        }
    }

    @Override
    public void mergeFactFinished(Node mergeFrom, Node mergeInto, Object[] sourceTuple, Object[] targetTuple) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.mergeFactFinished(mergeFrom, mergeInto, sourceTuple, targetTuple);
        }
    }

    @Override
    public void mergeFinished(Node mergeFrom, Node mergeInto) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.mergeFinished(mergeFrom, mergeInto);
        }
    }

    @Override
    public /* varargs */ void clashDetectionStarted(Object[] ... tuples) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.clashDetectionStarted(tuples);
        }
    }

    @Override
    public /* varargs */ void clashDetectionFinished(Object[] ... tuples) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.clashDetectionFinished(tuples);
        }
    }

    @Override
    public void clashDetected() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.clashDetected();
        }
    }

    @Override
    public void backtrackToStarted(BranchingPoint newCurrentBrancingPoint) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.backtrackToStarted(newCurrentBrancingPoint);
        }
    }

    @Override
    public void tupleRemoved(Object[] tuple) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.tupleRemoved(tuple);
        }
    }

    @Override
    public void backtrackToFinished(BranchingPoint newCurrentBrancingPoint) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.backtrackToFinished(newCurrentBrancingPoint);
        }
    }

    @Override
    public void groundDisjunctionDerived(GroundDisjunction groundDisjunction) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.groundDisjunctionDerived(groundDisjunction);
        }
    }

    @Override
    public void processGroundDisjunctionStarted(GroundDisjunction groundDisjunction) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.processGroundDisjunctionStarted(groundDisjunction);
        }
    }

    @Override
    public void groundDisjunctionSatisfied(GroundDisjunction groundDisjunction) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.groundDisjunctionSatisfied(groundDisjunction);
        }
    }

    @Override
    public void processGroundDisjunctionFinished(GroundDisjunction groundDisjunction) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.processGroundDisjunctionFinished(groundDisjunction);
        }
    }

    @Override
    public void disjunctProcessingStarted(GroundDisjunction groundDisjunction, int disjunct) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.disjunctProcessingStarted(groundDisjunction, disjunct);
        }
    }

    @Override
    public void disjunctProcessingFinished(GroundDisjunction groundDisjunction, int disjunct) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.disjunctProcessingFinished(groundDisjunction, disjunct);
        }
    }

    @Override
    public void pushBranchingPointStarted(BranchingPoint branchingPoint) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.pushBranchingPointStarted(branchingPoint);
        }
    }

    @Override
    public void pushBranchingPointFinished(BranchingPoint branchingPoint) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.pushBranchingPointFinished(branchingPoint);
        }
    }

    @Override
    public void startNextBranchingPointStarted(BranchingPoint branchingPoint) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.startNextBranchingPointStarted(branchingPoint);
        }
    }

    @Override
    public void startNextBranchingPointFinished(BranchingPoint branchingPoint) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.startNextBranchingPointFinished(branchingPoint);
        }
    }

    @Override
    public void existentialExpansionStarted(ExistentialConcept existentialConcept, Node forNode) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.existentialExpansionStarted(existentialConcept, forNode);
        }
    }

    @Override
    public void existentialExpansionFinished(ExistentialConcept existentialConcept, Node forNode) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.existentialExpansionFinished(existentialConcept, forNode);
        }
    }

    @Override
    public void existentialSatisfied(ExistentialConcept existentialConcept, Node forNode) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.existentialSatisfied(existentialConcept, forNode);
        }
    }

    @Override
    public void nominalIntorductionStarted(Node rootNode, Node treeNode, AnnotatedEquality annotatedEquality, Node argument1, Node argument2) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.nominalIntorductionStarted(rootNode, treeNode, annotatedEquality, argument1, argument2);
        }
    }

    @Override
    public void nominalIntorductionFinished(Node rootNode, Node treeNode, AnnotatedEquality annotatedEquality, Node argument1, Node argument2) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.nominalIntorductionFinished(rootNode, treeNode, annotatedEquality, argument1, argument2);
        }
    }

    @Override
    public void descriptionGraphCheckingStarted(int graphIndex1, int tupleIndex1, int position1, int graphIndex2, int tupleIndex2, int position2) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.descriptionGraphCheckingStarted(graphIndex1, tupleIndex1, position1, graphIndex2, tupleIndex2, position2);
        }
    }

    @Override
    public void descriptionGraphCheckingFinished(int graphIndex1, int tupleIndex1, int position1, int graphIndex2, int tupleIndex2, int position2) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.descriptionGraphCheckingFinished(graphIndex1, tupleIndex1, position1, graphIndex2, tupleIndex2, position2);
        }
    }

    @Override
    public void nodeCreated(Node node) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.nodeCreated(node);
        }
    }

    @Override
    public void nodeDestroyed(Node node) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.nodeDestroyed(node);
        }
    }

    @Override
    public void unknownDatatypeRestrictionDetectionStarted(DataRange dataRange1, Node node1, DataRange dataRange2, Node node2) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.unknownDatatypeRestrictionDetectionStarted(dataRange1, node1, dataRange2, node2);
        }
    }

    @Override
    public void unknownDatatypeRestrictionDetectionFinished(DataRange dataRange1, Node node1, DataRange dataRange2, Node node2) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.unknownDatatypeRestrictionDetectionFinished(dataRange1, node1, dataRange2, node2);
        }
    }

    @Override
    public void datatypeCheckingStarted() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.datatypeCheckingStarted();
        }
    }

    @Override
    public void datatypeCheckingFinished(boolean result) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.datatypeCheckingFinished(result);
        }
    }

    @Override
    public void datatypeConjunctionCheckingStarted(DatatypeManager.DConjunction conjunction) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.datatypeConjunctionCheckingStarted(conjunction);
        }
    }

    @Override
    public void datatypeConjunctionCheckingFinished(DatatypeManager.DConjunction conjunction, boolean result) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.datatypeConjunctionCheckingFinished(conjunction, result);
        }
    }

    @Override
    public void blockingValidationStarted() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.blockingValidationStarted();
        }
    }

    @Override
    public void blockingValidationFinished(int noInvalidlyBlocked) {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.blockingValidationFinished(noInvalidlyBlocked);
        }
    }

    @Override
    public void possibleInstanceIsInstance() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.possibleInstanceIsInstance();
        }
    }

    @Override
    public void possibleInstanceIsNotInstance() {
        if (this.m_forwardingOn) {
            this.m_forwardingTargetMonitor.possibleInstanceIsNotInstance();
        }
    }
}

