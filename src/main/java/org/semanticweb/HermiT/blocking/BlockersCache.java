/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.blocking;

import java.io.Serializable;
import org.semanticweb.HermiT.blocking.DirectBlockingChecker;
import org.semanticweb.HermiT.tableau.Node;

class BlockersCache
implements Serializable {
    private static final long serialVersionUID = -7692825443489644667L;
    protected final DirectBlockingChecker m_directBlockingChecker;
    protected CacheEntry[] m_buckets;
    protected int m_numberOfElements;
    protected int m_threshold;
    protected CacheEntry m_emptyEntries;

    public BlockersCache(DirectBlockingChecker directBlockingChecker) {
        this.m_directBlockingChecker = directBlockingChecker;
        this.clear();
    }

    public boolean isEmpty() {
        return this.m_numberOfElements == 0;
    }

    public void clear() {
        this.m_buckets = new CacheEntry[1024];
        this.m_threshold = (int)((double)this.m_buckets.length * 0.75);
        this.m_numberOfElements = 0;
        this.m_emptyEntries = null;
    }

    public void removeNode(Node node) {
        CacheEntry removeEntry = (CacheEntry)node.getBlockingCargo();
        if (removeEntry != null) {
            int bucketIndex = BlockersCache.getIndexFor(removeEntry.m_hashCode, this.m_buckets.length);
            CacheEntry lastEntry = null;
            CacheEntry entry = this.m_buckets[bucketIndex];
            while (entry != null) {
                if (entry == removeEntry) {
                    if (lastEntry == null) {
                        this.m_buckets[bucketIndex] = entry.m_nextEntry;
                    } else {
                        lastEntry.m_nextEntry = entry.m_nextEntry;
                    }
                    entry.m_nextEntry = this.m_emptyEntries;
                    entry.m_node = null;
                    entry.m_hashCode = 0;
                    this.m_emptyEntries = entry;
                    --this.m_numberOfElements;
                    node.setBlockingCargo(null);
                    return;
                }
                lastEntry = entry;
                entry = entry.m_nextEntry;
            }
            //throw new IllegalStateException("Internal error: entry not in cache!");
        }
    }

    public void addNode(Node node) {
        int hashCode = this.m_directBlockingChecker.blockingHashCode(node);
        int bucketIndex = BlockersCache.getIndexFor(hashCode, this.m_buckets.length);
        CacheEntry entry = this.m_buckets[bucketIndex];
        while (entry != null) {
            if (hashCode == entry.m_hashCode && this.m_directBlockingChecker.isBlockedBy(entry.m_node, node)) {
                throw new IllegalStateException("Internal error: node already in the cache!");
            }
            entry = entry.m_nextEntry;
        }
        if (this.m_emptyEntries == null) {
            entry = new CacheEntry();
        } else {
            entry = this.m_emptyEntries;
            this.m_emptyEntries = this.m_emptyEntries.m_nextEntry;
        }
        entry.initialize(node, hashCode, this.m_buckets[bucketIndex]);
        this.m_buckets[bucketIndex] = entry;
        node.setBlockingCargo(entry);
        ++this.m_numberOfElements;
        if (this.m_numberOfElements >= this.m_threshold) {
            this.resize(this.m_buckets.length * 2);
        }
    }

    protected void resize(int newCapacity) {
        CacheEntry[] newBuckets = new CacheEntry[newCapacity];
        for (int i = 0; i < this.m_buckets.length; ++i) {
            CacheEntry entry = this.m_buckets[i];
            while (entry != null) {
                CacheEntry nextEntry = entry.m_nextEntry;
                int newIndex = BlockersCache.getIndexFor(entry.m_hashCode, newCapacity);
                entry.m_nextEntry = newBuckets[newIndex];
                newBuckets[newIndex] = entry;
                entry = nextEntry;
            }
        }
        this.m_buckets = newBuckets;
        this.m_threshold = (int)((double)newCapacity * 0.75);
    }

    public Node getBlocker(Node node) {
        if (this.m_directBlockingChecker.canBeBlocked(node)) {
            int hashCode = this.m_directBlockingChecker.blockingHashCode(node);
            int bucketIndex = BlockersCache.getIndexFor(hashCode, this.m_buckets.length);
            CacheEntry entry = this.m_buckets[bucketIndex];
            while (entry != null) {
                if (hashCode == entry.m_hashCode && this.m_directBlockingChecker.isBlockedBy(entry.m_node, node)) {
                    return entry.m_node;
                }
                entry = entry.m_nextEntry;
            }
        }
        return null;
    }

    protected static int getIndexFor(int _hashCode, int tableLength) {
        int hashCode = _hashCode;
        hashCode += ~ (hashCode << 9);
        hashCode ^= hashCode >>> 14;
        hashCode += hashCode << 4;
        hashCode ^= hashCode >>> 10;
        return hashCode & tableLength - 1;
    }

    public static class CacheEntry
    implements Serializable {
        private static final long serialVersionUID = -7047487963170250200L;
        protected Node m_node;
        protected int m_hashCode;
        protected CacheEntry m_nextEntry;

        public void initialize(Node node, int hashCode, CacheEntry nextEntry) {
            this.m_node = node;
            this.m_hashCode = hashCode;
            this.m_nextEntry = nextEntry;
        }
    }

}

