/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.owlreal;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigRational
extends Number
implements Comparable<BigRational> {
    private static final long serialVersionUID = 3883936594384307950L;
    private final BigInteger m_numerator;
    private final BigInteger m_denominator;

    public BigRational(BigInteger numerator, BigInteger denominator) {
        this.m_numerator = numerator;
        this.m_denominator = denominator;
    }

    public BigInteger getNumerator() {
        return this.m_numerator;
    }

    public BigInteger getDenominator() {
        return this.m_denominator;
    }

    public boolean isFinitelyRepresentable() {
        try {
            new BigDecimal(this.m_numerator).divide(new BigDecimal(this.m_denominator)).doubleValue();
            return true;
        }
        catch (ArithmeticException e) {
            return false;
        }
    }

    @Override
    public int compareTo(BigRational that) {
        return this.m_numerator.multiply(that.m_denominator).compareTo(this.m_denominator.multiply(that.m_numerator));
    }

    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (!(that instanceof BigRational)) {
            return false;
        }
        BigRational thatRational = (BigRational)that;
        return this.m_numerator.equals(thatRational.m_numerator) && this.m_denominator.equals(thatRational.m_denominator);
    }

    public int hashCode() {
        return this.m_numerator.hashCode() * 3 + this.m_denominator.hashCode();
    }

    public String toString() {
        return this.m_numerator.toString() + "/" + this.m_denominator.toString();
    }

    @Override
    public double doubleValue() {
        return this.m_numerator.divide(this.m_denominator).doubleValue();
    }

    @Override
    public float floatValue() {
        return this.m_numerator.divide(this.m_denominator).floatValue();
    }

    @Override
    public int intValue() {
        return this.m_numerator.divide(this.m_denominator).intValue();
    }

    @Override
    public long longValue() {
        return this.m_numerator.divide(this.m_denominator).longValue();
    }
}

