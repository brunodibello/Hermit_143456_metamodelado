/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.binarydata;

import java.util.Collection;
import org.semanticweb.HermiT.datatypes.binarydata.BinaryData;
import org.semanticweb.HermiT.datatypes.binarydata.BinaryDataType;

public class BinaryDataLengthInterval {
    protected final BinaryDataType m_binaryDataType;
    protected final int m_minLength;
    protected final int m_maxLength;

    public BinaryDataLengthInterval(BinaryDataType binaryDataType, int minLength, int maxLength) {
        assert (!BinaryDataLengthInterval.isIntervalEmpty(minLength, maxLength));
        this.m_binaryDataType = binaryDataType;
        this.m_minLength = minLength;
        this.m_maxLength = maxLength;
    }

    public BinaryDataLengthInterval intersectWith(BinaryDataLengthInterval that) {
        int newMaxLength;
        if (this.m_binaryDataType != that.m_binaryDataType) {
            return null;
        }
        int newMinLength = Math.max(this.m_minLength, that.m_minLength);
        if (BinaryDataLengthInterval.isIntervalEmpty(newMinLength, newMaxLength = Math.min(this.m_maxLength, that.m_maxLength))) {
            return null;
        }
        if (this.isEqual(this.m_binaryDataType, newMinLength, newMaxLength)) {
            return this;
        }
        if (that.isEqual(this.m_binaryDataType, newMinLength, newMaxLength)) {
            return that;
        }
        return new BinaryDataLengthInterval(this.m_binaryDataType, newMinLength, newMaxLength);
    }

    protected boolean isEqual(BinaryDataType binaryDataType, int minLength, int maxLength) {
        return this.m_binaryDataType == binaryDataType && this.m_minLength == minLength && this.m_maxLength == maxLength;
    }

    public int subtractSizeFrom(int argument) {
        if (argument <= 0 || this.m_maxLength == Integer.MAX_VALUE) {
            return 0;
        }
        if (this.m_minLength >= 7 || this.m_maxLength >= 7) {
            return 0;
        }
        long size = this.getNumberOfValuesOfLength(this.m_maxLength) - this.getNumberOfValuesOfLength(this.m_minLength - 1);
        return (int)Math.max((long)argument - size, 0L);
    }

    protected long getNumberOfValuesOfLength(int length) {
        if (length < 0) {
            return 0L;
        }
        long valuesOfLength = 1L;
        long total = 1L;
        for (int i = 1; i <= length; ++i) {
            total += (valuesOfLength *= 256L);
        }
        return total;
    }

    public boolean contains(BinaryData value) {
        return this.m_binaryDataType == value.getBinaryDataType() && this.m_minLength <= value.getNumberOfBytes() && value.getNumberOfBytes() <= this.m_maxLength;
    }

    public void enumerateValues(Collection<Object> values) {
        if (this.m_maxLength == Integer.MAX_VALUE) {
            throw new IllegalStateException("Internal error: the data range is infinite!");
        }
        if (this.m_minLength == 0) {
            values.add(new BinaryData(this.m_binaryDataType, new byte[0]));
        }
        byte[] temp = new byte[this.m_maxLength];
        this.processPosition(temp, values, 0);
    }

    protected void processPosition(byte[] temp, Collection<Object> values, int position) {
        if (position < this.m_maxLength) {
            for (int b = 0; b <= 255; ++b) {
                temp[position] = (byte)b;
                if (this.m_minLength <= position + 1) {
                    byte[] copy = new byte[position + 1];
                    System.arraycopy(temp, 0, copy, 0, copy.length);
                    values.add(new BinaryData(this.m_binaryDataType, copy));
                }
                this.processPosition(temp, values, position + 1);
            }
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.m_binaryDataType.toString());
        buffer.append('[');
        buffer.append(this.m_minLength);
        buffer.append("..");
        if (this.m_maxLength == Integer.MAX_VALUE) {
            buffer.append("+INF");
        } else {
            buffer.append(this.m_maxLength);
        }
        buffer.append(']');
        return buffer.toString();
    }

    protected static boolean isIntervalEmpty(int minLength, int maxLength) {
        return minLength > maxLength;
    }
}

