/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.blocking;

import org.semanticweb.HermiT.blocking.BlockingSignature;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.DataRange;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public interface DirectBlockingChecker {
    public void initialize(Tableau var1);

    public void clear();

    public boolean isBlockedBy(Node var1, Node var2);

    public int blockingHashCode(Node var1);

    public boolean canBeBlocker(Node var1);

    public boolean canBeBlocked(Node var1);

    public boolean hasBlockingInfoChanged(Node var1);

    public void clearBlockingInfoChanged(Node var1);

    public boolean hasChangedSinceValidation(Node var1);

    public void setHasChangedSinceValidation(Node var1, boolean var2);

    public void nodeInitialized(Node var1);

    public void nodeDestroyed(Node var1);

    public Node assertionAdded(Concept var1, Node var2, boolean var3);

    public Node assertionRemoved(Concept var1, Node var2, boolean var3);

    public Node assertionAdded(DataRange var1, Node var2, boolean var3);

    public Node assertionRemoved(DataRange var1, Node var2, boolean var3);

    public Node assertionAdded(AtomicRole var1, Node var2, Node var3, boolean var4);

    public Node assertionRemoved(AtomicRole var1, Node var2, Node var3, boolean var4);

    public Node nodesMerged(Node var1, Node var2);

    public Node nodesUnmerged(Node var1, Node var2);

    public BlockingSignature getBlockingSignatureFor(Node var1);
}

