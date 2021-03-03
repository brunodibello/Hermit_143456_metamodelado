package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.InternalDatatype;

public class ExtensionTableWithFullIndex
extends ExtensionTable {
    private static final long serialVersionUID = 2856811178050960058L;
    protected final TupleTableFullIndex m_tupleTableFullIndex;
    protected final Object[] m_auxiliaryTuple;

    public ExtensionTableWithFullIndex(Tableau tableau, int tupleArity, boolean needsDependencySets) {
        super(tableau, tupleArity, needsDependencySets);
        this.m_tupleTableFullIndex = new TupleTableFullIndex(this.m_tupleTable, this.m_tupleArity);
        this.m_auxiliaryTuple = new Object[this.m_tupleArity];
    }

    @Override
    public int sizeInMemory() {
        return this.m_tupleTable.sizeInMemory() + this.m_tupleTableFullIndex.sizeInMemory();
    }

    @Override
    public boolean addTuple(Object[] tuple, DependencySet dependencySet, boolean isCore) {
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.addFactStarted(tuple, isCore);
        }
        if (!(!this.isTupleActive(tuple) || !this.m_tableau.m_needsThingExtension && AtomicConcept.THING.equals(tuple[0]) || !this.m_tableau.m_needsRDFSLiteralExtension && InternalDatatype.RDFS_LITERAL.equals(tuple[0]))) {
            int firstFreeTupleIndex = this.m_tupleTable.getFirstFreeTupleIndex();
            int addTupleIndex = this.m_tupleTableFullIndex.addTuple(tuple, firstFreeTupleIndex);
            if (addTupleIndex == firstFreeTupleIndex) {
                this.m_tupleTable.addTuple(tuple);
                this.m_dependencySetManager.setDependencySet(addTupleIndex, dependencySet);
                this.m_coreManager.setCore(addTupleIndex, isCore);
                this.m_afterDeltaNewTupleIndex = this.m_tupleTable.getFirstFreeTupleIndex();
                if (this.m_tableauMonitor != null) {
                    this.m_tableauMonitor.addFactFinished(tuple, isCore, true);
                }
                this.postAdd(tuple, dependencySet, addTupleIndex, isCore);
                return true;
            }
            if (isCore && !this.m_coreManager.isCore(addTupleIndex)) {
                this.m_coreManager.addCore(addTupleIndex);
                Object dlPredicateObject = tuple[0];
                if (dlPredicateObject instanceof Concept) {
                    this.m_tableau.m_existentialExpansionStrategy.assertionCoreSet((Concept)dlPredicateObject, (Node)tuple[1]);
                } else if (dlPredicateObject instanceof AtomicRole) {
                    this.m_tableau.m_existentialExpansionStrategy.assertionCoreSet((AtomicRole)dlPredicateObject, (Node)tuple[1], (Node)tuple[2]);
                }
            }
        }
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.addFactFinished(tuple, isCore, false);
        }
        return false;
    }

    @Override
    public boolean containsTuple(Object[] tuple) {
        int tupleIndex = this.m_tupleTableFullIndex.getTupleIndex(tuple);
        return tupleIndex != -1 && this.isTupleActive(tupleIndex);
    }

    @Override
    public DependencySet getDependencySet(Object[] tuple) {
        int tupleIndex = this.m_tupleTableFullIndex.getTupleIndex(tuple);
        if (tupleIndex == -1) {
            return null;
        }
        return this.m_dependencySetManager.getDependencySet(tupleIndex);
    }

    @Override
    public boolean isCore(Object[] tuple) {
        int tupleIndex = this.m_tupleTableFullIndex.getTupleIndex(tuple);
        if (tupleIndex == -1) {
            return false;
        }
        return this.m_coreManager.isCore(tupleIndex);
    }

    @Override
    public ExtensionTable.Retrieval createRetrieval(int[] bindingPositions, Object[] bindingsBuffer, Object[] tupleBuffer, boolean ownsBuffers, ExtensionTable.View extensionView) {
        int numberOfBindings = 0;
        for (int index = this.m_tupleArity - 1; index >= 0; --index) {
            if (bindingPositions[index] == -1) continue;
            ++numberOfBindings;
        }
        if (numberOfBindings == this.m_tupleArity) {
            return new IndexedRetrieval(bindingPositions, bindingsBuffer, tupleBuffer, ownsBuffers, extensionView);
        }
        return new ExtensionTable.UnindexedRetrieval(bindingPositions, bindingsBuffer, tupleBuffer, ownsBuffers, extensionView);
    }

    @Override
    protected void removeTuple(int tupleIndex) {
        this.m_tupleTableFullIndex.removeTuple(tupleIndex);
        this.m_tupleTable.retrieveTuple(this.m_auxiliaryTuple, tupleIndex);
        this.postRemove(this.m_auxiliaryTuple, tupleIndex);
    }

    @Override
    public void clear() {
        super.clear();
        this.m_tupleTableFullIndex.clear();
    }

    protected class IndexedRetrieval
    implements ExtensionTable.Retrieval,
    Serializable {
        private static final long serialVersionUID = 5984560476970027366L;
        protected final int[] m_bindingPositions;
        protected final Object[] m_bindingsBuffer;
        protected final Object[] m_tupleBuffer;
        protected final boolean m_ownsBuffers;
        protected final ExtensionTable.View m_extensionView;
        protected int m_currentTupleIndex;

        public IndexedRetrieval(int[] bindingPositions, Object[] bindingsBuffer, Object[] tupleBuffer, boolean ownsBuffers, ExtensionTable.View extensionView) {
            this.m_bindingPositions = bindingPositions;
            this.m_bindingsBuffer = bindingsBuffer;
            this.m_tupleBuffer = tupleBuffer;
            this.m_ownsBuffers = ownsBuffers;
            this.m_extensionView = extensionView;
        }

        @Override
        public ExtensionTable getExtensionTable() {
            return ExtensionTableWithFullIndex.this;
        }

        @Override
        public ExtensionTable.View getExtensionView() {
            return this.m_extensionView;
        }

        @Override
        public void clear() {
            if (this.m_ownsBuffers) {
                int index;
                for (index = this.m_bindingsBuffer.length - 1; index >= 0; --index) {
                    this.m_bindingsBuffer[index] = null;
                }
                for (index = this.m_tupleBuffer.length - 1; index >= 0; --index) {
                    this.m_tupleBuffer[index] = null;
                }
            }
        }

        @Override
        public int[] getBindingPositions() {
            return this.m_bindingPositions;
        }

        @Override
        public Object[] getBindingsBuffer() {
            return this.m_bindingsBuffer;
        }

        @Override
        public Object[] getTupleBuffer() {
            return this.m_tupleBuffer;
        }

        @Override
        public DependencySet getDependencySet() {
            if (this.m_currentTupleIndex == -1) {
                return null;
            }
            return ExtensionTableWithFullIndex.this.m_dependencySetManager.getDependencySet(this.m_currentTupleIndex);
        }

        @Override
        public boolean isCore() {
            if (this.m_currentTupleIndex == -1) {
                return false;
            }
            return ExtensionTableWithFullIndex.this.m_coreManager.isCore(this.m_currentTupleIndex);
        }

        @Override
        public void open() {
            this.m_currentTupleIndex = ExtensionTableWithFullIndex.this.m_tupleTableFullIndex.getTupleIndex(this.m_bindingsBuffer, this.m_bindingPositions);
            switch (this.m_extensionView) {
                case EXTENSION_THIS: {
                    if (0 <= this.m_currentTupleIndex && this.m_currentTupleIndex < ExtensionTableWithFullIndex.this.m_afterExtensionThisTupleIndex) break;
                    this.m_currentTupleIndex = -1;
                    break;
                }
                case EXTENSION_OLD: {
                    if (0 <= this.m_currentTupleIndex && this.m_currentTupleIndex < ExtensionTableWithFullIndex.this.m_afterExtensionOldTupleIndex) break;
                    this.m_currentTupleIndex = -1;
                    break;
                }
                case DELTA_OLD: {
                    if (ExtensionTableWithFullIndex.this.m_afterExtensionOldTupleIndex <= this.m_currentTupleIndex && this.m_currentTupleIndex < ExtensionTableWithFullIndex.this.m_afterExtensionThisTupleIndex) break;
                    this.m_currentTupleIndex = -1;
                    break;
                }
                case TOTAL: {
                    if (0 <= this.m_currentTupleIndex && this.m_currentTupleIndex < ExtensionTableWithFullIndex.this.m_afterDeltaNewTupleIndex) break;
                    this.m_currentTupleIndex = -1;
                    break;
                }
            }
            if (this.m_currentTupleIndex != -1) {
                ExtensionTableWithFullIndex.this.m_tupleTable.retrieveTuple(this.m_tupleBuffer, this.m_currentTupleIndex);
                if (!ExtensionTableWithFullIndex.this.isTupleActive(this.m_tupleBuffer)) {
                    this.m_currentTupleIndex = -1;
                }
            }
        }

        @Override
        public boolean afterLast() {
            return this.m_currentTupleIndex == -1;
        }

        @Override
        public int getCurrentTupleIndex() {
            return this.m_currentTupleIndex;
        }

        @Override
        public void next() {
            ++this.m_currentTupleIndex;
        }
    }

}

