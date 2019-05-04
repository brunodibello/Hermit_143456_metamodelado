/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import org.semanticweb.HermiT.tableau.TupleTable;

final class TupleTableFullIndex
implements Serializable {
    private static final long serialVersionUID = 5006873858554891684L;
    protected static final int BUCKET_OFFSET = 1;
    protected static final float LOAD_FACTOR = 0.75f;
    protected final TupleTable m_tupleTable;
    protected final int m_indexedArity;
    protected final EntryManager m_entryManager;
    protected int[] m_buckets;
    protected int m_resizeThreshold;
    protected int m_numberOfTuples;
    protected static final int ENTRY_SIZE = 3;
    protected static final int ENTRY_NEXT = 0;
    protected static final int ENTRY_HASH_CODE = 1;
    protected static final int ENTRY_TUPLE_INDEX = 2;
    protected static final int ENTRY_PAGE_SIZE = 512;

    public TupleTableFullIndex(TupleTable tupleTable, int indexedArity) {
        this.m_tupleTable = tupleTable;
        this.m_indexedArity = indexedArity;
        this.m_entryManager = new EntryManager();
        this.clear();
    }

    public int sizeInMemory() {
        return this.m_buckets.length * 4 + this.m_entryManager.size();
    }

    public void clear() {
        this.m_buckets = new int[16];
        this.m_resizeThreshold = (int)((float)this.m_buckets.length * 0.75f);
        this.m_entryManager.clear();
    }

    public int addTuple(Object[] tuple, int tentativeTupleIndex) {
        int hashCode = this.getTupleHashCode(tuple);
        int entryIndex = TupleTableFullIndex.getBucketIndex(hashCode, this.m_buckets.length);
        int entry = this.m_buckets[entryIndex] - 1;
        while (entry != -1) {
            int tupleIndex;
            if (hashCode == this.m_entryManager.getEntryComponent(entry, 1) && this.m_tupleTable.tupleEquals(tuple, tupleIndex = this.m_entryManager.getEntryComponent(entry, 2), this.m_indexedArity)) {
                return tupleIndex;
            }
            entry = this.m_entryManager.getEntryComponent(entry, 0);
        }
        entry = this.m_entryManager.newEntry();
        this.m_entryManager.setEntryComponent(entry, 0, this.m_buckets[entryIndex] - 1);
        this.m_entryManager.setEntryComponent(entry, 1, hashCode);
        this.m_entryManager.setEntryComponent(entry, 2, tentativeTupleIndex);
        this.m_buckets[entryIndex] = entry + 1;
        ++this.m_numberOfTuples;
        if (this.m_numberOfTuples >= this.m_resizeThreshold) {
            this.resizeBuckets();
        }
        return tentativeTupleIndex;
    }

    protected void resizeBuckets() {
        int[] newBuckets = new int[this.m_buckets.length * 2];
        for (int bucketIndex = this.m_buckets.length - 1; bucketIndex >= 0; --bucketIndex) {
            int entry = this.m_buckets[bucketIndex] - 1;
            while (entry != -1) {
                int nextEntry = this.m_entryManager.getEntryComponent(entry, 0);
                int newBucketIndex = TupleTableFullIndex.getBucketIndex(this.m_entryManager.getEntryComponent(entry, 1), newBuckets.length);
                this.m_entryManager.setEntryComponent(entry, 0, newBuckets[newBucketIndex] - 1);
                newBuckets[newBucketIndex] = entry + 1;
                entry = nextEntry;
            }
        }
        this.m_buckets = newBuckets;
        this.m_resizeThreshold = (int)((float)newBuckets.length * 0.75f);
    }

    public int getTupleIndex(Object[] tuple) {
        int hashCode = this.getTupleHashCode(tuple);
        int entryIndex = TupleTableFullIndex.getBucketIndex(hashCode, this.m_buckets.length);
        int entry = this.m_buckets[entryIndex] - 1;
        while (entry != -1) {
            int tupleIndex;
            if (hashCode == this.m_entryManager.getEntryComponent(entry, 1) && this.m_tupleTable.tupleEquals(tuple, tupleIndex = this.m_entryManager.getEntryComponent(entry, 2), this.m_indexedArity)) {
                return tupleIndex;
            }
            entry = this.m_entryManager.getEntryComponent(entry, 0);
        }
        return -1;
    }

    public int getTupleIndex(Object[] tupleBuffer, int[] positionIndexes) {
        int hashCode = this.getTupleHashCode(tupleBuffer, positionIndexes);
        int entryIndex = TupleTableFullIndex.getBucketIndex(hashCode, this.m_buckets.length);
        int entry = this.m_buckets[entryIndex] - 1;
        while (entry != -1) {
            int tupleIndex;
            if (hashCode == this.m_entryManager.getEntryComponent(entry, 1) && this.m_tupleTable.tupleEquals(tupleBuffer, positionIndexes, tupleIndex = this.m_entryManager.getEntryComponent(entry, 2), this.m_indexedArity)) {
                return tupleIndex;
            }
            entry = this.m_entryManager.getEntryComponent(entry, 0);
        }
        return -1;
    }

    public boolean removeTuple(int tupleIndex) {
        int hashCode = 0;
        for (int i = 0; i < this.m_indexedArity; ++i) {
            hashCode += this.m_tupleTable.getTupleObject(tupleIndex, i).hashCode();
        }
        int lastEntry = -1;
        int entryIndex = TupleTableFullIndex.getBucketIndex(hashCode, this.m_buckets.length);
        int entry = this.m_buckets[entryIndex] - 1;
        while (entry != -1) {
            int nextEntry = this.m_entryManager.getEntryComponent(entry, 0);
            if (hashCode == this.m_entryManager.getEntryComponent(entry, 1) && tupleIndex == this.m_entryManager.getEntryComponent(entry, 2)) {
                if (lastEntry == -1) {
                    this.m_buckets[entryIndex] = nextEntry + 1;
                } else {
                    this.m_entryManager.setEntryComponent(lastEntry, 0, nextEntry);
                }
                return true;
            }
            lastEntry = entry;
            entry = nextEntry;
        }
        return false;
    }

    protected int getTupleHashCode(Object[] tuple) {
        int hashCode = 0;
        for (int index = 0; index < this.m_indexedArity; ++index) {
            hashCode += tuple[index].hashCode();
        }
        return hashCode;
    }

    protected int getTupleHashCode(Object[] tupleBuffer, int[] positionIndexes) {
        int hashCode = 0;
        for (int index = 0; index < this.m_indexedArity; ++index) {
            hashCode += tupleBuffer[positionIndexes[index]].hashCode();
        }
        return hashCode;
    }

    protected static int getBucketIndex(int hashCode, int bucketsLength) {
        return hashCode & bucketsLength - 1;
    }

    protected static final class EntryManager
    implements Serializable {
        private static final long serialVersionUID = -7562640774004213308L;
        protected int[] m_entries;
        protected int m_firstFreeEntry;

        public EntryManager() {
            this.clear();
        }

        public int size() {
            return this.m_entries.length * 4;
        }

        public void clear() {
            this.m_entries = new int[1536];
            this.m_firstFreeEntry = 0;
            this.m_entries[this.m_firstFreeEntry + 0] = -1;
        }

        public int getEntryComponent(int entry, int component) {
            return this.m_entries[entry + component];
        }

        public void setEntryComponent(int entry, int component, int value) {
            this.m_entries[entry + component] = value;
        }

        public int newEntry() {
            int result = this.m_firstFreeEntry;
            int nextFreeEntry = this.m_entries[this.m_firstFreeEntry + 0];
            if (nextFreeEntry == -1) {
                this.m_firstFreeEntry += 3;
                if (this.m_firstFreeEntry >= this.m_entries.length) {
                    int[] newEntries = new int[this.m_entries.length + 1536];
                    System.arraycopy(this.m_entries, 0, newEntries, 0, this.m_entries.length);
                    this.m_entries = newEntries;
                }
                this.m_entries[this.m_firstFreeEntry + 0] = -1;
            } else {
                this.m_firstFreeEntry = nextFreeEntry;
            }
            return result;
        }

        public void deleteEntry(int entry) {
            this.m_entries[entry + 0] = this.m_firstFreeEntry;
            this.m_firstFreeEntry = entry;
        }
    }

}

