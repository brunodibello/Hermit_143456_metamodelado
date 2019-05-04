/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes;

import java.util.Collection;

public interface ValueSpaceSubset {
    public boolean hasCardinalityAtLeast(int var1);

    public boolean containsDataValue(Object var1);

    public void enumerateDataValues(Collection<Object> var1);
}

