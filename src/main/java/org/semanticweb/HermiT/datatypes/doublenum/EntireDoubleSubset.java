package org.semanticweb.HermiT.datatypes.doublenum;

import java.util.Collection;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;

public class EntireDoubleSubset
implements ValueSpaceSubset {
    @Override
    public boolean hasCardinalityAtLeast(int number) {
        int leftover = DoubleInterval.subtractIntervalSizeFrom(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, number);
        return leftover <= 1;
    }

    @Override
    public boolean containsDataValue(Object dataValue) {
        return dataValue instanceof Double;
    }

    @Override
    public void enumerateDataValues(Collection<Object> dataValues) {
        dataValues.add(Double.NaN);
        double number = Double.NEGATIVE_INFINITY;
        while (!DoubleInterval.areIdentical(number, Double.POSITIVE_INFINITY)) {
            dataValues.add(number);
            number = DoubleInterval.nextDouble(number);
        }
        dataValues.add(Double.POSITIVE_INFINITY);
    }

    public String toString() {
        return "xsd:double";
    }
}

