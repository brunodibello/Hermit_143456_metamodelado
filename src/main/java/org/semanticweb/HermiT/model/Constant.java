/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.datatypes.DatatypeRegistry;
import org.semanticweb.HermiT.datatypes.MalformedLiteralException;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.Term;

public class Constant
extends Term {
    private static final long serialVersionUID = -8143911431654640690L;
    protected final String m_lexicalForm;
    protected final String m_datatypeURI;
    protected final Object m_dataValue;
    protected static final InterningManager<Constant> s_interningManager = new InterningManager<Constant>(){

        @Override
        protected boolean equal(Constant object1, Constant object2) {
            return object1.m_lexicalForm.equals(object2.m_lexicalForm) && object1.m_datatypeURI.equals(object2.m_datatypeURI);
        }

        @Override
        protected int getHashCode(Constant object) {
            return object.m_lexicalForm.hashCode() + object.m_datatypeURI.hashCode();
        }
    };

    protected Constant(String lexicalForm, String datatypeURI, Object dataValue) {
        this.m_lexicalForm = lexicalForm;
        this.m_datatypeURI = datatypeURI;
        this.m_dataValue = dataValue;
    }

    public String getLexicalForm() {
        return this.m_lexicalForm;
    }

    public String getDatatypeURI() {
        return this.m_datatypeURI;
    }

    public Object getDataValue() {
        return this.m_dataValue;
    }

    public boolean isAnonymous() {
        return "internal:anonymous-constants".equals(this.m_datatypeURI);
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    @Override
    public String toString(Prefixes prefixes) {
        StringBuffer buffer = new StringBuffer();
        buffer.append('\"');
        block4 : for (int index = 0; index < this.m_lexicalForm.length(); ++index) {
            char c = this.m_lexicalForm.charAt(index);
            switch (c) {
                case '\"': {
                    buffer.append("\\\"");
                    continue block4;
                }
                case '\\': {
                    buffer.append("\\\\");
                    continue block4;
                }
                default: {
                    buffer.append(c);
                }
            }
        }
        buffer.append("\"^^");
        buffer.append(prefixes.abbreviateIRI(this.m_datatypeURI));
        return buffer.toString();
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static Constant create(String lexicalForm, String datatypeURI) throws MalformedLiteralException {
        Object dataValue = DatatypeRegistry.parseLiteral(lexicalForm, datatypeURI);
        return s_interningManager.intern(new Constant(lexicalForm, datatypeURI, dataValue));
    }

    public static Constant createAnonymous(String id) {
        return Constant.create(id, "internal:anonymous-constants");
    }

}

