/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.owlreal;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.semanticweb.HermiT.datatypes.owlreal.BigRational;

public enum NumberRange {
    NOTHING,
    INTEGER,
    DECIMAL,
    RATIONAL,
    REAL;
    

    public boolean isDense() {
        return this.ordinal() >= DECIMAL.ordinal();
    }

    public static NumberRange intersection(NumberRange it1, NumberRange it2) {
        int minOrdinal = Math.min(it1.ordinal(), it2.ordinal());
        return NumberRange.values()[minOrdinal];
    }

    public static NumberRange union(NumberRange it1, NumberRange it2) {
        int maxOrdinal = Math.max(it1.ordinal(), it2.ordinal());
        return NumberRange.values()[maxOrdinal];
    }

    public static boolean isSubsetOf(NumberRange subset, NumberRange superset) {
        return subset.ordinal() <= superset.ordinal();
    }

    public static NumberRange getMostSpecificRange(Number n) {
        if (n instanceof Integer || n instanceof Long || n instanceof BigInteger) {
            return INTEGER;
        }
        if (n instanceof BigDecimal) {
            return DECIMAL;
        }
        if (n instanceof BigRational) {
            return RATIONAL;
        }
        throw new IllegalArgumentException();
    }
}

