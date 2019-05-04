/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public abstract class InterningManager<E> {
    protected static final double LOAD_FACTOR = 0.75;
    protected final ReferenceQueue<E> m_referenceQueue = new ReferenceQueue();
    protected Entry<E>[] m_entries = this.createEntries(16);
    protected int m_size = 0;
    protected int m_resizeThreshold = 12;

    public synchronized E intern(E object) {
        this.processQueue();
        int hashCode = this.getHashCode(object);
        int objectEntryIndex = InterningManager.getIndexFor(hashCode, this.m_entries.length);
        Entry<E> previousEntry = null;
        Entry<E> entry = this.m_entries[objectEntryIndex];
        while (entry != null) {
            if (hashCode == entry.m_hashCode) {
                Object entryObject = entry.get();
                if (entryObject == null) {
                    if (previousEntry == null) {
                        this.m_entries[objectEntryIndex] = entry.m_next;
                    } else {
                        previousEntry.m_next = entry.m_next;
                    }
                    --this.m_size;
                } else if (this.equal(object, (E) entryObject)) {
                    return (E)entryObject;
                }
            }
            previousEntry = entry;
            entry = entry.m_next;
        }
        if (this.m_size >= this.m_resizeThreshold) {
            int newEntriesLength = this.m_entries.length * 2;
            Entry<E>[] newEntries = this.createEntries(newEntriesLength);
            for (int entryIndex = 0; entryIndex < this.m_entries.length; ++entryIndex) {
                Entry<E> currentEntry = this.m_entries[entryIndex];
                while (currentEntry != null) {
                    Entry nextEntry = currentEntry.m_next;
                    if (currentEntry.get() == null) {
                        --this.m_size;
                    } else {
                        int newIndex = InterningManager.getIndexFor(currentEntry.m_hashCode, newEntriesLength);
                        currentEntry.m_next = newEntries[newIndex];
                        newEntries[newIndex] = currentEntry;
                    }
                    currentEntry = nextEntry;
                }
            }
            this.m_entries = newEntries;
            this.m_resizeThreshold = (int)((double)newEntriesLength * 0.75);
            objectEntryIndex = InterningManager.getIndexFor(hashCode, this.m_entries.length);
        }
        Entry<E> newEntry = new Entry<E>(object, this.m_referenceQueue, hashCode, this.m_entries[objectEntryIndex]);
        this.m_entries[objectEntryIndex] = newEntry;
        ++this.m_size;
        return object;
    }

    protected static final int getIndexFor(int hashCode, int entriesLength) {
        return hashCode & entriesLength - 1;
    }

    protected void removeEntry(Entry<E> entry) {
        int index = InterningManager.getIndexFor(entry.m_hashCode, this.m_entries.length);
        Entry<E> previousEntry = null;
        Entry<E> current = this.m_entries[index];
        while (current != null) {
            if (current == entry) {
                --this.m_size;
                if (previousEntry == null) {
                    this.m_entries[index] = current.m_next;
                } else {
                    previousEntry.m_next = current.m_next;
                }
                return;
            }
            previousEntry = current;
            current = current.m_next;
        }
    }

    protected void processQueue() {
        Entry entry = (Entry)this.m_referenceQueue.poll();
        while (entry != null) {
            this.removeEntry(entry);
            entry = (Entry)this.m_referenceQueue.poll();
        }
    }

    protected final Entry<E>[] createEntries(int size) {
        return new Entry[size];
    }

    protected abstract int getHashCode(E var1);

    protected abstract boolean equal(E var1, E var2);

    protected static class Entry<E>
    extends WeakReference<E> {
        public final int m_hashCode;
        public Entry<E> m_next;

        public Entry(E object, ReferenceQueue<E> referenceQueue, int hashCode, Entry<E> next) {
            super(object, referenceQueue);
            this.m_hashCode = hashCode;
            this.m_next = next;
        }
    }

}

