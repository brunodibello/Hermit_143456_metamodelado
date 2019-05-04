/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes;

import java.util.Set;
import org.semanticweb.HermiT.datatypes.MalformedLiteralException;
import org.semanticweb.HermiT.datatypes.UnsupportedFacetException;
import org.semanticweb.HermiT.datatypes.ValueSpaceSubset;
import org.semanticweb.HermiT.model.DatatypeRestriction;

public interface DatatypeHandler {
    public Set<String> getManagedDatatypeURIs();

    public Object parseLiteral(String var1, String var2) throws MalformedLiteralException;

    public void validateDatatypeRestriction(DatatypeRestriction var1) throws UnsupportedFacetException;

    public ValueSpaceSubset createValueSpaceSubset(DatatypeRestriction var1);

    public ValueSpaceSubset conjoinWithDR(ValueSpaceSubset var1, DatatypeRestriction var2);

    public ValueSpaceSubset conjoinWithDRNegation(ValueSpaceSubset var1, DatatypeRestriction var2);

    public boolean isSubsetOf(String var1, String var2);

    public boolean isDisjointWith(String var1, String var2);
}

