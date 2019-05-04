/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.owlreal;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;
import org.semanticweb.HermiT.datatypes.owlreal.NumberInterval;
import org.semanticweb.HermiT.datatypes.owlreal.Numbers;

public class OWLRealValueSpaceSubset
implements ValueSpaceSubset {
    protected final List<NumberInterval> m_intervals;

    public OWLRealValueSpaceSubset() {
        this.m_intervals = Collections.emptyList();
    }

    public OWLRealValueSpaceSubset(NumberInterval interval) {
        this.m_intervals = Collections.singletonList(interval);
    }

    public OWLRealValueSpaceSubset(List<NumberInterval> intervals) {
        this.m_intervals = intervals;
    }

    @Override
    public boolean hasCardinalityAtLeast(int number) {
        int left = number;
        for (int index = this.m_intervals.size() - 1; left > 0 && index >= 0; --index) {
            left = this.m_intervals.get(index).subtractSizeFrom(left);
        }
        return left == 0;
    }

    @Override
    public boolean containsDataValue(Object dataValue) {
        Number number;
        if (dataValue instanceof Number && Numbers.isValidNumber(number = (Number)dataValue)) {
            for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
                if (!this.m_intervals.get(index).containsNumber(number)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public void enumerateDataValues(Collection<Object> dataValues) {
        for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
            this.m_intervals.get(index).enumerateNumbers(dataValues);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("owl:real{");
        for (int index = 0; index < this.m_intervals.size(); ++index) {
            if (index != 0) {
                buffer.append(" + ");
            }
            buffer.append(this.m_intervals.get(index).toString());
        }
        buffer.append('}');
        return buffer.toString();
    }
}

