package org.semanticweb.HermiT.model;

import java.io.Serializable;
import org.semanticweb.HermiT.Prefixes;

public class NodeIDsAscendingOrEqual
implements DLPredicate,
Serializable {
    private static final long serialVersionUID = 7197886700065386931L;
    protected final int m_arity;
    protected static final InterningManager<NodeIDsAscendingOrEqual> s_interningManager = new InterningManager<NodeIDsAscendingOrEqual>(){

        @Override
        protected boolean equal(NodeIDsAscendingOrEqual object1, NodeIDsAscendingOrEqual object2) {
            return object1.m_arity == object2.m_arity;
        }

        @Override
        protected int getHashCode(NodeIDsAscendingOrEqual object) {
            return object.m_arity;
        }
    };

    protected NodeIDsAscendingOrEqual(int arity) {
        this.m_arity = arity;
    }

    @Override
    public int getArity() {
        return this.m_arity;
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    @Override
    public String toString(Prefixes prefixes) {
        return "NodeIDsAscendingOrEqual";
    }

    public static NodeIDsAscendingOrEqual create(int arity) {
        return s_interningManager.intern(new NodeIDsAscendingOrEqual(arity));
    }

}

