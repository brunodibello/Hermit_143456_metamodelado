/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.doublenum;

import java.util.Collection;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;

class EmptyDoubleSubset
implements ValueSpaceSubset {
    EmptyDoubleSubset() {
    }

    @Override
    public boolean hasCardinalityAtLeast(int number) {
        return number <= 0;
    }

    @Override
    public boolean containsDataValue(Object dataValue) {
        return false;
    }

    @Override
    public void enumerateDataValues(Collection<Object> dataValues) {
    }
}

