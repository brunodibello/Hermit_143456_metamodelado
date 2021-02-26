package org.semanticweb.HermiT.existentials;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.blocking.BlockingStrategy;
import org.semanticweb.HermiT.model.AtLeast;
import org.semanticweb.HermiT.model.AtLeastConcept;
import org.semanticweb.HermiT.model.AtLeastDataRange;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.tableau.BranchingPoint;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.PermanentDependencySet;
import org.semanticweb.HermiT.tableau.Tableau;
import org.semanticweb.HermiT.tableau.TupleTable;

public class IndividualReuseStrategy
extends AbstractExpansionStrategy
implements Serializable {
    private static final long serialVersionUID = -7373787507623860081L;
    protected final boolean m_isDeterministic;
    protected final Map<AtomicConcept, NodeBranchingPointPair> m_reusedNodes;
    protected final Set<AtomicConcept> m_doReuseConceptsAlways;
    protected final Set<AtomicConcept> m_dontReuseConceptsThisRun;
    protected final Set<AtomicConcept> m_dontReuseConceptsEver;
    protected final TupleTable m_reuseBacktrackingTable;
    protected final Object[] m_auxiliaryBuffer;
    protected int[] m_indicesByBranchingPoint;

    public IndividualReuseStrategy(BlockingStrategy strategy, boolean isDeterministic) {
        super(strategy, true);
        this.m_isDeterministic = isDeterministic;
        this.m_reusedNodes = new HashMap<AtomicConcept, NodeBranchingPointPair>();
        this.m_doReuseConceptsAlways = new HashSet<AtomicConcept>();
        this.m_dontReuseConceptsThisRun = new HashSet<AtomicConcept>();
        this.m_dontReuseConceptsEver = new HashSet<AtomicConcept>();
        this.m_reuseBacktrackingTable = new TupleTable(1);
        this.m_auxiliaryBuffer = new Object[1];
        this.m_indicesByBranchingPoint = new int[10];
    }

    @Override
    public void initialize(Tableau tableau) {
        super.initialize(tableau);
        this.m_doReuseConceptsAlways.clear();
        this.m_dontReuseConceptsEver.clear();
        Object object = tableau.getParameters().get("IndividualReuseStrategy.reuseAlways");
        if (object instanceof Set) {
            this.m_doReuseConceptsAlways.addAll((Set)object);
        }
        if ((object = tableau.getParameters().get("IndividualReuseStrategy.reuseNever")) instanceof Set) {
            this.m_dontReuseConceptsEver.addAll((Set)object);
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.m_reusedNodes.clear();
        this.m_reuseBacktrackingTable.clear();
        this.m_dontReuseConceptsThisRun.clear();
        this.m_dontReuseConceptsThisRun.addAll(this.m_dontReuseConceptsEver);
    }

    @Override
    public void branchingPointPushed() {
        int start = this.m_tableau.getCurrentBranchingPoint().getLevel();
        int requiredSize = start + 1;
        if (requiredSize > this.m_indicesByBranchingPoint.length) {
            int newSize = this.m_indicesByBranchingPoint.length * 3 / 2;
            while (requiredSize > newSize) {
                newSize = newSize * 3 / 2;
            }
            int[] newIndicesByBranchingPoint = new int[newSize];
            System.arraycopy(this.m_indicesByBranchingPoint, 0, newIndicesByBranchingPoint, 0, this.m_indicesByBranchingPoint.length);
            this.m_indicesByBranchingPoint = newIndicesByBranchingPoint;
        }
        this.m_indicesByBranchingPoint[start] = this.m_reuseBacktrackingTable.getFirstFreeTupleIndex();
    }

    @Override
    public void backtrack() {
        int requiredFirstFreeTupleIndex = this.m_indicesByBranchingPoint[this.m_tableau.getCurrentBranchingPoint().getLevel()];
        for (int index = this.m_reuseBacktrackingTable.getFirstFreeTupleIndex() - 1; index >= requiredFirstFreeTupleIndex; --index) {
            AtomicConcept reuseConcept = (AtomicConcept)this.m_reuseBacktrackingTable.getTupleObject(index, 0);
            NodeBranchingPointPair result = this.m_reusedNodes.remove(reuseConcept);
            assert (result != null);
        }
        this.m_reuseBacktrackingTable.truncate(requiredFirstFreeTupleIndex);
    }

    @Override
    public void modelFound() {
        this.m_dontReuseConceptsEver.addAll(this.m_dontReuseConceptsThisRun);
    }

    @Override
    public boolean isDeterministic() {
        return this.m_isDeterministic;
    }

    public AtomicConcept getConceptForNode(Node node) {
        for (Map.Entry<AtomicConcept, NodeBranchingPointPair> entry : this.m_reusedNodes.entrySet()) {
            if (entry.getValue().m_node != node) continue;
            return entry.getKey();
        }
        return null;
    }

    public Set<AtomicConcept> getDontReuseConceptsEver() {
        return this.m_dontReuseConceptsEver;
    }

    @Override
    protected void expandExistential(AtLeast atLeast, Node forNode) {
        this.m_existentialExpansionManager.markExistentialProcessed(atLeast, forNode);
        if (!this.m_existentialExpansionManager.tryFunctionalExpansion(atLeast, forNode)) {
            if (atLeast instanceof AtLeastDataRange) {
                this.m_existentialExpansionManager.doNormalExpansion((AtLeastDataRange)atLeast, forNode);
            } else {
                AtLeastConcept atLeastConcept = (AtLeastConcept)atLeast;
                if (!this.tryParentReuse(atLeastConcept, forNode) && !this.expandWithModelReuse(atLeastConcept, forNode)) {
                    this.m_existentialExpansionManager.doNormalExpansion(atLeastConcept, forNode);
                }
            }
        }
    }

    protected boolean tryParentReuse(AtLeastConcept atLeastConcept, Node node) {
        Node parent;
        if (atLeastConcept.getNumber() == 1 && (parent = node.getParent()) != null && this.m_extensionManager.containsConceptAssertion(atLeastConcept.getToConcept(), parent)) {
            DependencySet dependencySet = this.m_extensionManager.getConceptAssertionDependencySet(atLeastConcept, node);
            if (!this.m_isDeterministic) {
                IndividualReuseBranchingPoint branchingPoint = new IndividualReuseBranchingPoint(this.m_tableau, atLeastConcept, node, true);
                this.m_tableau.pushBranchingPoint(branchingPoint);
                dependencySet = this.m_tableau.getDependencySetFactory().addBranchingPoint(dependencySet, branchingPoint.getLevel());
            }
            this.m_extensionManager.addRoleAssertion(atLeastConcept.getOnRole(), node, parent, dependencySet, true);
            return true;
        }
        return false;
    }

    protected boolean expandWithModelReuse(AtLeastConcept atLeastConcept, Node node) {
        if (!(atLeastConcept.getToConcept() instanceof AtomicConcept)) {
            return false;
        }
        AtomicConcept toConcept = (AtomicConcept)atLeastConcept.getToConcept();
        if (Prefixes.isInternalIRI(toConcept.getIRI())) {
            return false;
        }
        if (atLeastConcept.getNumber() == 1 && (this.m_doReuseConceptsAlways.contains(toConcept) || !this.m_dontReuseConceptsThisRun.contains(toConcept))) {
            Node existentialNode;
            if (this.m_tableau.getTableauMonitor() != null) {
                this.m_tableau.getTableauMonitor().existentialExpansionStarted(atLeastConcept, node);
            }
            DependencySet dependencySet = this.m_extensionManager.getConceptAssertionDependencySet(atLeastConcept, node);
            NodeBranchingPointPair reuseInfo = this.m_reusedNodes.get(toConcept);
            if (reuseInfo == null) {
                if (!this.m_isDeterministic) {
                    IndividualReuseBranchingPoint branchingPoint = new IndividualReuseBranchingPoint(this.m_tableau, atLeastConcept, node, false);
                    this.m_tableau.pushBranchingPoint(branchingPoint);
                    dependencySet = this.m_tableau.getDependencySetFactory().addBranchingPoint(dependencySet, branchingPoint.getLevel());
                }
                existentialNode = this.m_tableau.createNewNINode(dependencySet);
                reuseInfo = new NodeBranchingPointPair(existentialNode, this.m_tableau.getCurrentBranchingPointLevel());
                this.m_reusedNodes.put(toConcept, reuseInfo);
                this.m_extensionManager.addConceptAssertion(toConcept, existentialNode, dependencySet, true);
                this.m_auxiliaryBuffer[0] = toConcept;
                this.m_reuseBacktrackingTable.addTuple(this.m_auxiliaryBuffer);
            } else {
                dependencySet = reuseInfo.m_node.addCanonicalNodeDependencySet(dependencySet);
                existentialNode = reuseInfo.m_node.getCanonicalNode();
                if (!this.m_isDeterministic) {
                    dependencySet = this.m_tableau.getDependencySetFactory().addBranchingPoint(dependencySet, reuseInfo.m_branchingPoint);
                }
            }
            this.m_extensionManager.addRoleAssertion(atLeastConcept.getOnRole(), node, existentialNode, dependencySet, true);
            if (this.m_tableau.getTableauMonitor() != null) {
                this.m_tableau.getTableauMonitor().existentialExpansionFinished(atLeastConcept, node);
            }
            return true;
        }
        return false;
    }

    protected static class NodeBranchingPointPair
    implements Serializable {
        private static final long serialVersionUID = 427963701900451471L;
        protected final Node m_node;
        protected final int m_branchingPoint;

        public NodeBranchingPointPair(Node node, int branchingPoint) {
            this.m_node = node;
            this.m_branchingPoint = branchingPoint;
        }
    }

    protected class IndividualReuseBranchingPoint
    extends BranchingPoint {
        private static final long serialVersionUID = -5715836252258022216L;
        protected final AtLeastConcept m_existential;
        protected final Node m_node;
        protected final boolean m_wasParentReuse;

        public IndividualReuseBranchingPoint(Tableau tableau, AtLeastConcept existential, Node node, boolean wasParentReuse) {
            super(tableau);
            this.m_existential = existential;
            this.m_node = node;
            this.m_wasParentReuse = wasParentReuse;
        }

        @Override
        public void startNextChoice(Tableau tableau, DependencySet clashDependencySet) {
            if (!this.m_wasParentReuse) {
                IndividualReuseStrategy.this.m_dontReuseConceptsThisRun.add((AtomicConcept)this.m_existential.getToConcept());
            }
            PermanentDependencySet dependencySet = tableau.getDependencySetFactory().removeBranchingPoint(clashDependencySet, this.m_level);
            if (tableau.getTableauMonitor() != null) {
                tableau.getTableauMonitor().existentialExpansionStarted(this.m_existential, this.m_node);
            }
            Node existentialNode = tableau.createNewTreeNode(dependencySet, this.m_node);
            IndividualReuseStrategy.this.m_extensionManager.addConceptAssertion(this.m_existential.getToConcept(), existentialNode, dependencySet, true);
            IndividualReuseStrategy.this.m_extensionManager.addRoleAssertion(this.m_existential.getOnRole(), this.m_node, existentialNode, dependencySet, true);
            if (tableau.getTableauMonitor() != null) {
                tableau.getTableauMonitor().existentialExpansionFinished(this.m_existential, this.m_node);
            }
        }
    }

}

