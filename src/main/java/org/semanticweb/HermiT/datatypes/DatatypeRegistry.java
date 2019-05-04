/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.semanticweb.HermiT.datatypes.DatatypeHandler;
import org.semanticweb.HermiT.datatypes.MalformedLiteralException;
import org.semanticweb.HermiT.datatypes.UnsupportedDatatypeException;
import org.semanticweb.HermiT.datatypes.UnsupportedFacetException;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;
import org.semanticweb.HermiT.datatypes.anyuri.AnyURIDatatypeHandler;
import org.semanticweb.HermiT.datatypes.binarydata.BinaryDataDatatypeHandler;
import org.semanticweb.HermiT.datatypes.bool.BooleanDatatypeHandler;
import org.semanticweb.HermiT.datatypes.datetime.DateTimeDatatypeHandler;
import org.semanticweb.HermiT.datatypes.doublenum.DoubleDatatypeHandler;
import org.semanticweb.HermiT.datatypes.floatnum.FloatDatatypeHandler;
import org.semanticweb.HermiT.datatypes.owlreal.OWLRealDatatypeHandler;
import org.semanticweb.HermiT.datatypes.rdfplainliteral.RDFPlainLiteralDatatypeHandler;
import org.semanticweb.HermiT.datatypes.xmlliteral.XMLLiteralDatatypeHandler;
import org.semanticweb.HermiT.model.DatatypeRestriction;

public class DatatypeRegistry {
    protected static final Map<String, DatatypeHandler> s_handlersByDatatypeURI = new HashMap<String, DatatypeHandler>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void registerDatatypeHandler(DatatypeHandler datatypeHandler) {
        Map<String, DatatypeHandler> map = s_handlersByDatatypeURI;
        synchronized (map) {
            for (String datatypeURI : datatypeHandler.getManagedDatatypeURIs()) {
                if (!s_handlersByDatatypeURI.containsKey(datatypeURI)) continue;
                throw new IllegalArgumentException("Datatype handler for datatype '" + datatypeURI + "' has already been registed.");
            }
            for (String datatypeURI : datatypeHandler.getManagedDatatypeURIs()) {
                s_handlersByDatatypeURI.put(datatypeURI, datatypeHandler);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected static DatatypeHandler getDatatypeHandlerFor(String datatypeURI) throws UnsupportedDatatypeException {
        DatatypeHandler datatypeHandler;
        Map<String, DatatypeHandler> map = s_handlersByDatatypeURI;
        synchronized (map) {
            datatypeHandler = s_handlersByDatatypeURI.get(datatypeURI);
        }
        if (datatypeHandler == null) {
            String CRLF = System.getProperty("line.separator");
            String message = "HermiT supports all and only the datatypes of the OWL 2 datatype map, see " + CRLF + "http://www.w3.org/TR/owl2-syntax/#Datatype_Maps. " + CRLF + "The datatype '" + datatypeURI + "' is not part of the OWL 2 datatype map and " + CRLF + "no custom datatype definition is given; " + CRLF + "therefore, HermiT cannot handle this datatype.";
            throw new UnsupportedDatatypeException(message);
        }
        return datatypeHandler;
    }

    protected static DatatypeHandler getDatatypeHandlerFor(DatatypeRestriction datatypeRestriction) throws UnsupportedDatatypeException {
        return DatatypeRegistry.getDatatypeHandlerFor(datatypeRestriction.getDatatypeURI());
    }

    public static Object parseLiteral(String lexicalForm, String datatypeURI) throws MalformedLiteralException, UnsupportedDatatypeException {
        DatatypeHandler handler;
        try {
            handler = DatatypeRegistry.getDatatypeHandlerFor(datatypeURI);
        }
        catch (UnsupportedDatatypeException e) {
            String CRLF = System.getProperty("line.separator");
            String message = "Literals can only use the datatypes from the OWL 2 datatype map, see " + CRLF + "http://www.w3.org/TR/owl2-syntax/#Datatype_Maps. " + CRLF + "The datatype '" + datatypeURI + "' is not part of the OWL 2 datatype map and " + CRLF + "HermiT cannot parse this literal.";
            throw new UnsupportedDatatypeException(message, e);
        }
        return handler.parseLiteral(lexicalForm, datatypeURI);
    }

    public static void validateDatatypeRestriction(DatatypeRestriction datatypeRestriction) throws UnsupportedDatatypeException, UnsupportedFacetException {
        DatatypeRegistry.getDatatypeHandlerFor(datatypeRestriction).validateDatatypeRestriction(datatypeRestriction);
    }

    public static ValueSpaceSubset createValueSpaceSubset(DatatypeRestriction datatypeRestriction) {
        return DatatypeRegistry.getDatatypeHandlerFor(datatypeRestriction).createValueSpaceSubset(datatypeRestriction);
    }

    public static ValueSpaceSubset conjoinWithDR(ValueSpaceSubset valueSpaceSubset, DatatypeRestriction datatypeRestriction) {
        return DatatypeRegistry.getDatatypeHandlerFor(datatypeRestriction).conjoinWithDR(valueSpaceSubset, datatypeRestriction);
    }

    public static ValueSpaceSubset conjoinWithDRNegation(ValueSpaceSubset valueSpaceSubset, DatatypeRestriction datatypeRestriction) {
        return DatatypeRegistry.getDatatypeHandlerFor(datatypeRestriction).conjoinWithDRNegation(valueSpaceSubset, datatypeRestriction);
    }

    public static boolean isSubsetOf(String subsetDatatypeURI, String supersetDatatypeURI) {
        DatatypeHandler datatypeHandler = DatatypeRegistry.getDatatypeHandlerFor(subsetDatatypeURI);
        if (datatypeHandler.getManagedDatatypeURIs().contains(supersetDatatypeURI)) {
            return datatypeHandler.isSubsetOf(subsetDatatypeURI, supersetDatatypeURI);
        }
        return false;
    }

    public static boolean isDisjointWith(String datatypeURI1, String datatypeURI2) {
        DatatypeHandler datatypeHandler = DatatypeRegistry.getDatatypeHandlerFor(datatypeURI1);
        if (datatypeHandler.getManagedDatatypeURIs().contains(datatypeURI2)) {
            return datatypeHandler.isDisjointWith(datatypeURI1, datatypeURI2);
        }
        return true;
    }

    static {
        DatatypeRegistry.registerDatatypeHandler(new AnonymousConstantsDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new BooleanDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new RDFPlainLiteralDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new OWLRealDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new DoubleDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new FloatDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new DateTimeDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new BinaryDataDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new AnyURIDatatypeHandler());
        DatatypeRegistry.registerDatatypeHandler(new XMLLiteralDatatypeHandler());
    }

    public static class AnonymousConstantValue {
        protected final String m_name;

        public AnonymousConstantValue(String name) {
            this.m_name = name;
        }

        public String getName() {
            return this.m_name;
        }

        public int hashCode() {
            return this.m_name.hashCode();
        }

        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }
            if (!(that instanceof AnonymousConstantValue)) {
                return false;
            }
            return ((AnonymousConstantValue)that).m_name.equals(this.m_name);
        }

        public static AnonymousConstantValue create(String name) {
            return new AnonymousConstantValue(name);
        }
    }

    protected static class AnonymousConstantsDatatypeHandler
    implements DatatypeHandler {
        protected static final String ANONYMOUS_CONSTANTS = "internal:anonymous-constants";
        protected static final Set<String> s_managedDatatypeURIs = Collections.singleton("internal:anonymous-constants");

        protected AnonymousConstantsDatatypeHandler() {
        }

        @Override
        public Set<String> getManagedDatatypeURIs() {
            return s_managedDatatypeURIs;
        }

        @Override
        public Object parseLiteral(String lexicalForm, String datatypeURI) throws MalformedLiteralException {
            assert (ANONYMOUS_CONSTANTS.equals(datatypeURI));
            return new AnonymousConstantValue(lexicalForm.trim());
        }

        @Override
        public void validateDatatypeRestriction(DatatypeRestriction datatypeRestriction) throws UnsupportedFacetException {
            throw new IllegalStateException("Internal error: anonymous constants datatype should not occur in datatype restrictions.");
        }

        @Override
        public ValueSpaceSubset createValueSpaceSubset(DatatypeRestriction datatypeRestriction) {
            throw new IllegalStateException("Internal error: anonymous constants datatype should not occur in datatype restrictions.");
        }

        @Override
        public ValueSpaceSubset conjoinWithDR(ValueSpaceSubset valueSpaceSubset, DatatypeRestriction datatypeRestriction) {
            throw new IllegalStateException("Internal error: anonymous constants datatype should not occur in datatype restrictions.");
        }

        @Override
        public ValueSpaceSubset conjoinWithDRNegation(ValueSpaceSubset valueSpaceSubset, DatatypeRestriction datatypeRestriction) {
            throw new IllegalStateException("Internal error: anonymous constants datatype should not occur in datatype restrictions.");
        }

        @Override
        public boolean isSubsetOf(String subsetDatatypeURI, String supersetDatatypeURI) {
            throw new IllegalStateException("Internal error: anonymous constants datatype should not occur in datatype restrictions.");
        }

        @Override
        public boolean isDisjointWith(String datatypeURI1, String datatypeURI2) {
            throw new IllegalStateException("Internal error: anonymous constants datatype should not occur in datatype restrictions.");
        }
    }

}

