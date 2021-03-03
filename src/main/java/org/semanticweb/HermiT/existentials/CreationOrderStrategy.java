package org.semanticweb.HermiT.existentials;

import java.io.Serializable;
import org.semanticweb.HermiT.blocking.BlockingStrategy;
import org.semanticweb.HermiT.model.AtLeast;
import org.semanticweb.HermiT.tableau.Node;

public class CreationOrderStrategy
extends AbstractExpansionStrategy
implements Serializable {
    private static final long serialVersionUID = -64673639237063636L;

    public CreationOrderStrategy(BlockingStrategy strategy) {
        super(strategy, true);
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @Override
    protected void expandExistential(AtLeast atLeast, Node forNode) {
        this.m_existentialExpansionManager.expand(atLeast, forNode);
        this.m_existentialExpansionManager.markExistentialProcessed(atLeast, forNode);
    }
}

