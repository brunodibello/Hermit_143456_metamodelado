/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.model.AnnotatedEquality;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.model.Individual;
import org.semanticweb.HermiT.model.Inequality;
import org.semanticweb.HermiT.model.InterningManager;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.model.NodeIDLessEqualThan;
import org.semanticweb.HermiT.model.Role;
import org.semanticweb.HermiT.model.Term;
import org.semanticweb.HermiT.model.Variable;

public class Atom
implements Serializable {
    private static final long serialVersionUID = 7884900540178779422L;
    public static final Set<DLPredicate> s_infixPredicates = new HashSet<DLPredicate>();
    protected final DLPredicate m_dlPredicate;
    protected final Term[] m_arguments;
    protected static final InterningManager<Atom> s_interningManager;

    protected Atom(DLPredicate dlPredicate, Term[] arguments) {
        this.m_dlPredicate = dlPredicate;
        this.m_arguments = arguments;
        if (this.m_dlPredicate.getArity() != this.m_arguments.length) {
            throw new IllegalArgumentException("The arity of the predicate must be equal to the number of arguments.");
        }
    }

    public DLPredicate getDLPredicate() {
        return this.m_dlPredicate;
    }

    public int getArity() {
        return this.m_arguments.length;
    }

    public Term getArgument(int argumentIndex) {
        return this.m_arguments[argumentIndex];
    }

    public Variable getArgumentVariable(int argumentIndex) {
        if (this.m_arguments[argumentIndex] instanceof Variable) {
            return (Variable)this.m_arguments[argumentIndex];
        }
        return null;
    }

    public void getVariables(Set<Variable> variables) {
        for (int argumentIndex = this.m_arguments.length - 1; argumentIndex >= 0; --argumentIndex) {
            Term argument = this.m_arguments[argumentIndex];
            if (!(argument instanceof Variable)) continue;
            variables.add((Variable)argument);
        }
    }

    public void getIndividuals(Set<Individual> individuals) {
        for (int argumentIndex = this.m_arguments.length - 1; argumentIndex >= 0; --argumentIndex) {
            Term argument = this.m_arguments[argumentIndex];
            if (!(argument instanceof Individual)) continue;
            individuals.add((Individual)argument);
        }
    }

    public boolean containsVariable(Variable variable) {
        for (int argumentIndex = this.m_arguments.length - 1; argumentIndex >= 0; --argumentIndex) {
            if (!this.m_arguments[argumentIndex].equals(variable)) continue;
            return true;
        }
        return false;
    }

    public String toString(Prefixes prefixes) {
        StringBuffer buffer = new StringBuffer();
        if (s_infixPredicates.contains(this.m_dlPredicate)) {
            buffer.append(this.m_arguments[0].toString(prefixes));
            buffer.append(' ');
            buffer.append(this.m_dlPredicate.toString(prefixes));
            buffer.append(' ');
            buffer.append(this.m_arguments[1].toString(prefixes));
        } else if (this.m_dlPredicate instanceof AnnotatedEquality) {
            AnnotatedEquality annotatedEquality = (AnnotatedEquality)this.m_dlPredicate;
            buffer.append('[');
            buffer.append(this.m_arguments[0].toString(prefixes));
            buffer.append(' ');
            buffer.append("==");
            buffer.append(' ');
            buffer.append(this.m_arguments[1].toString(prefixes));
            buffer.append("]@atMost(");
            buffer.append(annotatedEquality.getCaridnality());
            buffer.append(' ');
            buffer.append(annotatedEquality.getOnRole().toString(prefixes));
            buffer.append(' ');
            buffer.append(annotatedEquality.getToConcept().toString(prefixes));
            buffer.append(")(");
            buffer.append(this.m_arguments[2].toString(prefixes));
            buffer.append(')');
        } else {
            buffer.append(this.m_dlPredicate.toString(prefixes));
            buffer.append('(');
            for (int i = 0; i < this.m_arguments.length; ++i) {
                if (i != 0) {
                    buffer.append(',');
                }
                buffer.append(this.m_arguments[i].toString(prefixes));
            }
            buffer.append(')');
        }
        return buffer.toString();
    }

    public String toString() {
        return this.toString(Prefixes.STANDARD_PREFIXES);
    }

    protected Object readResolve() {
        return s_interningManager.intern(this);
    }

    public static /* varargs */ Atom create(DLPredicate dlPredicate, Term ... arguments) {
        return s_interningManager.intern(new Atom(dlPredicate, arguments));
    }

    static {
        s_infixPredicates.add(Equality.INSTANCE);
        s_infixPredicates.add(Inequality.INSTANCE);
        s_infixPredicates.add(NodeIDLessEqualThan.INSTANCE);
        s_interningManager = new InterningManager<Atom>(){

            @Override
            protected boolean equal(Atom object1, Atom object2) {
                if (object1.m_dlPredicate != object2.m_dlPredicate) {
                    return false;
                }
                for (int index = object1.m_arguments.length - 1; index >= 0; --index) {
                    if (object1.m_arguments[index] == object2.m_arguments[index]) continue;
                    return false;
                }
                return true;
            }

            @Override
            protected int getHashCode(Atom object) {
                int hashCode = object.m_dlPredicate.hashCode();
                for (int index = object.m_arguments.length - 1; index >= 0; --index) {
                    hashCode += object.m_arguments[index].hashCode();
                }
                return hashCode;
            }
        };
    }

}

