package org.semanticweb.HermiT.datatypes.rdfplainliteral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;

public class RDFPlainLiteralLengthValueSpaceSubset
implements ValueSpaceSubset {
    protected final List<RDFPlainLiteralLengthInterval> m_intervals;

    public RDFPlainLiteralLengthValueSpaceSubset() {
        this.m_intervals = Collections.emptyList();
    }

    public RDFPlainLiteralLengthValueSpaceSubset(RDFPlainLiteralLengthInterval interval) {
        this.m_intervals = Collections.singletonList(interval);
    }

    public RDFPlainLiteralLengthValueSpaceSubset(RDFPlainLiteralLengthInterval interval1, RDFPlainLiteralLengthInterval interval2) {
        this.m_intervals = new ArrayList<RDFPlainLiteralLengthInterval>(2);
        this.m_intervals.add(interval1);
        this.m_intervals.add(interval2);
    }

    public RDFPlainLiteralLengthValueSpaceSubset(List<RDFPlainLiteralLengthInterval> intervals) {
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
        block3 : {
            block2 : {
                if (!(dataValue instanceof String)) break block2;
                String value = (String)dataValue;
                for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
                    if (!this.m_intervals.get(index).contains(value)) continue;
                    return true;
                }
                break block3;
            }
            if (!(dataValue instanceof RDFPlainLiteralDataValue)) break block3;
            RDFPlainLiteralDataValue value = (RDFPlainLiteralDataValue)dataValue;
            for (int index = this.m_intervals.size() - 1; index >= 0; --index) {
                if (!this.m_intervals.get(index).contains(value)) continue;
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
        StringBuilder buffer = new StringBuilder("rdf:PlainLiteral{");
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

