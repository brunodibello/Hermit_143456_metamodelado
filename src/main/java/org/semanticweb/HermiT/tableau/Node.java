/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.DependencySetFactory;
import org.semanticweb.HermiT.tableau.DescriptionGraphManager;
import org.semanticweb.HermiT.tableau.NodeType;
import org.semanticweb.HermiT.tableau.PermanentDependencySet;
import org.semanticweb.HermiT.tableau.Tableau;

public final class Node
implements Serializable {
    private static final long serialVersionUID = -2549229429321484690L;
    private static final List<ExistentialConcept> NO_EXISTENTIALS = Collections.emptyList();
    public static final Node SIGNATURE_CACHE_BLOCKER = new Node(null);
    protected final Tableau m_tableau;
    protected int m_nodeID;
    protected NodeState m_nodeState;
    protected Node m_parent;
    protected NodeType m_nodeType;
    protected int m_treeDepth;
    protected int m_numberOfPositiveAtomicConcepts;
    protected int m_numberOfNegatedAtomicConcepts;
    protected int m_numberOfNegatedRoleAssertions;
    protected List<ExistentialConcept> m_unprocessedExistentials;
    protected Node m_previousTableauNode;
    protected Node m_nextTableauNode;
    protected Node m_previousMergedOrPrunedNode;
    protected Node m_mergedInto;
    protected PermanentDependencySet m_mergedIntoDependencySet;
    protected Node m_blocker;
    protected boolean m_directlyBlocked;
    protected Object m_blockingObject;
    protected Object m_blockingCargo;
    protected int m_firstGraphOccurrenceNode;

    public Node(Tableau tableau) {
        this.m_tableau = tableau;
        this.m_nodeID = -1;
    }

    public Tableau getTableau() {
        return this.m_tableau;
    }

    protected void initialize(int nodeID, Node parent, NodeType nodeType, int treeDepth) {
        assert (this.m_nodeID == -1);
        assert (this.m_unprocessedExistentials == null);
        this.m_nodeID = nodeID;
        this.m_nodeState = NodeState.ACTIVE;
        this.m_parent = parent;
        this.m_nodeType = nodeType;
        this.m_treeDepth = treeDepth;
        this.m_numberOfPositiveAtomicConcepts = 0;
        this.m_numberOfNegatedAtomicConcepts = 0;
        this.m_numberOfNegatedRoleAssertions = 0;
        this.m_unprocessedExistentials = NO_EXISTENTIALS;
        this.m_previousTableauNode = null;
        this.m_nextTableauNode = null;
        this.m_previousMergedOrPrunedNode = null;
        this.m_mergedInto = null;
        this.m_mergedIntoDependencySet = null;
        this.m_blocker = null;
        this.m_directlyBlocked = false;
        DescriptionGraphManager.intializeNode(this);
    }

    protected void destroy() {
        this.m_nodeID = -1;
        this.m_nodeState = null;
        this.m_parent = null;
        this.m_nodeType = null;
        if (this.m_unprocessedExistentials != NO_EXISTENTIALS) {
            this.m_unprocessedExistentials.clear();
            this.m_tableau.putExistentialConceptsBuffer(this.m_unprocessedExistentials);
        }
        this.m_unprocessedExistentials = null;
        this.m_previousTableauNode = null;
        this.m_nextTableauNode = null;
        this.m_previousMergedOrPrunedNode = null;
        this.m_mergedInto = null;
        if (this.m_mergedIntoDependencySet != null) {
            this.m_tableau.m_dependencySetFactory.removeUsage(this.m_mergedIntoDependencySet);
            this.m_mergedIntoDependencySet = null;
        }
        this.m_blocker = null;
        this.m_tableau.m_descriptionGraphManager.destroyNode(this);
    }

    public int getNodeID() {
        return this.m_nodeID;
    }

    public Node getParent() {
        return this.m_parent;
    }

    public Node getClusterAnchor() {
        if (this.m_nodeType == NodeType.TREE_NODE) {
            return this;
        }
        return this.m_parent;
    }

    public boolean isRootNode() {
        return this.m_parent == null;
    }

    public boolean isParentOf(Node potentialChild) {
        return potentialChild.m_parent == this;
    }

    public boolean isAncestorOf(Node potendialDescendant) {
        while (potendialDescendant != null) {
            potendialDescendant = potendialDescendant.m_parent;
            if (potendialDescendant != this) continue;
            return true;
        }
        return false;
    }

    public NodeType getNodeType() {
        return this.m_nodeType;
    }

    public int getTreeDepth() {
        return this.m_treeDepth;
    }

    public boolean isBlocked() {
        return this.m_blocker != null;
    }

    public boolean isDirectlyBlocked() {
        return this.m_directlyBlocked;
    }

    public boolean isIndirectlyBlocked() {
        return this.m_blocker != null && !this.m_directlyBlocked;
    }

    public Node getBlocker() {
        return this.m_blocker;
    }

    public void setBlocked(Node blocker, boolean directlyBlocked) {
        this.m_blocker = blocker;
        this.m_directlyBlocked = directlyBlocked;
    }

    public Object getBlockingObject() {
        return this.m_blockingObject;
    }

    public void setBlockingObject(Object blockingObject) {
        this.m_blockingObject = blockingObject;
    }

    public Object getBlockingCargo() {
        return this.m_blockingCargo;
    }

    public void setBlockingCargo(Object blockingCargo) {
        this.m_blockingCargo = blockingCargo;
    }

    public int getNumberOfPositiveAtomicConcepts() {
        return this.m_numberOfPositiveAtomicConcepts;
    }

    public boolean isActive() {
        return this.m_nodeState == NodeState.ACTIVE;
    }

    public boolean isMerged() {
        return this.m_nodeState == NodeState.MERGED;
    }

    public Node getMergedInto() {
        return this.m_mergedInto;
    }

    public PermanentDependencySet getMergedIntoDependencySet() {
        return this.m_mergedIntoDependencySet;
    }

    public boolean isPruned() {
        return this.m_nodeState == NodeState.PRUNED;
    }

    public Node getPreviousTableauNode() {
        return this.m_previousTableauNode;
    }

    public Node getNextTableauNode() {
        return this.m_nextTableauNode;
    }

    public Node getCanonicalNode() {
        Node result = this;
        while (result.m_mergedInto != null) {
            result = result.m_mergedInto;
        }
        return result;
    }

    public PermanentDependencySet getCanonicalNodeDependencySet() {
        return this.addCanonicalNodeDependencySet(this.m_tableau.m_dependencySetFactory.m_emptySet);
    }

    public PermanentDependencySet addCanonicalNodeDependencySet(DependencySet dependencySet) {
        PermanentDependencySet result = this.m_tableau.m_dependencySetFactory.getPermanent(dependencySet);
        Node node = this;
        while (node.m_mergedInto != null) {
            result = this.m_tableau.m_dependencySetFactory.unionWith(result, node.m_mergedIntoDependencySet);
            node = node.m_mergedInto;
        }
        return result;
    }

    protected void addToUnprocessedExistentials(ExistentialConcept existentialConcept) {
        assert (NO_EXISTENTIALS.isEmpty());
        if (this.m_unprocessedExistentials == NO_EXISTENTIALS) {
            this.m_unprocessedExistentials = this.m_tableau.getExistentialConceptsBuffer();
            assert (this.m_unprocessedExistentials.isEmpty());
        }
        this.m_unprocessedExistentials.add(existentialConcept);
    }

    protected void removeFromUnprocessedExistentials(ExistentialConcept existentialConcept) {
        assert (!this.m_unprocessedExistentials.isEmpty());
        if (existentialConcept == this.m_unprocessedExistentials.get(this.m_unprocessedExistentials.size() - 1)) {
            this.m_unprocessedExistentials.remove(this.m_unprocessedExistentials.size() - 1);
        } else {
            boolean result = this.m_unprocessedExistentials.remove(existentialConcept);
            assert (result);
        }
        if (this.m_unprocessedExistentials.isEmpty()) {
            this.m_tableau.putExistentialConceptsBuffer(this.m_unprocessedExistentials);
            this.m_unprocessedExistentials = NO_EXISTENTIALS;
        }
    }

    public boolean hasUnprocessedExistentials() {
        return !this.m_unprocessedExistentials.isEmpty();
    }

    public ExistentialConcept getSomeUnprocessedExistential() {
        return this.m_unprocessedExistentials.get(this.m_unprocessedExistentials.size() - 1);
    }

    public Collection<ExistentialConcept> getUnprocessedExistentials() {
        return this.m_unprocessedExistentials;
    }

    public String toString() {
        return String.valueOf(this.m_nodeID);
    }

    static enum NodeState {
        ACTIVE,
        MERGED,
        PRUNED;
        
    }

}

