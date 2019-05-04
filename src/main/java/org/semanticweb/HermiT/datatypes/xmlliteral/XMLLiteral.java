/*
 * Decompiled with CFR 0.137.
 * 
 * Could not load the following classes:
 *  org.apache.axiom.c14n.impl.Canonicalizer20010315ExclWithComments
 */
package org.semanticweb.HermiT.datatypes.xmlliteral;

import org.apache.axiom.c14n.impl.Canonicalizer20010315ExclWithComments;

public class XMLLiteral {
    protected final String m_xml;

    public XMLLiteral(String xml) {
        this.m_xml = xml;
    }

    public String getXML() {
        return this.m_xml;
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof XMLLiteral)) {
            return false;
        }
        return ((XMLLiteral)that).m_xml.equals(this.m_xml);
    }

    public int hashCode() {
        return this.m_xml.hashCode();
    }

    public String toString() {
        return this.m_xml;
    }

    public static XMLLiteral parse(String lexicalForm) throws Exception {
        String enclosedXML = "<arbitraryTag>" + lexicalForm + "</arbitraryTag>";
        Canonicalizer20010315ExclWithComments canonicalizer = new Canonicalizer20010315ExclWithComments();
        byte[] result = canonicalizer.engineCanonicalize(enclosedXML.getBytes("UTF-8"));
        String canonicalXML = new String(result, "UTF-8");
        assert (canonicalXML.startsWith("<arbitraryTag>"));
        assert (canonicalXML.endsWith("</arbitraryTag>"));
        canonicalXML = canonicalXML.substring("<arbitraryTag>".length(), canonicalXML.length() - "</arbitraryTag>".length());
        return new XMLLiteral(canonicalXML);
    }
}

