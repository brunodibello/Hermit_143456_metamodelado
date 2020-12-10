/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fing.metamodelling.MetamodellingAxiomHelper;
import org.semanticweb.HermiT.model.AnnotatedEquality;
import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.DLOntology;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.DataRange;
import org.semanticweb.HermiT.model.DescriptionGraph;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.model.Inequality;
import org.semanticweb.HermiT.model.InternalDatatype;
import org.semanticweb.HermiT.model.InverseRole;
import org.semanticweb.HermiT.model.Role;
import org.semanticweb.HermiT.monitor.TableauMonitor;
import org.semanticweb.HermiT.structural.OWLNormalization;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator.GroundDisjunctionHeaderManager;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.DependencySetFactory;
import org.semanticweb.HermiT.tableau.ExtensionTable;
import org.semanticweb.HermiT.tableau.ExtensionTableWithFullIndex;
import org.semanticweb.HermiT.tableau.ExtensionTableWithTupleIndexes;
import org.semanticweb.HermiT.tableau.MergingManager;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.NodeType;
import org.semanticweb.HermiT.tableau.NominalIntroductionManager;
import org.semanticweb.HermiT.tableau.PermanentDependencySet;
import org.semanticweb.HermiT.tableau.Tableau;
import org.semanticweb.HermiT.tableau.TupleIndex;
import org.semanticweb.HermiT.tableau.TupleTable;
import org.semanticweb.owlapi.model.OWLClassExpression;

public final class ExtensionManager
implements Serializable {
    private static final long serialVersionUID = 5900300914631070591L;
    protected final Tableau m_tableau;
    protected final TableauMonitor m_tableauMonitor;
    protected final DependencySetFactory m_dependencySetFactory;
    protected final Map<Integer, ExtensionTable> m_extensionTablesByArity;
    protected final ExtensionTable[] m_allExtensionTablesArray;
    protected final ExtensionTable m_binaryExtensionTable;
    protected final ExtensionTable m_ternaryExtensionTable;
    protected final Object[] m_binaryAuxiliaryTupleContains;
    protected final Object[] m_binaryAuxiliaryTupleAdd;
    protected final Object[] m_ternaryAuxiliaryTupleContains;
    protected final Object[] m_ternaryAuxiliaryTupleAdd;
    protected final Object[] m_fouraryAuxiliaryTupleContains;
    protected final Object[] m_fouraryAuxiliaryTupleAdd;
    protected PermanentDependencySet m_clashDependencySet;
    protected boolean m_addActive;

    public ExtensionManager(Tableau tableau) {
        this.m_tableau = tableau;
        this.m_tableauMonitor = this.m_tableau.m_tableauMonitor;
        this.m_dependencySetFactory = this.m_tableau.m_dependencySetFactory;
        this.m_extensionTablesByArity = new HashMap<Integer, ExtensionTable>();
        this.m_binaryExtensionTable = new ExtensionTableWithTupleIndexes(this.m_tableau, 2, !this.m_tableau.isDeterministic(), new TupleIndex[]{new TupleIndex(new int[]{1, 0}), new TupleIndex(new int[]{0, 1})}){
            private static final long serialVersionUID = 1462821385000191875L;

            @Override
            public boolean isTupleActive(Object[] tuple) {
                return ((Node)tuple[1]).isActive();
            }

            @Override
            public boolean isTupleActive(int tupleIndex) {
                return ((Node)this.m_tupleTable.getTupleObject(tupleIndex, 1)).isActive();
            }
        };
        this.m_extensionTablesByArity.put(new Integer(2), this.m_binaryExtensionTable);
        this.m_ternaryExtensionTable = new ExtensionTableWithTupleIndexes(this.m_tableau, 3, !this.m_tableau.isDeterministic(), new TupleIndex[]{new TupleIndex(new int[]{0, 1, 2}), new TupleIndex(new int[]{1, 2, 0}), new TupleIndex(new int[]{2, 0, 1})}){
            private static final long serialVersionUID = -731201626401421877L;

            @Override
            public boolean isTupleActive(Object[] tuple) {
                return ((Node)tuple[1]).isActive() && ((Node)tuple[2]).isActive();
            }

            @Override
            public boolean isTupleActive(int tupleIndex) {
                return ((Node)this.m_tupleTable.getTupleObject(tupleIndex, 1)).isActive() && ((Node)this.m_tupleTable.getTupleObject(tupleIndex, 2)).isActive();
            }
        };
        this.m_extensionTablesByArity.put(new Integer(3), this.m_ternaryExtensionTable);
        for (DescriptionGraph descriptionGraph : this.m_tableau.m_permanentDLOntology.getAllDescriptionGraphs()) {
            Integer arityInteger = descriptionGraph.getNumberOfVertices() + 1;
            if (this.m_extensionTablesByArity.containsKey(arityInteger)) continue;
            this.m_extensionTablesByArity.put(arityInteger, new ExtensionTableWithFullIndex(this.m_tableau, descriptionGraph.getNumberOfVertices() + 1, !this.m_tableau.isDeterministic()));
        }
        this.m_allExtensionTablesArray = new ExtensionTable[this.m_extensionTablesByArity.size()];
        this.m_extensionTablesByArity.values().toArray(this.m_allExtensionTablesArray);
        this.m_binaryAuxiliaryTupleContains = new Object[2];
        this.m_binaryAuxiliaryTupleAdd = new Object[2];
        this.m_ternaryAuxiliaryTupleContains = new Object[3];
        this.m_ternaryAuxiliaryTupleAdd = new Object[3];
        this.m_fouraryAuxiliaryTupleContains = new Object[4];
        this.m_fouraryAuxiliaryTupleAdd = new Object[4];
    }

    public void clear() {
        for (int index = this.m_allExtensionTablesArray.length - 1; index >= 0; --index) {
            this.m_allExtensionTablesArray[index].clear();
        }
        this.m_clashDependencySet = null;
        this.m_binaryAuxiliaryTupleContains[0] = null;
        this.m_binaryAuxiliaryTupleContains[1] = null;
        this.m_binaryAuxiliaryTupleAdd[0] = null;
        this.m_binaryAuxiliaryTupleAdd[1] = null;
        this.m_ternaryAuxiliaryTupleContains[0] = null;
        this.m_ternaryAuxiliaryTupleContains[1] = null;
        this.m_ternaryAuxiliaryTupleContains[2] = null;
        this.m_ternaryAuxiliaryTupleAdd[0] = null;
        this.m_ternaryAuxiliaryTupleAdd[1] = null;
        this.m_ternaryAuxiliaryTupleAdd[2] = null;
        this.m_fouraryAuxiliaryTupleContains[0] = null;
        this.m_fouraryAuxiliaryTupleContains[1] = null;
        this.m_fouraryAuxiliaryTupleContains[2] = null;
        this.m_fouraryAuxiliaryTupleContains[3] = null;
        this.m_fouraryAuxiliaryTupleAdd[0] = null;
        this.m_fouraryAuxiliaryTupleAdd[1] = null;
        this.m_fouraryAuxiliaryTupleAdd[2] = null;
        this.m_fouraryAuxiliaryTupleAdd[3] = null;
    }

    public void branchingPointPushed() {
        for (int index = this.m_allExtensionTablesArray.length - 1; index >= 0; --index) {
            this.m_allExtensionTablesArray[index].branchingPointPushed();
        }
    }

    public void backtrack() {
        for (int index = this.m_allExtensionTablesArray.length - 1; index >= 0; --index) {
            this.m_allExtensionTablesArray[index].backtrack();
        }
    }

    public ExtensionTable getBinaryExtensionTable() {
        return this.m_binaryExtensionTable;
    }

    public ExtensionTable getTernaryExtensionTable() {
        return this.m_ternaryExtensionTable;
    }

    public ExtensionTable getExtensionTable(int arity) {
        switch (arity) {
            case 2: {
                return this.m_binaryExtensionTable;
            }
            case 3: {
                return this.m_ternaryExtensionTable;
            }
        }
        return this.m_extensionTablesByArity.get(arity);
    }

    public Collection<ExtensionTable> getExtensionTables() {
        return this.m_extensionTablesByArity.values();
    }

    public boolean propagateDeltaNew() {
        boolean hasChange = false;
        //System.out.println(" => propagateDeltaNew for "+this.m_allExtensionTablesArray.length+" extension tables <=");
        for (int index = 0; index < this.m_allExtensionTablesArray.length; ++index) {
        	//System.out.println(" Table "+index);
            if (!this.m_allExtensionTablesArray[index].propagateDeltaNew()) continue;
            hasChange = true;
        }
        return hasChange;
    }
    
    public boolean checkDeltaNewPropagation() {
    	boolean hasChange = false;
        for (int index = 0; index < this.m_allExtensionTablesArray.length; ++index) {
            if (!this.m_allExtensionTablesArray[index].checkDeltaNewPropagation()) continue;
            hasChange = true;
        }
        return hasChange;
    }
    
    public void resetDeltaNew() {
    	for (int index = 0; index < this.m_allExtensionTablesArray.length; ++index) {
            this.m_allExtensionTablesArray[index].resetDeltaNew();
        }
    }

    public void clearClash() {
        if (this.m_clashDependencySet != null) {
            this.m_dependencySetFactory.removeUsage(this.m_clashDependencySet);
            this.m_clashDependencySet = null;
        }
    }

    public void setClash(DependencySet clashDependencySet) {
        if (this.m_clashDependencySet != null) {
            this.m_dependencySetFactory.removeUsage(this.m_clashDependencySet);
        }
        this.m_clashDependencySet = this.m_dependencySetFactory.getPermanent(clashDependencySet);
        if (this.m_clashDependencySet != null) {
            this.m_dependencySetFactory.addUsage(this.m_clashDependencySet);
        }
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.clashDetected();
        }
    }

    public DependencySet getClashDependencySet() {
        return this.m_clashDependencySet;
    }

    public boolean containsClash() {
        return this.m_clashDependencySet != null;
    }

    public boolean containsConceptAssertion(Concept concept, Node node) {
        if (node.getNodeType().isAbstract() && AtomicConcept.THING.equals(concept)) {
            return true;
        }
        this.m_binaryAuxiliaryTupleContains[0] = concept;
        this.m_binaryAuxiliaryTupleContains[1] = node;
        return this.m_binaryExtensionTable.containsTuple(this.m_binaryAuxiliaryTupleContains);
    }

    public boolean containsDataRangeAssertion(DataRange range, Node node) {
        if (!node.getNodeType().isAbstract() && InternalDatatype.RDFS_LITERAL.equals(range)) {
            return true;
        }
        this.m_binaryAuxiliaryTupleContains[0] = range;
        this.m_binaryAuxiliaryTupleContains[1] = node;
        return this.m_binaryExtensionTable.containsTuple(this.m_binaryAuxiliaryTupleContains);
    }

    public boolean containsRoleAssertion(Role role, Node nodeFrom, Node nodeTo) {
        if (role instanceof AtomicRole) {
            this.m_ternaryAuxiliaryTupleContains[0] = role;
            this.m_ternaryAuxiliaryTupleContains[1] = nodeFrom;
            this.m_ternaryAuxiliaryTupleContains[2] = nodeTo;
        } else {
            this.m_ternaryAuxiliaryTupleContains[0] = ((InverseRole)role).getInverseOf();
            this.m_ternaryAuxiliaryTupleContains[1] = nodeTo;
            this.m_ternaryAuxiliaryTupleContains[2] = nodeFrom;
        }
        return this.m_ternaryExtensionTable.containsTuple(this.m_ternaryAuxiliaryTupleContains);
    }

    public boolean containsAssertion(DLPredicate dlPredicate, Node node) {
        if (AtomicConcept.THING.equals(dlPredicate)) {
            return true;
        }
        this.m_binaryAuxiliaryTupleContains[0] = dlPredicate;
        this.m_binaryAuxiliaryTupleContains[1] = node;
        return this.m_binaryExtensionTable.containsTuple(this.m_binaryAuxiliaryTupleContains);
    }

    public boolean containsAssertion(DLPredicate dlPredicate, Node node0, Node node1) {
        if (Equality.INSTANCE.equals(dlPredicate)) {
            return node0 == node1;
        }
        this.m_ternaryAuxiliaryTupleContains[0] = dlPredicate;
        this.m_ternaryAuxiliaryTupleContains[1] = node0;
        this.m_ternaryAuxiliaryTupleContains[2] = node1;
        return this.m_ternaryExtensionTable.containsTuple(this.m_ternaryAuxiliaryTupleContains);
    }

    public boolean containsAssertion(DLPredicate dlPredicate, Node node0, Node node1, Node node2) {
        this.m_fouraryAuxiliaryTupleContains[0] = dlPredicate;
        this.m_fouraryAuxiliaryTupleContains[1] = node0;
        this.m_fouraryAuxiliaryTupleContains[2] = node1;
        this.m_fouraryAuxiliaryTupleContains[3] = node2;
        return this.containsTuple(this.m_fouraryAuxiliaryTupleContains);
    }

    public static boolean containsAnnotatedEquality(Node node0, Node node1, Node node2) {
        return NominalIntroductionManager.canForgetAnnotation(node0, node1, node2) && node0 == node1;
    }

    public boolean containsTuple(Object[] tuple) {
        if (tuple.length == 0) {
            return this.containsClash();
        }
        if (AtomicConcept.THING.equals(tuple[0])) {
            return true;
        }
        if (Equality.INSTANCE.equals(tuple[0])) {
            return tuple[1] == tuple[2];
        }
        if (tuple[0] instanceof AnnotatedEquality) {
            return NominalIntroductionManager.canForgetAnnotation((Node)tuple[1], (Node)tuple[2], (Node)tuple[3]) && tuple[1] == tuple[2];
        }
        return this.getExtensionTable(tuple.length).containsTuple(tuple);
    }

    public DependencySet getConceptAssertionDependencySet(Concept concept, Node node) {
        if (AtomicConcept.THING.equals(concept)) {
            return this.m_dependencySetFactory.emptySet();
        }
        this.m_binaryAuxiliaryTupleContains[0] = concept;
        this.m_binaryAuxiliaryTupleContains[1] = node;
        return this.m_binaryExtensionTable.getDependencySet(this.m_binaryAuxiliaryTupleContains);
    }

    public DependencySet getDataRangeAssertionDependencySet(DataRange range, Node node) {
        if (InternalDatatype.RDFS_LITERAL.equals(range)) {
            return this.m_dependencySetFactory.emptySet();
        }
        this.m_binaryAuxiliaryTupleContains[0] = range;
        this.m_binaryAuxiliaryTupleContains[1] = node;
        return this.m_binaryExtensionTable.getDependencySet(this.m_binaryAuxiliaryTupleContains);
    }

    public DependencySet getRoleAssertionDependencySet(Role role, Node nodeFrom, Node nodeTo) {
        if (role instanceof AtomicRole) {
            this.m_ternaryAuxiliaryTupleContains[0] = role;
            this.m_ternaryAuxiliaryTupleContains[1] = nodeFrom;
            this.m_ternaryAuxiliaryTupleContains[2] = nodeTo;
        } else {
            this.m_ternaryAuxiliaryTupleContains[0] = ((InverseRole)role).getInverseOf();
            this.m_ternaryAuxiliaryTupleContains[1] = nodeTo;
            this.m_ternaryAuxiliaryTupleContains[2] = nodeFrom;
        }
        return this.m_ternaryExtensionTable.getDependencySet(this.m_ternaryAuxiliaryTupleContains);
    }

    public DependencySet getAssertionDependencySet(DLPredicate dlPredicate, Node node) {
        this.m_binaryAuxiliaryTupleContains[0] = dlPredicate;
        this.m_binaryAuxiliaryTupleContains[1] = node;
        return this.m_binaryExtensionTable.getDependencySet(this.m_binaryAuxiliaryTupleContains);
    }

    public DependencySet getAssertionDependencySet(DLPredicate dlPredicate, Node node0, Node node1) {
        if (Equality.INSTANCE.equals(dlPredicate)) {
            return node0 == node1 ? this.m_dependencySetFactory.emptySet() : null;
        }
        this.m_ternaryAuxiliaryTupleContains[0] = dlPredicate;
        this.m_ternaryAuxiliaryTupleContains[1] = node0;
        this.m_ternaryAuxiliaryTupleContains[2] = node1;
        return this.m_ternaryExtensionTable.getDependencySet(this.m_ternaryAuxiliaryTupleContains);
    }

    public DependencySet getAssertionDependencySet(DLPredicate dlPredicate, Node node0, Node node1, Node node2) {
        this.m_fouraryAuxiliaryTupleContains[0] = dlPredicate;
        this.m_fouraryAuxiliaryTupleContains[1] = node0;
        this.m_fouraryAuxiliaryTupleContains[2] = node1;
        this.m_fouraryAuxiliaryTupleContains[3] = node2;
        return this.getTupleDependencySet(this.m_fouraryAuxiliaryTupleContains);
    }

    public DependencySet getTupleDependencySet(Object[] tuple) {
        if (tuple.length == 0) {
            return this.m_clashDependencySet;
        }
        return this.getExtensionTable(tuple.length).getDependencySet(tuple);
    }

    public boolean isCore(Object[] tuple) {
        if (tuple.length == 0) {
            return true;
        }
        return this.getExtensionTable(tuple.length).isCore(tuple);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addConceptAssertion(Concept concept, Node node, DependencySet dependencySet, boolean isCore) {
    	System.out.println("[!] Se agrega ConceptAssertion a la binaryExtensionTable");
        if (this.m_addActive) {
            throw new IllegalStateException("ExtensionManager is not reentrant.");
        }
        this.m_addActive = true;
        try {
            this.m_binaryAuxiliaryTupleAdd[0] = concept;
            this.m_binaryAuxiliaryTupleAdd[1] = node;
            System.out.println("	Conecept -> "+concept);
            System.out.println("	Node -> "+node);
            boolean bl = this.m_binaryExtensionTable.addTuple(this.m_binaryAuxiliaryTupleAdd, dependencySet, isCore);
            return bl;
        }
        finally {
            this.m_addActive = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addDataRangeAssertion(DataRange dataRange, Node node, DependencySet dependencySet, boolean isCore) {
        if (this.m_addActive) {
            throw new IllegalStateException("ExtensionManager is not reentrant.");
        }
        this.m_addActive = true;
        try {
            this.m_binaryAuxiliaryTupleAdd[0] = dataRange;
            this.m_binaryAuxiliaryTupleAdd[1] = node;
            System.out.println("[!] Se agrega DataRangeAssertion a la binaryExtensionTable");
            System.out.println("	DataRange -> "+dataRange);
            System.out.println("	Node -> "+node);
            boolean bl = this.m_binaryExtensionTable.addTuple(this.m_binaryAuxiliaryTupleAdd, dependencySet, isCore);
            return bl;
        }
        finally {
            this.m_addActive = false;
        }
    }

    public boolean addRoleAssertion(Role role, Node nodeFrom, Node nodeTo, DependencySet dependencySet, boolean isCore) {
    	System.out.println("[!] Se agrega RoleAssertion");
        if (role instanceof AtomicRole) {
        	System.out.println("	Role -> "+(AtomicRole)role);
            System.out.println("	nodeFrom -> "+nodeFrom);
            System.out.println("	nodeFrom -> "+nodeTo);
            return this.addAssertion((AtomicRole)role, nodeFrom, nodeTo, dependencySet, isCore);
        }
        System.out.println("	Role -> "+((InverseRole)role).getInverseOf());
        System.out.println("	nodeFrom -> "+nodeFrom);
        System.out.println("	nodeFrom -> "+nodeTo);
        return this.addAssertion(((InverseRole)role).getInverseOf(), nodeTo, nodeFrom, dependencySet, isCore);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addAssertion(DLPredicate dlPredicate, Node node, DependencySet dependencySet, boolean isCore) {
        if (this.m_addActive) {
            throw new IllegalStateException("ExtensionManager is not reentrant.");
        }
        this.m_addActive = true;
        try {
            this.m_binaryAuxiliaryTupleAdd[0] = dlPredicate;
            this.m_binaryAuxiliaryTupleAdd[1] = node;
//            System.out.println("[!] Se agrega Assertion a la binaryExtensionTable");
//            System.out.println("	dlPredicate -> "+dlPredicate);
//            System.out.println("	Node -> "+node);
            boolean bl = this.m_binaryExtensionTable.addTuple(this.m_binaryAuxiliaryTupleAdd, dependencySet, isCore);
            return bl;
        }
        finally {
            this.m_addActive = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addAssertion(DLPredicate dlPredicate, Node node0, Node node1, DependencySet dependencySet, boolean isCore) {
        if (Equality.INSTANCE.equals(dlPredicate)) {
            return this.m_tableau.m_mergingManager.mergeNodes(node0, node1, dependencySet);
        }
        if (this.m_addActive) {
            throw new IllegalStateException("ExtensionManager is not reentrant.");
        }
        this.m_addActive = true;
        try {
            this.m_ternaryAuxiliaryTupleAdd[0] = dlPredicate;
            this.m_ternaryAuxiliaryTupleAdd[1] = node0;
            this.m_ternaryAuxiliaryTupleAdd[2] = node1;
//            System.out.println("[!] Se agrega Assertion a la m_ternaryExtensionTable");
//            System.out.println("	dlPredicate -> "+dlPredicate);
//            System.out.println("	node0 -> "+node0);
//            System.out.println("	node1 -> "+node1);
            boolean bl = this.m_ternaryExtensionTable.addTuple(this.m_ternaryAuxiliaryTupleAdd, dependencySet, isCore);
            return bl;
        }
        finally {
            this.m_addActive = false;
        }
    }

	public boolean checkEqualMetamodellingRuleIteration(Node node0, Node node1) {
		//Si ambos nodos que se mergean tienen axioma de metamodelling
		List<OWLClassExpression> node0Classes = MetamodellingAxiomHelper.getMetamodellingClassesByIndividual(this.m_tableau.nodeToMetaIndividual.get(node0.m_nodeID), this.m_tableau.m_permanentDLOntology);
		List<OWLClassExpression> node1Classes = MetamodellingAxiomHelper.getMetamodellingClassesByIndividual(this.m_tableau.nodeToMetaIndividual.get(node1.m_nodeID), this.m_tableau.m_permanentDLOntology);
		if (!node0Classes.isEmpty() && !node1Classes.isEmpty()) {	
			for (OWLClassExpression node0Class : node0Classes) {
				for (OWLClassExpression node1Class : node1Classes) {
					//Checkear si existe Axiom A U !B y B U !A
					// <#B>(X) :- <#A>(X)
					// <#A>(X) :- <#B>(X)
					if (node1Class != node0Class && !MetamodellingAxiomHelper.containsSubClassOfAxiom( node0Class, node1Class, this.m_tableau.m_permanentDLOntology) && !MetamodellingAxiomHelper.containsSubClassOfAxiom(node1Class, node0Class, this.m_tableau.m_permanentDLOntology)) {
						MetamodellingAxiomHelper.addSubClassOfAxioms(node0Class, node1Class, this.m_tableau.m_permanentDLOntology, this.m_tableau);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkInequalityMetamodellingRuleIteration(Node node0, Node node1) {
		//Si ambos nodos que se mergean tienen axioma de metamodelling
		List<OWLClassExpression> node0Classes = MetamodellingAxiomHelper.getMetamodellingClassesByIndividual(this.m_tableau.nodeToMetaIndividual.get(node0.m_nodeID), this.m_tableau.m_permanentDLOntology);
		List<OWLClassExpression> node1Classes = MetamodellingAxiomHelper.getMetamodellingClassesByIndividual(this.m_tableau.nodeToMetaIndividual.get(node1.m_nodeID), this.m_tableau.m_permanentDLOntology);
		if (!node0Classes.isEmpty() && !node1Classes.isEmpty()) {
			for (OWLClassExpression node0Class : node0Classes) {
				for (OWLClassExpression node1Class : node1Classes) {
					//Checkear si existe Axiom (A int not-B) union (not-A int B) 
					
					if (node1Class != node0Class) { 
						Atom def0 = MetamodellingAxiomHelper.containsInequalityRuleAxiom( node0Class, node1Class, this.m_tableau);
						if ((def0 != null && !this.m_tableau.containsClassAssertion(def0.getDLPredicate().toString())) || def0 == null) {
							MetamodellingAxiomHelper.addInequalityMetamodellingRuleAxiom(node0Class, node1Class, this.m_tableau.m_permanentDLOntology, this.m_tableau, def0);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkCloseMetamodellingRuleIteration(Node node0, Node node1) {
		Node node0Equivalent = node0.getCanonicalNode();
		Node node1Equivalent = node1.getCanonicalNode();
		if (!this.m_tableau.areDifferentIndividual(node0Equivalent, node1Equivalent) && !this.m_tableau.areSameIndividual(node0Equivalent, node1Equivalent) && !this.m_tableau.alreadyCreateDisjunction(node0Equivalent, node1Equivalent)) {
			//create ground disjunction x=y or x!=y
			Atom eqAtom = Atom.create(Equality.INSTANCE, this.m_tableau.mapNodeIndividual.get(node0Equivalent.m_nodeID), this.m_tableau.mapNodeIndividual.get(node1Equivalent.m_nodeID));
			DLPredicate equalityPredicate = eqAtom.getDLPredicate();
			Atom ineqAtom = Atom.create(Inequality.INSTANCE, this.m_tableau.mapNodeIndividual.get(node0Equivalent.m_nodeID), this.m_tableau.mapNodeIndividual.get(node1Equivalent.m_nodeID));
			DLPredicate inequalityPredicate = ineqAtom.getDLPredicate();
			DLPredicate[] dlPredicates = new DLPredicate[] {equalityPredicate, inequalityPredicate};
			//DLPredicate[] dlPredicates = new DLPredicate[] {inequalityPredicate, equalityPredicate};
			
			int hashCode = 0;
            for (int disjunctIndex = 0; disjunctIndex < dlPredicates.length; ++disjunctIndex) {
                hashCode = hashCode * 7 + dlPredicates[disjunctIndex].hashCode();
            }
            
			GroundDisjunctionHeader gdh = new GroundDisjunctionHeader(dlPredicates, hashCode , null);
			DependencySet dependencySet = getActualDependencySet();
			System.out.println("DEPENDENCYSET FOR CLOSE RULE DISJUNCTION -> "+dependencySet);
			GroundDisjunction groundDisjunction = new GroundDisjunction(this.m_tableau, gdh, new Node[] {node0Equivalent, node1Equivalent, node0Equivalent, node1Equivalent}, new boolean[] {true, true}, dependencySet);
			if (!this.m_tableau.alreadyCreateDisjunction(node0Equivalent, node1Equivalent) && !groundDisjunction.isSatisfied(this.m_tableau)) {
				this.m_tableau.addGroundDisjunction(groundDisjunction);
				this.m_tableau.addCreatedDisjuntcion(node0Equivalent, node1Equivalent);
				System.out.println("CLOSE RULE add the following disjunction -> "+eqAtom.toString() +" OR "+ineqAtom.toString());
				return true;
			}
		}
		return false;
	}
	
	public DependencySet getActualDependencySet() {
		return this.m_dependencySetFactory.lastEntryAddedIndex == -1 || this.m_dependencySetFactory.m_entries[this.m_dependencySetFactory.lastEntryAddedIndex] == null ? this.m_dependencySetFactory.emptySet() : this.m_dependencySetFactory.m_entries[this.m_dependencySetFactory.lastEntryAddedIndex];
	}
	
    public boolean addAssertion(DLPredicate dlPredicate, Node node0, Node node1, Node node2, DependencySet dependencySet, boolean isCore) {
        if (this.m_addActive) {
            throw new IllegalStateException("ExtensionManager is not reentrant.");
        }
        this.m_fouraryAuxiliaryTupleAdd[0] = dlPredicate;
        this.m_fouraryAuxiliaryTupleAdd[1] = node0;
        this.m_fouraryAuxiliaryTupleAdd[2] = node1;
        this.m_fouraryAuxiliaryTupleAdd[3] = node2;
//        System.out.println("[!] Se agrega Assertion a la m_fouraryAuxiliaryTupleAdd");
//        System.out.println("	dlPredicate -> "+dlPredicate);
//        System.out.println("	node0 -> "+node0);
//        System.out.println("	node1 -> "+node1);
//        System.out.println("	node2 -> "+node2);
        
        return this.addTuple(this.m_fouraryAuxiliaryTupleAdd, dependencySet, isCore);
    }

    public boolean addAnnotatedEquality(AnnotatedEquality annotatedEquality, Node node0, Node node1, Node node2, DependencySet dependencySet) {
        return this.m_tableau.m_nominalIntroductionManager.addAnnotatedEquality(annotatedEquality, node0, node1, node2, dependencySet);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addTuple(Object[] tuple, DependencySet dependencySet, boolean isCore) {
        if (tuple.length == 0) {
            boolean result = this.m_clashDependencySet == null;
            this.setClash(dependencySet);
            return result;
        }
        if (Equality.INSTANCE.equals(tuple[0])) {
            return this.m_tableau.m_mergingManager.mergeNodes((Node)tuple[1], (Node)tuple[2], dependencySet);
        }
        if (tuple[0] instanceof AnnotatedEquality) {
            return this.m_tableau.m_nominalIntroductionManager.addAnnotatedEquality((AnnotatedEquality)tuple[0], (Node)tuple[1], (Node)tuple[2], (Node)tuple[3], dependencySet);
        }
        if (this.m_addActive) {
            throw new IllegalStateException("ExtensionManager is not reentrant.");
        }
        this.m_addActive = true;
        try {
            boolean result = this.getExtensionTable(tuple.length).addTuple(tuple, dependencySet, isCore);
            if(result) {
            	System.out.print("TUPLE ADDED: ");
            	for (Object obj : tuple) {
            		System.out.println(obj+" ");
            	}
            	System.out.println();
            }
            return result;
        }
        finally {
            this.m_addActive = false;
        }
    }

}

