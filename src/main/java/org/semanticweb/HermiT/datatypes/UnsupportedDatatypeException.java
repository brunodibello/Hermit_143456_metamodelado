package org.semanticweb.HermiT.datatypes;

public class UnsupportedDatatypeException
extends RuntimeException {
    public UnsupportedDatatypeException(String message) {
        super(message);
    }

    public UnsupportedDatatypeException(String message, Throwable exception) {
        super(message, exception);
    }
}

