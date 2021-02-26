/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.blocking;

import java.io.Serializable;
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

public class AnywhereBlocking
implements BlockingStrategy,
Serializable {
    private static final long serialVersionUID = -2959900333817197464L;
    protected final DirectBlockingChecker m_directBlockingChecker;
    protected final BlockersCache m_currentBlockersCache;
    protected final BlockingSignatureCache m_blockingSignatureCache;
    protected Tableau m_tableau;
    protected boolean m_useBlockingSignatureCache;
    protected Node m_firstChangedNode;

    public AnywhereBlocking(DirectBlockingChecker directBlockingChecker, BlockingSignatureCache blockingSignatureCache) {
        this.m_directBlockingChecker = directBlockingChecker;
        this.m_currentBlockersCache = new BlockersCache(this.m_directBlockingChecker);
        this.m_blockingSignatureCache = blockingSignatureCache;
    }

    @Override
    public void initialize(Tableau tableau) {
        this.m_tableau = tableau;
        this.m_directBlockingChecker.initialize(tableau);
        this.updateBlockingSignatureCacheUsage();
    }

    @Override
    public void additionalDLOntologySet(DLOntology additionalDLOntology) {
        this.updateBlockingSignatureCacheUsage();
    }

    @Override
    public void additionalDLOntologyCleared() {
        this.updateBlockingSignatureCacheUsage();
    }

    protected void updateBlockingSignatureCacheUsage() {
        this.m_useBlockingSignatureCache = this.m_tableau.getAdditionalHyperresolutionManager() == null;
    }

    @Override
    public void clear() {
        this.m_currentBlockersCache.clear();
        this.m_firstChangedNode = null;
        this.m_directBlockingChecker.clear();
    }

    @Override
    public void computeBlocking(boolean finalChance) {
        if (this.m_firstChangedNode != null) {
            boolean checkBlockingSignatureCache;
            Node node;
            for (node = this.m_firstChangedNode; node != null; node = node.getNextTableauNode()) {
                this.m_currentBlockersCache.removeNode(node);
            }
            boolean bl = checkBlockingSignatureCache = this.m_useBlockingSignatureCache && this.m_blockingSignatureCache != null && !this.m_blockingSignatureCache.isEmpty();
            for (node = this.m_firstChangedNode; node != null; node = node.getNextTableauNode()) {
                if (!node.isActive() || !this.m_directBlockingChecker.canBeBlocked(node) && !this.m_directBlockingChecker.canBeBlocker(node)) continue;
                if (this.m_directBlockingChecker.hasBlockingInfoChanged(node) || !node.isDirectlyBlocked() || node.getBlocker().getNodeID() >= this.m_firstChangedNode.getNodeID()) {
                    Node blocker;
                    Node parent = node.getParent();
                    if (parent == null) {
                        node.setBlocked(null, false);
                    } else if (parent.isBlocked()) {
                        node.setBlocked(parent, false);
                    } else if (checkBlockingSignatureCache) {
                        if (this.m_blockingSignatureCache.containsSignature(node)) {
                            node.setBlocked(Node.SIGNATURE_CACHE_BLOCKER, true);
                        } else {
                        	blocker = this.m_currentBlockersCache.getBlocker(node);
                            node.setBlocked(blocker, (blocker = this.m_currentBlockersCache.getBlocker(node)) != null);
                        }
                    } else {
                    	blocker = this.m_currentBlockersCache.getBlocker(node);
                        node.setBlocked(blocker, (blocker = this.m_currentBlockersCache.getBlocker(node)) != null);
                    }
                    if (!node.isBlocked() && this.m_directBlockingChecker.canBeBlocker(node)) {
                        this.m_currentBlockersCache.addNode(node);
                    }
                }
                this.m_directBlockingChecker.clearBlockingInfoChanged(node);
            }
            this.m_firstChangedNode = null;
        }
    }

    @Override
    public boolean isPermanentAssertion(Concept concept, Node node) {
        return true;
    }

    @Override
    public boolean isPermanentAssertion(DataRange range, Node node) {
        return true;
    }

    @Override
    public void assertionAdded(Concept concept, Node node, boolean isCore) {
        this.updateNodeChange(this.m_directBlockingChecker.assertionAdded(concept, node, isCore));
    }

    @Override
    public void assertionCoreSet(Concept concept, Node node) {
    }

    @Override
    public void assertionRemoved(Concept concept, Node node, boolean isCore) {
        this.updateNodeChange(this.m_directBlockingChecker.assertionRemoved(concept, node, isCore));
    }

    @Override
    public void assertionAdded(DataRange range, Node node, boolean isCore) {
        this.m_directBlockingChecker.assertionAdded(range, node, isCore);
    }

    @Override
    public void assertionCoreSet(DataRange range, Node node) {
    }

    @Override
    public void assertionRemoved(DataRange range, Node node, boolean isCore) {
        this.m_directBlockingChecker.assertionRemoved(range, node, isCore);
    }

    @Override
    public void assertionAdded(AtomicRole atomicRole, Node nodeFrom, Node nodeTo, boolean isCore) {
        this.updateNodeChange(this.m_directBlockingChecker.assertionAdded(atomicRole, nodeFrom, nodeTo, isCore));
    }

    @Override
    public void nodesMerged(Node mergeFrom, Node mergeInto) {
        this.updateNodeChange(this.m_directBlockingChecker.nodesMerged(mergeFrom, mergeInto));
    }

    @Override
    public void nodesUnmerged(Node mergeFrom, Node mergeInto) {
        this.updateNodeChange(this.m_directBlockingChecker.nodesUnmerged(mergeFrom, mergeInto));
    }

    @Override
    public void assertionCoreSet(AtomicRole atomicRole, Node nodeFrom, Node nodeTo) {
    }

    @Override
    public void assertionRemoved(AtomicRole atomicRole, Node nodeFrom, Node nodeTo, boolean isCore) {
        this.updateNodeChange(this.m_directBlockingChecker.assertionRemoved(atomicRole, nodeFrom, nodeTo, isCore));
    }

    @Override
    public void nodeStatusChanged(Node node) {
        this.updateNodeChange(node);
    }

    protected final void updateNodeChange(Node node) {
        if (node != null && (this.m_firstChangedNode == null || node.getNodeID() < this.m_firstChangedNode.getNodeID())) {
            this.m_firstChangedNode = node;
        }
    }

    @Override
    public void nodeInitialized(Node node) {
        this.m_directBlockingChecker.nodeInitialized(node);
    }

    @Override
    public void nodeDestroyed(Node node) {
        this.m_currentBlockersCache.removeNode(node);
        this.m_directBlockingChecker.nodeDestroyed(node);
        if (this.m_firstChangedNode != null && this.m_firstChangedNode.getNodeID() >= node.getNodeID()) {
            this.m_firstChangedNode = null;
        }
    }

    @Override
    public void modelFound() {
        if (this.m_useBlockingSignatureCache && this.m_blockingSignatureCache != null) {
            assert (this.m_firstChangedNode == null);
            for (Node node = this.m_tableau.getFirstTableauNode(); node != null; node = node.getNextTableauNode()) {
                if (!node.isActive() || node.isBlocked() || !this.m_directBlockingChecker.canBeBlocker(node)) continue;
                this.m_blockingSignatureCache.addNode(node);
            }
        }
    }

    @Override
    public boolean isExact() {
        return true;
    }

    @Override
    public void dlClauseBodyCompiled(List<DLClauseEvaluator.Worker> workers, DLClause dlClause, List<Variable> variables, Object[] valuesBuffer, boolean[] coreVariables) {
        for (int i = 0; i < coreVariables.length; ++i) {
            coreVariables[i] = true;
        }
    }
}

