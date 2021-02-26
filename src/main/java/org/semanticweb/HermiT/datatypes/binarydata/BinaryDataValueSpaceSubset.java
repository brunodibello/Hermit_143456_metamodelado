package org.semanticweb.HermiT.datatypes.binarydata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;

public class BinaryDataValueSpaceSubset
implements ValueSpaceSubset {
    protected final List<BinaryDataLengthInterval> m_intervals;

    public BinaryDataValueSpaceSubset() {
        this.m_intervals = Collections.emptyList();
    }

    public BinaryDataValueSpaceSubset(BinaryDataLengthInterval interval) {
        this.m_intervals = Collections.singletonList(interval);
    }

    public BinaryDataValueSpaceSubset(List<BinaryDataLengthInterval> intervals) {
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
        if (dataValue instanceof BinaryData) {
            BinaryData binaryData = (BinaryData)dataValue;
            for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
                if (!this.m_intervals.get(index).contains(binaryData)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public void enumerateDataValues(Collection<Object> dataValues) {
        for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
            this.m_intervals.get(index).enumerateValues(dataValues);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("binaryData{");
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

