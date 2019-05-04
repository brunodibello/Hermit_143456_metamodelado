/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.rdfplainliteral;

public class RDFPlainLiteralDataValue {
    protected final String m_string;
    protected final String m_languageTag;

    public RDFPlainLiteralDataValue(String string, String languageTag) {
        this.m_string = string;
        this.m_languageTag = languageTag;
    }

    public String getString() {
        return this.m_string;
    }

    public String getLanguageTag() {
        return this.m_languageTag;
    }

    public int hashCode() {
        return this.m_string.hashCode() * 3 + this.m_languageTag.hashCode();
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof RDFPlainLiteralDataValue)) {
            return false;
        }
        RDFPlainLiteralDataValue thatValue = (RDFPlainLiteralDataValue)that;
        return thatValue.m_string.equals(this.m_string) && thatValue.m_languageTag.equals(this.m_languageTag);
    }

    public String toString() {
        return '\"' + this.m_string + "\"@" + this.m_languageTag;
    }
}

