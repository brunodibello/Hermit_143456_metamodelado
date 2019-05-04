/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.semanticweb.HermiT.datalog.DatalogEngine;
import org.semanticweb.HermiT.datalog.QueryResultCollector;
import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.Term;
import org.semanticweb.HermiT.model.Variable;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.ExtensionManager;
import org.semanticweb.HermiT.tableau.ExtensionTable;
import org.semanticweb.HermiT.tableau.HyperresolutionManager;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.UnionDependencySet;

public class ConjunctiveQuery {
    protected final DatalogEngine m_datalogEngine;
    protected final Atom[] m_queryAtoms;
    protected final Term[] m_answerTerms;
    protected final Term[] m_resultBuffer;
    protected final OneEmptyTupleRetrieval m_firstRetrieval;
    protected final QueryResultCollector[] m_queryResultCollector;
    protected final DLClauseEvaluator.Worker[] m_workers;

    public ConjunctiveQuery(DatalogEngine datalogEngine, Atom[] queryAtoms, Term[] answerTerms) {
        if (!datalogEngine.materialize()) {
            throw new IllegalStateException("The supplied DL ontology is unsatisfiable.");
        }
        this.m_datalogEngine = datalogEngine;
        this.m_queryAtoms = queryAtoms;
        this.m_answerTerms = answerTerms;
        this.m_resultBuffer = (Term[])answerTerms.clone();
        this.m_firstRetrieval = new OneEmptyTupleRetrieval();
        this.m_queryResultCollector = new QueryResultCollector[1];
        HyperresolutionManager.BodyAtomsSwapper swapper = new HyperresolutionManager.BodyAtomsSwapper(DLClause.create(new Atom[0], queryAtoms));
        DLClause queryDLClause = swapper.getSwappedDLClause(0);
        QueryCompiler queryCompiler = new QueryCompiler(this, queryDLClause, answerTerms, datalogEngine.m_termsToNodes, datalogEngine.m_nodesToTerms, this.m_resultBuffer, this.m_queryResultCollector, this.m_firstRetrieval);
        this.m_workers = new DLClauseEvaluator.Worker[queryCompiler.m_workers.size()];
        queryCompiler.m_workers.toArray(this.m_workers);
    }

    public DatalogEngine getDatalogEngine() {
        return this.m_datalogEngine;
    }

    public int getNumberOfQUeryAtoms() {
        return this.m_queryAtoms.length;
    }

    public int getNumberOfAnswerTerms() {
        return this.m_answerTerms.length;
    }

    public void evaluate(QueryResultCollector queryResultCollector) {
        try {
            this.m_queryResultCollector[0] = queryResultCollector;
            this.m_firstRetrieval.open();
            int programCounter = 0;
            while (programCounter < this.m_workers.length) {
                programCounter = this.m_workers[programCounter].execute(programCounter);
            }
        }
        finally {
            this.m_queryResultCollector[0] = null;
        }
    }

    protected static final class QueryCompiler
    extends DLClauseEvaluator.ConjunctionCompiler {
        protected final ConjunctiveQuery m_conjunctiveQuery;
        protected final Term[] m_answerTerms;
        protected final Map<Node, Term> m_nodesToTerms;
        protected final Term[] m_resultBuffer;
        protected final QueryResultCollector[] m_queryResultCollector;

        public QueryCompiler(ConjunctiveQuery conjunctiveQuery, DLClause queryDLClause, Term[] answerTerms, Map<Term, Node> termsToNodes, Map<Node, Term> nodesToTerms, Term[] resultBuffer, QueryResultCollector[] queryResultCollector, ExtensionTable.Retrieval oneEmptyTupleRetrieval) {
            super(new DLClauseEvaluator.BufferSupply(), new DLClauseEvaluator.ValuesBufferManager(Collections.singleton(queryDLClause), termsToNodes), null, conjunctiveQuery.m_datalogEngine.m_extensionManager, queryDLClause.getBodyAtoms(), QueryCompiler.getAnswerVariables(answerTerms));
            this.m_conjunctiveQuery = conjunctiveQuery;
            this.m_answerTerms = answerTerms;
            this.m_nodesToTerms = nodesToTerms;
            this.m_resultBuffer = resultBuffer;
            this.m_queryResultCollector = queryResultCollector;
            this.generateCode(0, oneEmptyTupleRetrieval);
        }

        @Override
        protected void compileHeads() {
            ArrayList<int[]> copyAnswers = new ArrayList<int[]>();
            for (int index = 0; index < this.m_answerTerms.length; ++index) {
                Term answerTerm = this.m_answerTerms[index];
                if (!(answerTerm instanceof Variable)) continue;
                int answerVariableIndex = this.m_variables.indexOf(answerTerm);
                copyAnswers.add(new int[]{answerVariableIndex, index});
            }
            this.m_workers.add(new QueryAnswerCallback(this.m_conjunctiveQuery, this.m_nodesToTerms, this.m_resultBuffer, this.m_queryResultCollector, (int[][])copyAnswers.toArray((T[])new int[copyAnswers.size()][]), this.m_valuesBufferManager.m_valuesBuffer));
        }

        protected static List<Variable> getAnswerVariables(Term[] answerTerms) {
            ArrayList<Variable> result = new ArrayList<Variable>();
            for (Term answerTerm : answerTerms) {
                if (!(answerTerm instanceof Variable)) continue;
                result.add((Variable)answerTerm);
            }
            return result;
        }
    }

    public static class QueryAnswerCallback
    implements DLClauseEvaluator.Worker {
        protected final ConjunctiveQuery m_conjunctiveQuery;
        protected final Map<Node, Term> m_nodesToTerms;
        protected final Term[] m_resultBuffer;
        protected final QueryResultCollector[] m_queryResultCollector;
        protected final int[][] m_copyAnswers;
        protected final Object[] m_valuesBuffer;

        public QueryAnswerCallback(ConjunctiveQuery conjunctiveQuery, Map<Node, Term> nodesToTerms, Term[] resultBuffer, QueryResultCollector[] queryResultCollector, int[][] copyAnswers, Object[] valuesBuffer) {
            this.m_conjunctiveQuery = conjunctiveQuery;
            this.m_nodesToTerms = nodesToTerms;
            this.m_resultBuffer = resultBuffer;
            this.m_queryResultCollector = queryResultCollector;
            this.m_copyAnswers = copyAnswers;
            this.m_valuesBuffer = valuesBuffer;
        }

        @Override
        public int execute(int programCounter) {
            for (int copyIndex = this.m_copyAnswers.length - 1; copyIndex >= 0; --copyIndex) {
                this.m_resultBuffer[this.m_copyAnswers[copyIndex][1]] = this.m_nodesToTerms.get(this.m_valuesBuffer[this.m_copyAnswers[copyIndex][0]]);
            }
            this.m_queryResultCollector[0].processResult(this.m_conjunctiveQuery, this.m_resultBuffer);
            return programCounter + 1;
        }

        public String toString() {
            return "Call query consumer";
        }
    }

    protected static final class OneEmptyTupleRetrieval
    implements ExtensionTable.Retrieval {
        protected static final int[] s_noBindings = new int[0];
        protected static final Object[] s_noObjects = new Object[0];
        protected boolean m_afterLast = true;

        @Override
        public ExtensionTable getExtensionTable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExtensionTable.View getExtensionView() {
            return ExtensionTable.View.TOTAL;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int[] getBindingPositions() {
            return s_noBindings;
        }

        @Override
        public Object[] getBindingsBuffer() {
            return s_noObjects;
        }

        @Override
        public Object[] getTupleBuffer() {
            return s_noObjects;
        }

        @Override
        public DependencySet getDependencySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isCore() {
            return false;
        }

        @Override
        public void open() {
            this.m_afterLast = false;
        }

        @Override
        public boolean afterLast() {
            return this.m_afterLast;
        }

        @Override
        public int getCurrentTupleIndex() {
            return this.m_afterLast ? -1 : 0;
        }

        @Override
        public void next() {
            this.m_afterLast = true;
        }
    }

}

