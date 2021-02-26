package org.semanticweb.HermiT.datatypes.floatnum;

import java.util.Collection;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;

class EntireFloatSubset
implements ValueSpaceSubset {
    EntireFloatSubset() {
    }

    @Override
    public boolean hasCardinalityAtLeast(int number) {
        int leftover = FloatInterval.subtractIntervalSizeFrom(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, number);
        return leftover <= 1;
    }

    @Override
    public boolean containsDataValue(Object dataValue) {
        return dataValue instanceof Float;
    }

    @Override
    public void enumerateDataValues(Collection<Object> dataValues) {
        dataValues.add(Float.valueOf(Float.NaN));
        float number = Float.NEGATIVE_INFINITY;
        while (!FloatInterval.areIdentical(number, Float.POSITIVE_INFINITY)) {
            dataValues.add(Float.valueOf(number));
            number = FloatInterval.nextFloat(number);
        }
        dataValues.add(Float.valueOf(Float.POSITIVE_INFINITY));
    }

    public String toString() {
        return "xsd:float";
    }
}

