/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.datetime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;
import org.semanticweb.HermiT.datatypes.datetime.DateTime;
import org.semanticweb.HermiT.datatypes.datetime.DateTimeInterval;

public class DateTimeValueSpaceSubset
implements ValueSpaceSubset {
    protected final List<DateTimeInterval> m_intervals;

    public DateTimeValueSpaceSubset() {
        this.m_intervals = Collections.emptyList();
    }

    public DateTimeValueSpaceSubset(DateTimeInterval interval1, DateTimeInterval interval2) {
        this.m_intervals = new ArrayList<DateTimeInterval>(2);
        if (interval1 != null) {
            this.m_intervals.add(interval1);
        }
        if (interval2 != null) {
            this.m_intervals.add(interval2);
        }
    }

    public DateTimeValueSpaceSubset(List<DateTimeInterval> intervals) {
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
        if (dataValue instanceof DateTime) {
            DateTime dateTime = (DateTime)dataValue;
            for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
                if (!this.m_intervals.get(index).containsDateTime(dateTime)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public void enumerateDataValues(Collection<Object> dataValues) {
        for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
            this.m_intervals.get(index).enumerateDateTimes(dataValues);
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder("xsd:dateTime{");
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

