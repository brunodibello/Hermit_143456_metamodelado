/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.blocking;

import java.io.Serializable;
import org.semanticweb.HermiT.blocking.BlockingSignature;
import org.semanticweb.HermiT.blocking.DirectBlockingChecker;
import org.semanticweb.HermiT.tableau.Node;

public class BlockingSignatureCache
implements Serializable {
    private static final long serialVersionUID = -7692825443489644667L;
    protected final DirectBlockingChecker m_directBlockingChecker;
    protected BlockingSignature[] m_buckets;
    protected int m_numberOfElements;
    protected int m_threshold;

    public BlockingSignatureCache(DirectBlockingChecker directBlockingChecker) {
        this.m_directBlockingChecker = directBlockingChecker;
        this.m_buckets = new BlockingSignature[1024];
        this.m_threshold = (int)((double)this.m_buckets.length * 0.75);
        this.m_numberOfElements = 0;
    }

    public boolean isEmpty() {
        return this.m_numberOfElements == 0;
    }

    public boolean addNode(Node node) {
        int hashCode = this.m_directBlockingChecker.blockingHashCode(node);
        int bucketIndex = BlockingSignatureCache.getIndexFor(hashCode, this.m_buckets.length);
        BlockingSignature entry = this.m_buckets[bucketIndex];
        while (entry != null) {
            if (hashCode == entry.hashCode() && entry.blocksNode(node)) {
                return false;
            }
            entry = entry.m_nextEntry;
        }
        entry = this.m_directBlockingChecker.getBlockingSignatureFor(node);
        entry.m_nextEntry = this.m_buckets[bucketIndex];
        this.m_buckets[bucketIndex] = entry;
        ++this.m_numberOfElements;
        if (this.m_numberOfElements >= this.m_threshold) {
            this.resize(this.m_buckets.length * 2);
        }
        return true;
    }

    protected void resize(int newCapacity) {
        BlockingSignature[] newBuckets = new BlockingSignature[newCapacity];
        for (int i = 0; i < this.m_buckets.length; ++i) {
            BlockingSignature entry = this.m_buckets[i];
            while (entry != null) {
                BlockingSignature nextEntry = entry.m_nextEntry;
                int newIndex = BlockingSignatureCache.getIndexFor(entry.hashCode(), newCapacity);
                entry.m_nextEntry = newBuckets[newIndex];
                newBuckets[newIndex] = entry;
                entry = nextEntry;
            }
        }
        this.m_buckets = newBuckets;
        this.m_threshold = (int)((double)newCapacity * 0.75);
    }

    public boolean containsSignature(Node node) {
        if (this.m_directBlockingChecker.canBeBlocked(node)) {
            int hashCode = this.m_directBlockingChecker.blockingHashCode(node);
            int bucketIndex = BlockingSignatureCache.getIndexFor(hashCode, this.m_buckets.length);
            BlockingSignature entry = this.m_buckets[bucketIndex];
            while (entry != null) {
                if (hashCode == entry.hashCode() && entry.blocksNode(node)) {
                    return true;
                }
                entry = entry.m_nextEntry;
            }
        }
        return false;
    }

    protected static int getIndexFor(int _hashCode, int tableLength) {
        int hashCode = _hashCode;
        hashCode += ~ (hashCode << 9);
        hashCode ^= hashCode >>> 14;
        hashCode += hashCode << 4;
        hashCode ^= hashCode >>> 10;
        return hashCode & tableLength - 1;
    }
}

