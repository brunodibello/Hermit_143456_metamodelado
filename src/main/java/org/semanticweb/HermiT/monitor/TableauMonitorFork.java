package org.semanticweb.HermiT.monitor;

import java.io.Serializable;
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

public class TableauMonitorFork
implements TableauMonitor,
Serializable {
    private static final long serialVersionUID = 8321902665477431455L;
    protected final TableauMonitor m_first;
    protected final TableauMonitor m_second;

    public TableauMonitorFork(TableauMonitor first, TableauMonitor second) {
        this.m_first = first;
        this.m_second = second;
    }

    @Override
    public void setTableau(Tableau tableau) {
        this.m_first.setTableau(tableau);
        this.m_second.setTableau(tableau);
    }

    @Override
    public void isSatisfiableStarted(ReasoningTaskDescription reasoningTaskDescription) {
        this.m_first.isSatisfiableStarted(reasoningTaskDescription);
        this.m_second.isSatisfiableStarted(reasoningTaskDescription);
    }

    @Override
    public void isSatisfiableFinished(ReasoningTaskDescription reasoningTaskDescription, boolean result) {
        this.m_first.isSatisfiableFinished(reasoningTaskDescription, result);
        this.m_second.isSatisfiableFinished(reasoningTaskDescription, result);
    }

    @Override
    public void tableauCleared() {
        this.m_first.tableauCleared();
        this.m_second.tableauCleared();
    }

    @Override
    public void saturateStarted() {
        this.m_first.saturateStarted();
        this.m_second.saturateStarted();
    }

    @Override
    public void saturateFinished(boolean modelFound) {
        this.m_first.saturateFinished(modelFound);
        this.m_second.saturateFinished(modelFound);
    }

    @Override
    public void iterationStarted() {
        this.m_first.iterationStarted();
        this.m_second.iterationStarted();
    }

    @Override
    public void iterationFinished() {
        this.m_first.iterationFinished();
        this.m_second.iterationFinished();
    }

    @Override
    public void dlClauseMatchedStarted(DLClauseEvaluator dlClauseEvaluator, int dlClauseIndex) {
        this.m_first.dlClauseMatchedStarted(dlClauseEvaluator, dlClauseIndex);
        this.m_second.dlClauseMatchedStarted(dlClauseEvaluator, dlClauseIndex);
    }

    @Override
    public void dlClauseMatchedFinished(DLClauseEvaluator dlClauseEvaluator, int dlClauseIndex) {
        this.m_first.dlClauseMatchedFinished(dlClauseEvaluator, dlClauseIndex);
        this.m_second.dlClauseMatchedFinished(dlClauseEvaluator, dlClauseIndex);
    }

    @Override
    public void addFactStarted(Object[] tuple, boolean isCore) {
        this.m_first.addFactStarted(tuple, isCore);
        this.m_second.addFactStarted(tuple, isCore);
    }

    @Override
    public void addFactFinished(Object[] tuple, boolean isCore, boolean factAdded) {
        this.m_first.addFactFinished(tuple, isCore, factAdded);
        this.m_second.addFactFinished(tuple, isCore, factAdded);
    }

    @Override
    public void mergeStarted(Node mergeFrom, Node mergeInto) {
        this.m_first.mergeStarted(mergeFrom, mergeInto);
        this.m_second.mergeStarted(mergeFrom, mergeInto);
    }

    @Override
    public void nodePruned(Node node) {
        this.m_first.nodePruned(node);
        this.m_second.nodePruned(node);
    }

    @Override
    public void mergeFactStarted(Node mergeFrom, Node mergeInto, Object[] sourceTuple, Object[] targetTuple) {
        this.m_first.mergeFactStarted(mergeFrom, mergeInto, sourceTuple, targetTuple);
        this.m_second.mergeFactStarted(mergeFrom, mergeInto, sourceTuple, targetTuple);
    }

    @Override
    public void mergeFactFinished(Node mergeFrom, Node mergeInto, Object[] sourceTuple, Object[] targetTuple) {
        this.m_first.mergeFactFinished(mergeFrom, mergeInto, sourceTuple, targetTuple);
        this.m_second.mergeFactFinished(mergeFrom, mergeInto, sourceTuple, targetTuple);
    }

    @Override
    public void mergeFinished(Node mergeFrom, Node mergeInto) {
        this.m_first.mergeFinished(mergeFrom, mergeInto);
        this.m_second.mergeFinished(mergeFrom, mergeInto);
    }

    @Override
    public /* varargs */ void clashDetectionStarted(Object[] ... tuples) {
        this.m_first.clashDetectionStarted(tuples);
        this.m_second.clashDetectionStarted(tuples);
    }

    @Override
    public /* varargs */ void clashDetectionFinished(Object[] ... tuples) {
        this.m_first.clashDetectionFinished(tuples);
        this.m_second.clashDetectionFinished(tuples);
    }

    @Override
    public void clashDetected() {
        this.m_first.clashDetected();
        this.m_second.clashDetected();
    }

    @Override
    public void backtrackToStarted(BranchingPoint newCurrentBrancingPoint) {
        this.m_first.backtrackToStarted(newCurrentBrancingPoint);
        this.m_second.backtrackToStarted(newCurrentBrancingPoint);
    }

    @Override
    public void tupleRemoved(Object[] tuple) {
        this.m_first.tupleRemoved(tuple);
        this.m_second.tupleRemoved(tuple);
    }

    @Override
    public void backtrackToFinished(BranchingPoint newCurrentBrancingPoint) {
        this.m_first.backtrackToFinished(newCurrentBrancingPoint);
        this.m_second.backtrackToFinished(newCurrentBrancingPoint);
    }

    @Override
    public void groundDisjunctionDerived(GroundDisjunction groundDisjunction) {
        this.m_first.groundDisjunctionDerived(groundDisjunction);
        this.m_second.groundDisjunctionDerived(groundDisjunction);
    }

    @Override
    public void processGroundDisjunctionStarted(GroundDisjunction groundDisjunction) {
        this.m_first.processGroundDisjunctionStarted(groundDisjunction);
        this.m_second.processGroundDisjunctionStarted(groundDisjunction);
    }

    @Override
    public void groundDisjunctionSatisfied(GroundDisjunction groundDisjunction) {
        this.m_first.groundDisjunctionSatisfied(groundDisjunction);
        this.m_second.groundDisjunctionSatisfied(groundDisjunction);
    }

    @Override
    public void processGroundDisjunctionFinished(GroundDisjunction groundDisjunction) {
        this.m_first.processGroundDisjunctionFinished(groundDisjunction);
        this.m_second.processGroundDisjunctionFinished(groundDisjunction);
    }

    @Override
    public void disjunctProcessingStarted(GroundDisjunction groundDisjunction, int disjunct) {
        this.m_first.disjunctProcessingStarted(groundDisjunction, disjunct);
        this.m_second.disjunctProcessingStarted(groundDisjunction, disjunct);
    }

    @Override
    public void disjunctProcessingFinished(GroundDisjunction groundDisjunction, int disjunct) {
        this.m_first.disjunctProcessingFinished(groundDisjunction, disjunct);
        this.m_second.disjunctProcessingFinished(groundDisjunction, disjunct);
    }

    @Override
    public void pushBranchingPointStarted(BranchingPoint branchingPoint) {
        this.m_first.pushBranchingPointStarted(branchingPoint);
        this.m_second.pushBranchingPointStarted(branchingPoint);
    }

    @Override
    public void pushBranchingPointFinished(BranchingPoint branchingPoint) {
        this.m_first.pushBranchingPointFinished(branchingPoint);
        this.m_second.pushBranchingPointFinished(branchingPoint);
    }

    @Override
    public void startNextBranchingPointStarted(BranchingPoint branchingPoint) {
        this.m_first.startNextBranchingPointStarted(branchingPoint);
        this.m_second.startNextBranchingPointStarted(branchingPoint);
    }

    @Override
    public void startNextBranchingPointFinished(BranchingPoint branchingPoint) {
        this.m_first.startNextBranchingPointFinished(branchingPoint);
        this.m_second.startNextBranchingPointFinished(branchingPoint);
    }

    @Override
    public void existentialExpansionStarted(ExistentialConcept existentialConcept, Node forNode) {
        this.m_first.existentialExpansionStarted(existentialConcept, forNode);
        this.m_second.existentialExpansionStarted(existentialConcept, forNode);
    }

    @Override
    public void existentialExpansionFinished(ExistentialConcept existentialConcept, Node forNode) {
        this.m_first.existentialExpansionFinished(existentialConcept, forNode);
        this.m_second.existentialExpansionFinished(existentialConcept, forNode);
    }

    @Override
    public void existentialSatisfied(ExistentialConcept existentialConcept, Node forNode) {
        this.m_first.existentialSatisfied(existentialConcept, forNode);
        this.m_second.existentialSatisfied(existentialConcept, forNode);
    }

    @Override
    public void nominalIntorductionStarted(Node rootNode, Node treeNode, AnnotatedEquality annotatedEquality, Node argument1, Node argument2) {
        this.m_first.nominalIntorductionStarted(rootNode, treeNode, annotatedEquality, argument1, argument2);
        this.m_second.nominalIntorductionStarted(rootNode, treeNode, annotatedEquality, argument1, argument2);
    }

    @Override
    public void nominalIntorductionFinished(Node rootNode, Node treeNode, AnnotatedEquality annotatedEquality, Node argument1, Node argument2) {
        this.m_first.nominalIntorductionFinished(rootNode, treeNode, annotatedEquality, argument1, argument2);
        this.m_second.nominalIntorductionFinished(rootNode, treeNode, annotatedEquality, argument1, argument2);
    }

    @Override
    public void descriptionGraphCheckingStarted(int graphIndex1, int tupleIndex1, int position1, int graphIndex2, int tupleIndex2, int position2) {
        this.m_first.descriptionGraphCheckingStarted(graphIndex1, tupleIndex1, position1, graphIndex2, tupleIndex2, position2);
        this.m_second.descriptionGraphCheckingStarted(graphIndex1, tupleIndex1, position1, graphIndex2, tupleIndex2, position2);
    }

    @Override
    public void descriptionGraphCheckingFinished(int graphIndex1, int tupleIndex1, int position1, int graphIndex2, int tupleIndex2, int position2) {
        this.m_first.descriptionGraphCheckingFinished(graphIndex1, tupleIndex1, position1, graphIndex2, tupleIndex2, position2);
        this.m_second.descriptionGraphCheckingFinished(graphIndex1, tupleIndex1, position1, graphIndex2, tupleIndex2, position2);
    }

    @Override
    public void nodeCreated(Node node) {
        this.m_first.nodeCreated(node);
        this.m_second.nodeCreated(node);
    }

    @Override
    public void nodeDestroyed(Node node) {
        this.m_first.nodeDestroyed(node);
        this.m_second.nodeDestroyed(node);
    }

    @Override
    public void unknownDatatypeRestrictionDetectionStarted(DataRange dataRange1, Node node1, DataRange dataRange2, Node node2) {
        this.m_first.unknownDatatypeRestrictionDetectionStarted(dataRange1, node1, dataRange2, node2);
        this.m_second.unknownDatatypeRestrictionDetectionStarted(dataRange1, node1, dataRange2, node2);
    }

    @Override
    public void unknownDatatypeRestrictionDetectionFinished(DataRange dataRange1, Node node1, DataRange dataRange2, Node node2) {
        this.m_first.unknownDatatypeRestrictionDetectionFinished(dataRange1, node1, dataRange2, node2);
        this.m_second.unknownDatatypeRestrictionDetectionFinished(dataRange1, node1, dataRange2, node2);
    }

    @Override
    public void datatypeCheckingStarted() {
        this.m_first.datatypeCheckingStarted();
        this.m_second.datatypeCheckingStarted();
    }

    @Override
    public void datatypeCheckingFinished(boolean result) {
        this.m_first.datatypeCheckingFinished(result);
        this.m_second.datatypeCheckingFinished(result);
    }

    @Override
    public void datatypeConjunctionCheckingStarted(DatatypeManager.DConjunction conjunction) {
        this.m_first.datatypeConjunctionCheckingStarted(conjunction);
        this.m_second.datatypeConjunctionCheckingStarted(conjunction);
    }

    @Override
    public void datatypeConjunctionCheckingFinished(DatatypeManager.DConjunction conjunction, boolean result) {
        this.m_first.datatypeConjunctionCheckingFinished(conjunction, result);
        this.m_second.datatypeConjunctionCheckingFinished(conjunction, result);
    }

    @Override
    public void blockingValidationStarted() {
        this.m_first.blockingValidationStarted();
        this.m_second.blockingValidationStarted();
    }

    @Override
    public void blockingValidationFinished(int noInvalidlyBlocked) {
        this.m_first.blockingValidationFinished(noInvalidlyBlocked);
        this.m_second.blockingValidationFinished(noInvalidlyBlocked);
    }

    @Override
    public void possibleInstanceIsInstance() {
        this.m_first.possibleInstanceIsInstance();
        this.m_second.possibleInstanceIsInstance();
    }

    @Override
    public void possibleInstanceIsNotInstance() {
        this.m_first.possibleInstanceIsNotInstance();
        this.m_second.possibleInstanceIsNotInstance();
    }
}

