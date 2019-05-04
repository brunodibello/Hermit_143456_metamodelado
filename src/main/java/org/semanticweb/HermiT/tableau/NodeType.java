/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

public enum NodeType {
    NAMED_NODE(0, false, true),
    NI_NODE(1, false, true),
    ROOT_CONSTANT_NODE(1, false, false),
    TREE_NODE(2, true, true),
    GRAPH_NODE(2, true, true),
    CONCRETE_NODE(2, false, false);
    
    protected final int m_mergePrecedence;
    protected final boolean m_isNITarget;
    protected final boolean m_isAbstract;

    private NodeType(int mergePrecedence, boolean isNITarget, boolean isAbstract) {
        this.m_mergePrecedence = mergePrecedence;
        this.m_isNITarget = isNITarget;
        this.m_isAbstract = isAbstract;
    }

    public int getMergePrecedence() {
        return this.m_mergePrecedence;
    }

    public boolean isNITarget() {
        return this.m_isNITarget;
    }

    public boolean isAbstract() {
        return this.m_isAbstract;
    }
}

