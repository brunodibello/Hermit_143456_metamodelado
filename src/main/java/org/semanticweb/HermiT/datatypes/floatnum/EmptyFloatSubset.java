package org.semanticweb.HermiT.datatypes.floatnum;

import java.util.Collection;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;

class EmptyFloatSubset
implements ValueSpaceSubset {
    EmptyFloatSubset() {
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

