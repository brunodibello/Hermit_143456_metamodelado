/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes;

public class MalformedLiteralException
extends RuntimeException {
    public MalformedLiteralException(String lexicalForm, String datatypeURI) {
        this(lexicalForm, datatypeURI, null);
    }

    public MalformedLiteralException(String lexicalForm, String datatypeURI, Throwable cause) {
        super("Literal \"" + lexicalForm + "\"^^<" + datatypeURI + "> is malformed", cause);
    }
}

