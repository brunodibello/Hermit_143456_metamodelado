/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.tableau;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fing.metamodelling.BranchedHyperresolutionManager;
import org.semanticweb.HermiT.existentials.ExistentialExpansionStrategy;
import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.Constant;
import org.semanticweb.HermiT.model.ConstantEnumeration;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.DLOntology;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.DataRange;
import org.semanticweb.HermiT.model.DescriptionGraph;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.model.Individual;
import org.semanticweb.HermiT.model.Inequality;
import org.semanticweb.HermiT.model.InternalDatatype;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.model.NegatedAtomicRole;
import org.semanticweb.HermiT.model.Term;
import org.semanticweb.HermiT.monitor.TableauMonitor;
import org.semanticweb.HermiT.tableau.BranchingPoint;
import org.semanticweb.HermiT.tableau.ClashManager;
import org.semanticweb.HermiT.tableau.DatatypeManager;
import org.semanticweb.HermiT.tableau.DependencySet;
import org.semanticweb.HermiT.tableau.DependencySetFactory;
import org.semanticweb.HermiT.tableau.DescriptionGraphManager;
import org.semanticweb.HermiT.tableau.DisjunctionBranchingPoint;
import org.semanticweb.HermiT.tableau.ExistentialExpansionManager;
import org.semanticweb.HermiT.tableau.ExtensionManager;
import org.semanticweb.HermiT.tableau.GroundDisjunction;
import org.semanticweb.HermiT.tableau.GroundDisjunctionHeader;
import org.semanticweb.HermiT.tableau.HyperresolutionManager;
import org.semanticweb.HermiT.tableau.InterruptFlag;
import org.semanticweb.HermiT.tableau.MergingManager;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.NodeType;
import org.semanticweb.HermiT.tableau.NominalIntroductionManager;
import org.semanticweb.HermiT.tableau.PermanentDependencySet;
import org.semanticweb.HermiT.tableau.ReasoningTaskDescription;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLMetamodellingAxiom;

public final class Tableau
implements Serializable {
    private static final long serialVersionUID = -28982363158925221L;
    protected final InterruptFlag m_interruptFlag;
    protected final Map<String, Object> m_parameters;
    protected final TableauMonitor m_tableauMonitor;
    protected final ExistentialExpansionStrategy m_existentialExpansionStrategy;
    protected final DLOntology m_permanentDLOntology;
    protected DLOntology m_additionalDLOntology;
    protected final DependencySetFactory m_dependencySetFactory;
    protected final ExtensionManager m_extensionManager;
    protected final ClashManager m_clashManager;
    protected HyperresolutionManager m_permanentHyperresolutionManager;
    protected ArrayList<BranchedHyperresolutionManager> branchedHyperresolutionManagers;
    protected HyperresolutionManager m_additionalHyperresolutionManager;
    protected final MergingManager m_mergingManager;
    protected final ExistentialExpansionManager m_existentialExpasionManager;
    protected final NominalIntroductionManager m_nominalIntroductionManager;
    protected final DescriptionGraphManager m_descriptionGraphManager;
    protected final DatatypeManager m_datatypeManager;
    protected final List<List<ExistentialConcept>> m_existentialConceptsBuffers;
    protected final boolean m_useDisjunctionLearning;
    protected final boolean m_hasDescriptionGraphs;
    protected BranchingPoint[] m_branchingPoints;
    public int m_currentBranchingPoint;
    protected int m_nonbacktrackableBranchingPoint;
    protected boolean m_isCurrentModelDeterministic;
    protected boolean m_needsThingExtension;
    protected boolean m_needsNamedExtension;
    protected boolean m_needsRDFSLiteralExtension;
    protected boolean m_checkDatatypes;
    protected boolean m_checkUnknownDatatypeRestrictions;
    protected int m_allocatedNodes;
    protected int m_numberOfNodesInTableau;
    protected int m_numberOfMergedOrPrunedNodes;
    protected int m_numberOfNodeCreations;
    protected Node m_firstFreeNode;
    protected Node m_firstTableauNode;
    protected Node m_lastTableauNode;
    protected Node m_lastMergedOrPrunedNode;
    protected GroundDisjunction m_firstGroundDisjunction;
    protected GroundDisjunction m_firstUnprocessedGroundDisjunction;
    protected Map<Integer, Individual> nodeToMetaIndividual;
    protected List<Node> metamodellingNodes;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Tableau(InterruptFlag interruptFlag, TableauMonitor tableauMonitor, ExistentialExpansionStrategy existentialsExpansionStrategy, boolean useDisjunctionLearning, DLOntology permanentDLOntology, DLOntology additionalDLOntology, Map<String, Object> parameters) {
        if (additionalDLOntology != null && !additionalDLOntology.getAllDescriptionGraphs().isEmpty()) {
            throw new IllegalArgumentException("Additional ontology cannot contain description graphs.");
        }
        this.m_interruptFlag = interruptFlag;
        this.m_interruptFlag.startTask();
        try {
            this.m_parameters = parameters;
            this.m_tableauMonitor = tableauMonitor;
            this.m_existentialExpansionStrategy = existentialsExpansionStrategy;
            this.m_permanentDLOntology = permanentDLOntology;
            this.m_additionalDLOntology = additionalDLOntology;
            this.m_dependencySetFactory = new DependencySetFactory();
            this.m_extensionManager = new ExtensionManager(this);
            this.m_clashManager = new ClashManager(this);
            this.m_permanentHyperresolutionManager = new HyperresolutionManager(this, this.m_permanentDLOntology.getDLClauses());
            this.m_additionalHyperresolutionManager = this.m_additionalDLOntology != null ? new HyperresolutionManager(this, this.m_additionalDLOntology.getDLClauses()) : null;
            this.m_mergingManager = new MergingManager(this);
            this.m_existentialExpasionManager = new ExistentialExpansionManager(this);
            this.m_nominalIntroductionManager = new NominalIntroductionManager(this);
            this.m_descriptionGraphManager = new DescriptionGraphManager(this);
            this.m_datatypeManager = new DatatypeManager(this);
            this.m_existentialExpansionStrategy.initialize(this);
            this.m_existentialConceptsBuffers = new ArrayList<List<ExistentialConcept>>();
            this.m_useDisjunctionLearning = useDisjunctionLearning;
            this.m_hasDescriptionGraphs = !this.m_permanentDLOntology.getAllDescriptionGraphs().isEmpty();
            this.m_branchingPoints = new BranchingPoint[2];
            this.m_currentBranchingPoint = -1;
            this.m_nonbacktrackableBranchingPoint = -1;
            this.nodeToMetaIndividual = new HashMap<Integer, Individual>();
            this.metamodellingNodes = new ArrayList<Node>();
            this.branchedHyperresolutionManagers = new ArrayList<BranchedHyperresolutionManager>();
            
            BranchedHyperresolutionManager branchedHypM = new BranchedHyperresolutionManager();
    		branchedHypM.setHyperresolutionManager(this.m_permanentHyperresolutionManager);
    		branchedHypM.setBranchingIndex(this.getCurrentBranchingPointLevel());
    		branchedHypM.setBranchingPoint(this.m_currentBranchingPoint);
    		this.branchedHyperresolutionManagers.add(branchedHypM);
    		
            this.updateFlagsDependentOnAdditionalOntology();
            if (this.m_tableauMonitor != null) {
                this.m_tableauMonitor.setTableau(this);
            }
        }
        finally {
            this.m_interruptFlag.endTask();
        }
    }

    public ArrayList<BranchedHyperresolutionManager> getBranchedHyperresolutionManagers() {
		return branchedHyperresolutionManagers;
	}

	public void setBranchedHyperresolutionManagers(
			ArrayList<BranchedHyperresolutionManager> branchedHyperresolutionManagers) {
		this.branchedHyperresolutionManagers = branchedHyperresolutionManagers;
	}

	public InterruptFlag getInterruptFlag() {
        return this.m_interruptFlag;
    }

    public DLOntology getPermanentDLOntology() {
        return this.m_permanentDLOntology;
    }

    public DLOntology getAdditionalDLOntology() {
        return this.m_additionalDLOntology;
    }

    public Map<String, Object> getParameters() {
        return this.m_parameters;
    }

    public TableauMonitor getTableauMonitor() {
        return this.m_tableauMonitor;
    }

    public ExistentialExpansionStrategy getExistentialsExpansionStrategy() {
        return this.m_existentialExpansionStrategy;
    }

    public boolean isDeterministic() {
        return this.m_permanentDLOntology.isHorn() && (this.m_additionalDLOntology == null || this.m_additionalDLOntology.isHorn()) && this.m_existentialExpansionStrategy.isDeterministic();
    }

    public DependencySetFactory getDependencySetFactory() {
        return this.m_dependencySetFactory;
    }

    public ExtensionManager getExtensionManager() {
        return this.m_extensionManager;
    }

    public HyperresolutionManager getPermanentHyperresolutionManager() {
        return this.m_permanentHyperresolutionManager;
    }
    
    public void setPermanentHyperresolutionManager(HyperresolutionManager hypM) {
    	this.m_permanentHyperresolutionManager = hypM;
    }

    public HyperresolutionManager getAdditionalHyperresolutionManager() {
        return this.m_additionalHyperresolutionManager;
    }

    public MergingManager getMergingManager() {
        return this.m_mergingManager;
    }

    public ExistentialExpansionManager getExistentialExpansionManager() {
        return this.m_existentialExpasionManager;
    }

    public NominalIntroductionManager getNominalIntroductionManager() {
        return this.m_nominalIntroductionManager;
    }

    public DescriptionGraphManager getDescriptionGraphManager() {
        return this.m_descriptionGraphManager;
    }

    public void clear() {
        this.m_allocatedNodes = 0;
        this.m_numberOfNodesInTableau = 0;
        this.m_numberOfMergedOrPrunedNodes = 0;
        this.m_numberOfNodeCreations = 0;
        this.m_firstFreeNode = null;
        this.m_firstTableauNode = null;
        this.m_lastTableauNode = null;
        this.m_lastMergedOrPrunedNode = null;
        this.m_firstGroundDisjunction = null;
        this.m_firstUnprocessedGroundDisjunction = null;
        this.m_branchingPoints = new BranchingPoint[2];
        this.m_currentBranchingPoint = -1;
        this.m_nonbacktrackableBranchingPoint = -1;
        this.m_dependencySetFactory.clear();
        this.m_extensionManager.clear();
        this.m_clashManager.clear();
        this.m_permanentHyperresolutionManager.clear();
        if (this.m_additionalHyperresolutionManager != null) {
            this.m_additionalHyperresolutionManager.clear();
        }
        this.m_mergingManager.clear();
        this.m_existentialExpasionManager.clear();
        this.m_nominalIntroductionManager.clear();
        this.m_descriptionGraphManager.clear();
        this.m_isCurrentModelDeterministic = true;
        this.m_existentialExpansionStrategy.clear();
        this.m_datatypeManager.clear();
        this.m_existentialConceptsBuffers.clear();
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.tableauCleared();
        }
    }

    public boolean supportsAdditionalDLOntology(DLOntology additionalDLOntology) {
        boolean hasBottomObjectProperty;
        boolean hasInverseRoles = this.m_permanentDLOntology.hasInverseRoles() || this.m_additionalDLOntology != null && this.m_additionalDLOntology.hasInverseRoles();
        boolean hasNominals = this.m_permanentDLOntology.hasNominals() || this.m_additionalDLOntology != null && this.m_additionalDLOntology.hasNominals();
        boolean isHorn = this.m_permanentDLOntology.isHorn() || this.m_additionalDLOntology != null && this.m_additionalDLOntology.isHorn();
        boolean permanentHasBottomObjectProperty = this.m_permanentDLOntology.containsObjectRole(AtomicRole.BOTTOM_OBJECT_ROLE);
        boolean bl = hasBottomObjectProperty = permanentHasBottomObjectProperty || this.m_additionalDLOntology != null && this.m_additionalDLOntology.containsObjectRole(AtomicRole.BOTTOM_OBJECT_ROLE);
        if (!additionalDLOntology.getAllDescriptionGraphs().isEmpty() || additionalDLOntology.hasInverseRoles() && !hasInverseRoles || additionalDLOntology.hasNominals() && !hasNominals || !additionalDLOntology.isHorn() && isHorn || hasBottomObjectProperty && !permanentHasBottomObjectProperty) {
            return false;
        }
        for (DLClause dlClause : additionalDLOntology.getDLClauses()) {
            if (!dlClause.isAtomicRoleInclusion() && !dlClause.isAtomicRoleInverseInclusion() && !dlClause.isFunctionalityAxiom() && !dlClause.isInverseFunctionalityAxiom()) continue;
            return false;
        }
        return true;
    }

    public void setAdditionalDLOntology(DLOntology additionalDLOntology) {
        if (!this.supportsAdditionalDLOntology(additionalDLOntology)) {
            throw new IllegalArgumentException("Additional DL-ontology contains features that are incompatible with this tableau.");
        }
        this.m_additionalDLOntology = additionalDLOntology;
        this.m_additionalHyperresolutionManager = new HyperresolutionManager(this, this.m_additionalDLOntology.getDLClauses());
        this.m_existentialExpansionStrategy.additionalDLOntologySet(this.m_additionalDLOntology);
        this.m_datatypeManager.additionalDLOntologySet(this.m_additionalDLOntology);
        this.updateFlagsDependentOnAdditionalOntology();
    }

    public void clearAdditionalDLOntology() {
        this.m_additionalDLOntology = null;
        this.m_additionalHyperresolutionManager = null;
        this.m_existentialExpansionStrategy.additionalDLOntologyCleared();
        this.m_datatypeManager.additionalDLOntologyCleared();
        this.updateFlagsDependentOnAdditionalOntology();
    }

    protected void updateFlagsDependentOnAdditionalOntology() {
        this.m_needsThingExtension = this.m_permanentHyperresolutionManager.m_tupleConsumersByDeltaPredicate.containsKey(AtomicConcept.THING);
        this.m_needsNamedExtension = this.m_permanentHyperresolutionManager.m_tupleConsumersByDeltaPredicate.containsKey(AtomicConcept.INTERNAL_NAMED);
        this.m_needsRDFSLiteralExtension = this.m_permanentHyperresolutionManager.m_tupleConsumersByDeltaPredicate.containsKey(InternalDatatype.RDFS_LITERAL);
        this.m_checkDatatypes = this.m_permanentDLOntology.hasDatatypes();
        this.m_checkUnknownDatatypeRestrictions = this.m_permanentDLOntology.hasUnknownDatatypeRestrictions();
        if (this.m_additionalHyperresolutionManager != null) {
            this.m_needsThingExtension |= this.m_additionalHyperresolutionManager.m_tupleConsumersByDeltaPredicate.containsKey(AtomicConcept.THING);
            this.m_needsNamedExtension |= this.m_additionalHyperresolutionManager.m_tupleConsumersByDeltaPredicate.containsKey(AtomicConcept.INTERNAL_NAMED);
            this.m_needsRDFSLiteralExtension |= this.m_additionalHyperresolutionManager.m_tupleConsumersByDeltaPredicate.containsKey(InternalDatatype.RDFS_LITERAL);
        }
        if (this.m_additionalDLOntology != null) {
            this.m_checkDatatypes |= this.m_additionalDLOntology.hasDatatypes();
            this.m_checkUnknownDatatypeRestrictions |= this.m_additionalDLOntology.hasUnknownDatatypeRestrictions();
        }
    }

    public boolean isSatisfiable(boolean loadAdditionalABox, Set<Atom> perTestPositiveFactsNoDependency, Set<Atom> perTestNegativeFactsNoDependency, Set<Atom> perTestPositiveFactsDummyDependency, Set<Atom> perTestNegativeFactsDummyDependency, Map<Individual, Node> nodesForIndividuals, ReasoningTaskDescription reasoningTaskDescription) {
        boolean loadPermanentABox = this.m_permanentDLOntology.hasNominals() || this.m_additionalDLOntology != null && this.m_additionalDLOntology.hasNominals();
        return this.isSatisfiable(loadPermanentABox, loadAdditionalABox, perTestPositiveFactsNoDependency, perTestNegativeFactsNoDependency, perTestPositiveFactsDummyDependency, perTestNegativeFactsDummyDependency, new HashMap<Term, Node>(), nodesForIndividuals, reasoningTaskDescription);
    }

    public boolean isSatisfiable(boolean loadPermanentABox, boolean loadAdditionalABox, Set<Atom> perTestPositiveFactsNoDependency, Set<Atom> perTestNegativeFactsNoDependency, Set<Atom> perTestPositiveFactsDummyDependency, Set<Atom> perTestNegativeFactsDummyDependency, Map<Individual, Node> nodesForIndividuals, ReasoningTaskDescription reasoningTaskDescription) {
        return this.isSatisfiable(loadPermanentABox, loadAdditionalABox, perTestPositiveFactsNoDependency, perTestNegativeFactsNoDependency, perTestPositiveFactsDummyDependency, perTestNegativeFactsDummyDependency, new HashMap<Term, Node>(), nodesForIndividuals, reasoningTaskDescription);
    }

    public boolean isSatisfiable(boolean loadPermanentABox, boolean loadAdditionalABox, Set<Atom> perTestPositiveFactsNoDependency, Set<Atom> perTestNegativeFactsNoDependency, Set<Atom> perTestPositiveFactsDummyDependency, Set<Atom> perTestNegativeFactsDummyDependency, Map<Term, Node> termsToNodes, Map<Individual, Node> nodesForIndividuals, ReasoningTaskDescription reasoningTaskDescription) {
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.isSatisfiableStarted(reasoningTaskDescription);
        }
        this.clear();
        System.out.println("[!] isSatisfiable Started");
        //Agregar individuos del MBox a los nodos
        for (OWLMetamodellingAxiom metamodellingAxiom : this.m_permanentDLOntology.getMetamodellingAxioms()) {
        	Individual ind = Individual.create(metamodellingAxiom.getMetamodelIndividual().toStringID());
        	if (!termsToNodes.containsKey(ind)) {
        		Node node = this.createNewNamedNode(this.m_dependencySetFactory.emptySet());
        		System.out.println("    Individual -> "+ind+" || Node -> "+node);
            	termsToNodes.put(ind, node);
        	}
        	this.nodeToMetaIndividual.put(termsToNodes.get(ind).m_nodeID, ind);
        	this.metamodellingNodes.add(termsToNodes.get(ind));
        }
        if (loadPermanentABox) {
        	System.out.println("loadPermanentABox");
        	System.out.println("	positiveFacts:");
            for (Atom atom : this.m_permanentDLOntology.getPositiveFacts()) {
            	System.out.println("		atom -> "+atom);
                this.loadPositiveFact(termsToNodes, atom, this.m_dependencySetFactory.emptySet());
            }
            System.out.println("	negativeFacts:");
            for (Atom atom : this.m_permanentDLOntology.getNegativeFacts()) {
            	System.out.println("		atom -> "+atom);
                this.loadNegativeFact(termsToNodes, atom, this.m_dependencySetFactory.emptySet());
            }
        }
        if (loadAdditionalABox && this.m_additionalDLOntology != null) {
        	System.out.println("loadAdditionalABox");
        	System.out.println("	positiveFacts:");
            for (Atom atom : this.m_additionalDLOntology.getPositiveFacts()) {
            	System.out.println("		atom -> "+atom);
                this.loadPositiveFact(termsToNodes, atom, this.m_dependencySetFactory.emptySet());
            }
            System.out.println("	negativeFacts:");
            for (Atom atom : this.m_additionalDLOntology.getNegativeFacts()) {
            	System.out.println("		atom -> "+atom);
                this.loadNegativeFact(termsToNodes, atom, this.m_dependencySetFactory.emptySet());
            }
        }
        if (perTestPositiveFactsNoDependency != null && !perTestPositiveFactsNoDependency.isEmpty()) {
        	System.out.println("	perTestPositiveFactsNoDependency - positiveFacts:");
            for (Atom atom : perTestPositiveFactsNoDependency) {
            	System.out.println("		atom -> "+atom);
                this.loadPositiveFact(termsToNodes, atom, this.m_dependencySetFactory.emptySet());
            }
        }
        if (perTestNegativeFactsNoDependency != null && !perTestNegativeFactsNoDependency.isEmpty()) {
        	System.out.println("	perTestNegativeFactsNoDependency - negativeFacts:");
            for (Atom atom : perTestNegativeFactsNoDependency) {
            	System.out.println("		atom -> "+atom);
                this.loadNegativeFact(termsToNodes, atom, this.m_dependencySetFactory.emptySet());
            }
        }
        if (perTestPositiveFactsDummyDependency != null && !perTestPositiveFactsDummyDependency.isEmpty() || perTestNegativeFactsDummyDependency != null && !perTestNegativeFactsDummyDependency.isEmpty()) {
            this.m_branchingPoints[0] = new BranchingPoint(this);
            ++this.m_currentBranchingPoint;
            this.m_nonbacktrackableBranchingPoint = this.m_currentBranchingPoint;
            PermanentDependencySet dependencySet = this.m_dependencySetFactory.addBranchingPoint(this.m_dependencySetFactory.emptySet(), this.m_currentBranchingPoint);
            System.out.println("	perTestPositiveFactsDummyDependency - positiveFacts:");
            if (perTestPositiveFactsDummyDependency != null && !perTestPositiveFactsDummyDependency.isEmpty()) {
                for (Atom atom : perTestPositiveFactsDummyDependency) {
                	System.out.println("		atom -> "+atom);
                    this.loadPositiveFact(termsToNodes, atom, dependencySet);
                }
            }
            System.out.println("	perTestNegativeFactsDummyDependency - negativeFacts:");
            if (perTestNegativeFactsDummyDependency != null && !perTestNegativeFactsDummyDependency.isEmpty()) {
                for (Atom atom : perTestNegativeFactsDummyDependency) {
                	System.out.println("		atom -> "+atom);
                    this.loadNegativeFact(termsToNodes, atom, dependencySet);
                }
            }
        }
        if (nodesForIndividuals != null) {
        	System.out.println("	Iterate throuth nodesForIndividuals");
            for (Map.Entry<Individual, Node> entry : nodesForIndividuals.entrySet()) {
                if (termsToNodes.get(entry.getKey()) == null) {
                    Atom topAssertion = Atom.create(AtomicConcept.THING, entry.getKey());
                    System.out.println("		topAssertion -> "+topAssertion);
                    this.loadPositiveFact(termsToNodes, topAssertion, this.m_dependencySetFactory.emptySet());
                }
                System.out.println("		termsToNodes.get(entry.getKey()) -> "+termsToNodes.get(entry.getKey()));
                entry.setValue(termsToNodes.get(entry.getKey()));
            }
        }
        if (this.m_firstTableauNode == null) {
            this.createNewNINode(this.m_dependencySetFactory.emptySet());
        }
        boolean result = this.runCalculus();
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.isSatisfiableFinished(reasoningTaskDescription, result);
        }
        return result;
    }

    protected void loadPositiveFact(Map<Term, Node> termsToNodes, Atom atom, DependencySet dependencySet) {
    	System.out.println("		* loadPositiveFact");
        DLPredicate dlPredicate = atom.getDLPredicate();
        System.out.println("		 dlPredicate -> "+dlPredicate);
        if (dlPredicate instanceof LiteralConcept) {
        	System.out.println("		is LiteralConcept");
            this.m_extensionManager.addConceptAssertion((LiteralConcept)((Object)dlPredicate), this.getNodeForTerm(termsToNodes, atom.getArgument(0), dependencySet), dependencySet, true);
        } else if (dlPredicate instanceof AtomicRole || Equality.INSTANCE.equals(dlPredicate) || Inequality.INSTANCE.equals(dlPredicate)) {
        	System.out.println("		is AtomicRole or Equality or Inequality instance");
        	this.m_extensionManager.addAssertion(dlPredicate, this.getNodeForTerm(termsToNodes, atom.getArgument(0), dependencySet), this.getNodeForTerm(termsToNodes, atom.getArgument(1), dependencySet), dependencySet, true);
        } else if (dlPredicate instanceof DescriptionGraph) {
        	System.out.println("		is DescriptionGraph");
            DescriptionGraph descriptionGraph = (DescriptionGraph)dlPredicate;
            Object[] tuple = new Object[descriptionGraph.getArity() + 1];
            tuple[0] = descriptionGraph;
            for (int argumentIndex = 0; argumentIndex < descriptionGraph.getArity(); ++argumentIndex) {
                tuple[argumentIndex + 1] = this.getNodeForTerm(termsToNodes, atom.getArgument(argumentIndex), dependencySet);
            }
            System.out.println("		tuple -> "+tuple);
            this.m_extensionManager.addTuple(tuple, dependencySet, true);
        } else {
            throw new IllegalArgumentException("Unsupported type of positive ground atom.");
        }
    }

    protected void loadNegativeFact(Map<Term, Node> termsToNodes, Atom atom, DependencySet dependencySet) {
    	System.out.println("		* loadNegativeFact");
        DLPredicate dlPredicate = atom.getDLPredicate();
        System.out.println("		 dlPredicate -> "+dlPredicate);
        if (dlPredicate instanceof LiteralConcept) {
        	System.out.println("		is LiteralConcept");
            this.m_extensionManager.addConceptAssertion(((LiteralConcept)((Object)dlPredicate)).getNegation(), this.getNodeForTerm(termsToNodes, atom.getArgument(0), dependencySet), dependencySet, true);
        } else if (dlPredicate instanceof AtomicRole) {
        	System.out.println("		is AtomicRole");
            Object[] ternaryTuple = this.m_extensionManager.m_ternaryAuxiliaryTupleAdd;
            ternaryTuple[0] = NegatedAtomicRole.create((AtomicRole)dlPredicate);
            ternaryTuple[1] = this.getNodeForTerm(termsToNodes, atom.getArgument(0), dependencySet);
            ternaryTuple[2] = this.getNodeForTerm(termsToNodes, atom.getArgument(1), dependencySet);
            System.out.println("		ternaryTuple -> "+ternaryTuple);
            this.m_extensionManager.addTuple(ternaryTuple, dependencySet, true);
        } else if (Equality.INSTANCE.equals(dlPredicate)) {
        	System.out.println("		is Equality");
            this.m_extensionManager.addAssertion(Inequality.INSTANCE, this.getNodeForTerm(termsToNodes, atom.getArgument(0), dependencySet), this.getNodeForTerm(termsToNodes, atom.getArgument(1), dependencySet), dependencySet, true);
        } else if (Inequality.INSTANCE.equals(dlPredicate)) {
        	System.out.println("		is Inequality");
            this.m_extensionManager.addAssertion(Equality.INSTANCE, this.getNodeForTerm(termsToNodes, atom.getArgument(0), dependencySet), this.getNodeForTerm(termsToNodes, atom.getArgument(1), dependencySet), dependencySet, true);
        } else {
            throw new IllegalArgumentException("Unsupported type of negative ground atom.");
        }
    }

    //Crea nodos en el grafo
    protected Node getNodeForTerm(Map<Term, Node> termsToNodes, Term term, DependencySet dependencySet) {
        Node node = termsToNodes.get(term);
        if (node == null) {
        	System.out.println("***** Going to create a Node ******");
        	System.out.println("term -> "+term);
            if (term instanceof Individual) {
                Individual individual = (Individual)term;
                node = individual.isAnonymous() ? this.createNewNINode(dependencySet) : this.createNewNamedNode(dependencySet);
            } else {
                Constant constant = (Constant)term;
                node = this.createNewRootConstantNode(dependencySet);
                if (!constant.isAnonymous()) {
                    this.m_extensionManager.addAssertion(ConstantEnumeration.create(new Constant[]{constant}), node, dependencySet, true);
                }
            }
            termsToNodes.put(term, node);
            System.out.println("node -> "+node);
        }
        return node.getCanonicalNode();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected boolean runCalculus() {
    	int iterations = 0;
        this.m_interruptFlag.startTask();
        try {
            boolean existentialsAreExact = this.m_existentialExpansionStrategy.isExact();
            if (this.m_tableauMonitor != null) {
                this.m_tableauMonitor.saturateStarted();
            }
            boolean hasMoreWork = true;
            while (hasMoreWork) {
                if (this.m_tableauMonitor != null) {
                    this.m_tableauMonitor.iterationStarted();
                }
                System.out.println("====> ITERATION "+(++iterations));
                hasMoreWork = this.doIteration();
                if (this.m_tableauMonitor != null) {
                    this.m_tableauMonitor.iterationFinished();
                }
                if (existentialsAreExact || hasMoreWork || this.m_extensionManager.containsClash()) continue;
                if (this.m_tableauMonitor != null) {
                    this.m_tableauMonitor.iterationStarted();
                }
                hasMoreWork = this.m_existentialExpansionStrategy.expandExistentials(true);
                if (this.m_tableauMonitor == null) continue;
                this.m_tableauMonitor.iterationFinished();
            }
            if (this.m_tableauMonitor != null) {
                this.m_tableauMonitor.saturateFinished(!this.m_extensionManager.containsClash());
            }
            if (!this.m_extensionManager.containsClash()) {
                this.m_existentialExpansionStrategy.modelFound();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
        finally {
            this.m_interruptFlag.endTask();
        }
    }

    protected boolean doIteration() {
    	System.out.println("[!] Start Iteration [!]");
        if (!this.m_extensionManager.containsClash()) {
            this.m_nominalIntroductionManager.processAnnotatedEqualities();
            boolean hasChange = false;
            while (this.m_extensionManager.propagateDeltaNew() && !this.m_extensionManager.containsClash()) {
                if (this.m_hasDescriptionGraphs && !this.m_extensionManager.containsClash()) {
                    this.m_descriptionGraphManager.checkGraphConstraints();
                }
                if (!this.m_extensionManager.containsClash()) {
                    this.m_permanentHyperresolutionManager.applyDLClauses();
                }
                if (this.m_additionalHyperresolutionManager != null && !this.m_extensionManager.containsClash()) {
                    this.m_additionalHyperresolutionManager.applyDLClauses();
                }
                if (this.m_checkUnknownDatatypeRestrictions && !this.m_extensionManager.containsClash()) {
                    this.m_datatypeManager.applyUnknownDatatypeRestrictionSemantics();
                }
                if (this.m_checkDatatypes && !this.m_extensionManager.containsClash()) {
                    this.m_datatypeManager.checkDatatypeConstraints();
                }
                if (!this.m_extensionManager.containsClash()) {
                    this.m_nominalIntroductionManager.processAnnotatedEqualities();
                }
                if (this.m_extensionManager.containsClash() && this.branchedHyperresolutionManagers.size() > 1 && this.m_branchingPoints[0] != null) {
                	if (this.branchedHyperresolutionManagers.get(this.branchedHyperresolutionManagers.size()-1).getBranchingPoint() == this.m_currentBranchingPoint && this.branchedHyperresolutionManagers.get(this.branchedHyperresolutionManagers.size()-1).getBranchingPoint() == this.getCurrentBranchingPointLevel()) {
                		for (DLClause dlClauseAdded : this.branchedHyperresolutionManagers.get(this.branchedHyperresolutionManagers.size()-1).getDlClausesAdded()) {
                    		this.getPermanentDLOntology().getDLClauses().remove(dlClauseAdded);
                    	}
                        this.setPermanentHyperresolutionManager(new HyperresolutionManager(this, this.getPermanentDLOntology().getDLClauses()));
//                        this.m_extensionManager.resetDeltaNew();
//                        this.m_dependencySetFactory.removeUnusedSets();
//                        //this.m_extensionManager.clearClash();
//                        this.m_extensionManager.backtrack();
//                        this.backtrackLastMergedOrPrunedNode();
//                        this.backtrackLastMergedOrPrunedNode();
//                        this.m_branchingPoints[this.m_currentBranchingPoint].startNextChoice(this, this.m_extensionManager.getClashDependencySet());
//                        this.m_extensionManager.clearClash();
                        //return true;
     
                        this.m_existentialExpansionStrategy.backtrack();
                        this.m_existentialExpasionManager.backtrack();
                        this.m_nominalIntroductionManager.backtrack();
                        this.m_extensionManager.backtrack();
                        Node lastMergedOrPrunedNodeShouldBe = this.m_branchingPoints[this.m_currentBranchingPoint].m_lastMergedOrPrunedNode;
                        while (this.m_lastMergedOrPrunedNode != lastMergedOrPrunedNodeShouldBe) {
                            this.backtrackLastMergedOrPrunedNode();
                        }
                        Node lastTableauNodeShouldBe = this.m_branchingPoints[this.m_currentBranchingPoint].m_lastTableauNode;
                        while (lastTableauNodeShouldBe != this.m_lastTableauNode) {
                            this.destroyLastTableauNode();
                        }
                        try {
                        	this.m_branchingPoints[this.m_currentBranchingPoint].startNextChoice(this, this.m_extensionManager.getClashDependencySet());
                        } catch (ArrayIndexOutOfBoundsException e) {
                        	return false;
                        }                
                        this.m_extensionManager.clearClash();
                	}
                	
                } else if (checkEqualMetamodellingRuleIteration()) {
                	//si se agregan los axiomas por rule 1, ademas de crear de nuevo el hyperresolution manager, reiniciar el delta new
                	this.m_extensionManager.resetDeltaNew();
                }
                hasChange = true;
            }
            if (hasChange) {
                return true;
            }
        }
        if (!this.m_extensionManager.containsClash() && this.m_existentialExpansionStrategy.expandExistentials(false)) {
            return true;
        }
        if (!this.m_extensionManager.containsClash()) {
            while (this.m_firstUnprocessedGroundDisjunction != null) {
            	System.out.println("@@@@@@ Backtracking @@@@");
                GroundDisjunction groundDisjunction = this.m_firstUnprocessedGroundDisjunction;
                System.out.println("@@@@@@ groundDisjunction -> "+groundDisjunction);
                if (this.m_tableauMonitor != null) {
                    this.m_tableauMonitor.processGroundDisjunctionStarted(groundDisjunction);
                }
                this.m_firstUnprocessedGroundDisjunction = groundDisjunction.m_previousGroundDisjunction;
                if (!groundDisjunction.isPruned() && !groundDisjunction.isSatisfied(this)) {
                    int[] sortedDisjunctIndexes = groundDisjunction.getGroundDisjunctionHeader().getSortedDisjunctIndexes();
                    DependencySet dependencySet = groundDisjunction.getDependencySet();
                    if (groundDisjunction.getNumberOfDisjuncts() > 1) {
                        DisjunctionBranchingPoint branchingPoint = new DisjunctionBranchingPoint(this, groundDisjunction, sortedDisjunctIndexes);
                        this.pushBranchingPoint(branchingPoint);
                        dependencySet = this.m_dependencySetFactory.addBranchingPoint(dependencySet, branchingPoint.getLevel());
                    }
                    if (this.m_tableauMonitor != null) {
                        this.m_tableauMonitor.disjunctProcessingStarted(groundDisjunction, sortedDisjunctIndexes[0]);
                    }
                    groundDisjunction.addDisjunctToTableau(this, sortedDisjunctIndexes[0], dependencySet);
                    if (this.m_tableauMonitor != null) {
                        this.m_tableauMonitor.disjunctProcessingFinished(groundDisjunction, sortedDisjunctIndexes[0]);
                        this.m_tableauMonitor.processGroundDisjunctionFinished(groundDisjunction);
                    }
                    return true;
                }
                if (this.m_tableauMonitor != null) {
                    this.m_tableauMonitor.groundDisjunctionSatisfied(groundDisjunction);
                }
                this.m_interruptFlag.checkInterrupt();
            }
        }
        if (this.m_extensionManager.containsClash()) {
        	DependencySet clashDependencySet = this.m_extensionManager.getClashDependencySet();
    		int newCurrentBranchingPoint = clashDependencySet.getMaximumBranchingPoint();
    		if (newCurrentBranchingPoint <= this.m_nonbacktrackableBranchingPoint) {
    		    return false;
    		}
    		this.backtrackTo(newCurrentBranchingPoint);
    		BranchingPoint branchingPoint = this.getCurrentBranchingPoint();
    		if (this.m_tableauMonitor != null) {
    		    this.m_tableauMonitor.startNextBranchingPointStarted(branchingPoint);
    		}
    		branchingPoint.startNextChoice(this, clashDependencySet);
    		if (this.m_tableauMonitor != null) {
    		    this.m_tableauMonitor.startNextBranchingPointFinished(branchingPoint);
    		}
    		this.m_dependencySetFactory.removeUnusedSets();
    		return true;
        }
        return false;
    }
    
    public boolean checkEqualMetamodellingRuleIteration() {
    	for (Node node1 : this.metamodellingNodes) {
    		for (Node node2 : this.metamodellingNodes) {
    			if (areSameIndividual(node1, node2)) {
    				if (this.m_extensionManager.checkEqualMetamodellingRule(node1, node2)) return true;
    			}
    		}
    	}
    	return false;
    }

    private boolean areSameIndividual(Node node1, Node node2) {
    	if (node1.m_nodeID == node2.m_nodeID) return true;
    	Individual paramIndividual1 = this.nodeToMetaIndividual.get(node1.m_nodeID);
    	Individual paramIndividual2 = this.nodeToMetaIndividual.get(node2.m_nodeID);
    	//chequeo en axiomas de la ontologia
    	for (Atom positiveFact : this.m_permanentDLOntology.getPositiveFacts()) {
    		if (Equality.INSTANCE.equals(positiveFact.getDLPredicate())) {
    			Individual ind1 = (Individual) positiveFact.getArgument(0);
    			Individual ind2 = (Individual) positiveFact.getArgument(1);
    			//chequear que los nodos coincidan con los individuos del axioma o si son el mismo individuo
    			if ((paramIndividual1 == ind1 && paramIndividual2 == ind2 ) || (paramIndividual2 == ind1 && paramIndividual1 == ind2 )) {
    				return true;
    			}
    		}
    	}
    	//chequeo en axiomas inferidos -> Cuando mergea agregar el ABox axiom a la permanentDLOntology y listo. (NO FUNCIONA PARA BACKTRACKING)
    	//PRUEBA CHECKEANDO EL ESTADO DEL NODO ( SI SE MERGEA CAMBIA A MERGED Y GUARDA AL NODO QUE SE MERGEO )
    	return ((node1.isMerged() && node1.m_mergedInto == node2) || (node2.isMerged() && node2.m_mergedInto == node1)) ;
    }
    
    public boolean isCurrentModelDeterministic() {
        return this.m_isCurrentModelDeterministic;
    }

    public int getCurrentBranchingPointLevel() {
        return this.m_currentBranchingPoint;
    }

    public BranchingPoint getCurrentBranchingPoint() {
        return this.m_branchingPoints[this.m_currentBranchingPoint];
    }

    public void addGroundDisjunction(GroundDisjunction groundDisjunction) {
        groundDisjunction.m_nextGroundDisjunction = this.m_firstGroundDisjunction;
        groundDisjunction.m_previousGroundDisjunction = null;
        if (this.m_firstGroundDisjunction != null) {
            this.m_firstGroundDisjunction.m_previousGroundDisjunction = groundDisjunction;
        }
        this.m_firstGroundDisjunction = groundDisjunction;
        if (this.m_firstUnprocessedGroundDisjunction == null) {
            this.m_firstUnprocessedGroundDisjunction = groundDisjunction;
        }
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.groundDisjunctionDerived(groundDisjunction);
        }
    }

    public GroundDisjunction getFirstUnprocessedGroundDisjunction() {
        return this.m_firstUnprocessedGroundDisjunction;
    }

    public void pushBranchingPoint(BranchingPoint branchingPoint) {
        assert (this.m_currentBranchingPoint + 1 == branchingPoint.m_level);
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.pushBranchingPointStarted(branchingPoint);
        }
        ++this.m_currentBranchingPoint;
        if (this.m_currentBranchingPoint >= this.m_branchingPoints.length) {
            BranchingPoint[] newBranchingPoints = new BranchingPoint[this.m_currentBranchingPoint * 3 / 2];
            System.arraycopy(this.m_branchingPoints, 0, newBranchingPoints, 0, this.m_branchingPoints.length);
            this.m_branchingPoints = newBranchingPoints;
        }
        this.m_branchingPoints[this.m_currentBranchingPoint] = branchingPoint;
        this.m_extensionManager.branchingPointPushed();
        this.m_existentialExpasionManager.branchingPointPushed();
        this.m_existentialExpansionStrategy.branchingPointPushed();
        this.m_nominalIntroductionManager.branchingPointPushed();
        this.m_isCurrentModelDeterministic = false;
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.pushBranchingPointFinished(branchingPoint);
        }
    }

    protected void backtrackTo(int newCurrentBrancingPoint) {
        BranchingPoint branchingPoint = this.m_branchingPoints[newCurrentBrancingPoint];
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.backtrackToStarted(branchingPoint);
        }
        for (int index = newCurrentBrancingPoint + 1; index <= this.m_currentBranchingPoint; ++index) {
            this.m_branchingPoints[index] = null;
        }
        this.m_currentBranchingPoint = newCurrentBrancingPoint;
        this.m_firstUnprocessedGroundDisjunction = branchingPoint.m_firstUnprocessedGroundDisjunction;
        GroundDisjunction firstGroundDisjunctionShouldBe = branchingPoint.m_firstGroundDisjunction;
        while (this.m_firstGroundDisjunction != firstGroundDisjunctionShouldBe) {
            this.m_firstGroundDisjunction.destroy(this);
            this.m_firstGroundDisjunction = this.m_firstGroundDisjunction.m_nextGroundDisjunction;
        }
        if (this.m_firstGroundDisjunction != null) {
            this.m_firstGroundDisjunction.m_previousGroundDisjunction = null;
        }
        this.m_existentialExpansionStrategy.backtrack();
        this.m_existentialExpasionManager.backtrack();
        this.m_nominalIntroductionManager.backtrack();
        this.m_extensionManager.backtrack();
        Node lastMergedOrPrunedNodeShouldBe = branchingPoint.m_lastMergedOrPrunedNode;
        while (this.m_lastMergedOrPrunedNode != lastMergedOrPrunedNodeShouldBe) {
            this.backtrackLastMergedOrPrunedNode();
        }
        Node lastTableauNodeShouldBe = branchingPoint.m_lastTableauNode;
        while (lastTableauNodeShouldBe != this.m_lastTableauNode) {
            this.destroyLastTableauNode();
        }
        this.m_extensionManager.clearClash();
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.backtrackToFinished(branchingPoint);
        }
    }

    public Node createNewNamedNode(DependencySet dependencySet) {
        return this.createNewNodeRaw(dependencySet, null, NodeType.NAMED_NODE, 0);
    }

    public Node createNewNINode(DependencySet dependencySet) {
        return this.createNewNodeRaw(dependencySet, null, NodeType.NI_NODE, 0);
    }

    public Node createNewTreeNode(DependencySet dependencySet, Node parent) {
        return this.createNewNodeRaw(dependencySet, parent, NodeType.TREE_NODE, parent.getTreeDepth() + 1);
    }

    public Node createNewConcreteNode(DependencySet dependencySet, Node parent) {
        return this.createNewNodeRaw(dependencySet, parent, NodeType.CONCRETE_NODE, parent.getTreeDepth() + 1);
    }

    public Node createNewRootConstantNode(DependencySet dependencySet) {
        return this.createNewNodeRaw(dependencySet, null, NodeType.ROOT_CONSTANT_NODE, 0);
    }

    public Node createNewGraphNode(Node parent, DependencySet dependencySet) {
        return this.createNewNodeRaw(dependencySet, parent, NodeType.GRAPH_NODE, parent == null ? 0 : parent.getTreeDepth());
    }

    protected Node createNewNodeRaw(DependencySet dependencySet, Node parent, NodeType nodeType, int treeDepth) {
        Node node;
        if (this.m_firstFreeNode == null) {
            node = new Node(this);
            ++this.m_allocatedNodes;
        } else {
            node = this.m_firstFreeNode;
            this.m_firstFreeNode = this.m_firstFreeNode.m_nextTableauNode;
        }
        assert (node.m_nodeID == -1);
        assert (node.m_nodeState == null);
        node.initialize(++this.m_numberOfNodesInTableau, parent, nodeType, treeDepth);
        this.m_existentialExpansionStrategy.nodeInitialized(node);
        node.m_previousTableauNode = this.m_lastTableauNode;
        if (this.m_lastTableauNode == null) {
            this.m_firstTableauNode = node;
        } else {
            this.m_lastTableauNode.m_nextTableauNode = node;
        }
        this.m_lastTableauNode = node;
        this.m_existentialExpansionStrategy.nodeStatusChanged(node);
        ++this.m_numberOfNodeCreations;
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.nodeCreated(node);
        }
        if (nodeType.m_isAbstract) {
            this.m_extensionManager.addConceptAssertion(AtomicConcept.THING, node, dependencySet, true);
            if (nodeType == NodeType.NAMED_NODE && this.m_needsNamedExtension) {
                this.m_extensionManager.addConceptAssertion(AtomicConcept.INTERNAL_NAMED, node, dependencySet, true);
            }
        } else {
            this.m_extensionManager.addDataRangeAssertion(InternalDatatype.RDFS_LITERAL, node, dependencySet, true);
        }
        return node;
    }

    public void mergeNode(Node node, Node mergeInto, DependencySet dependencySet) {
        assert (node.m_nodeState == Node.NodeState.ACTIVE);
        assert (node.m_mergedInto == null);
        assert (node.m_mergedIntoDependencySet == null);
        assert (node.m_previousMergedOrPrunedNode == null);
        node.m_mergedInto = mergeInto;
        node.m_mergedIntoDependencySet = this.m_dependencySetFactory.getPermanent(dependencySet);
        this.m_dependencySetFactory.addUsage(node.m_mergedIntoDependencySet);
        node.m_nodeState = Node.NodeState.MERGED;
        node.m_previousMergedOrPrunedNode = this.m_lastMergedOrPrunedNode;
        this.m_lastMergedOrPrunedNode = node;
        ++this.m_numberOfMergedOrPrunedNodes;
        this.m_existentialExpansionStrategy.nodeStatusChanged(node);
        this.m_existentialExpansionStrategy.nodesMerged(node, mergeInto);
    }

    public void pruneNode(Node node) {
        assert (node.m_nodeState == Node.NodeState.ACTIVE);
        assert (node.m_mergedInto == null);
        assert (node.m_mergedIntoDependencySet == null);
        assert (node.m_previousMergedOrPrunedNode == null);
        node.m_nodeState = Node.NodeState.PRUNED;
        node.m_previousMergedOrPrunedNode = this.m_lastMergedOrPrunedNode;
        this.m_lastMergedOrPrunedNode = node;
        ++this.m_numberOfMergedOrPrunedNodes;
        this.m_existentialExpansionStrategy.nodeStatusChanged(node);
    }

    protected void backtrackLastMergedOrPrunedNode() {
        Node node = this.m_lastMergedOrPrunedNode;
        assert (node.m_nodeState == Node.NodeState.MERGED && node.m_mergedInto != null || node.m_nodeState == Node.NodeState.PRUNED && node.m_mergedInto == null);
        Node savedMergedInfo = null;
        if (node.m_nodeState == Node.NodeState.MERGED) {
            this.m_dependencySetFactory.removeUsage(node.m_mergedIntoDependencySet);
            savedMergedInfo = node.m_mergedInto;
            node.m_mergedInto = null;
            node.m_mergedIntoDependencySet = null;
        }
        node.m_nodeState = Node.NodeState.ACTIVE;
        this.m_lastMergedOrPrunedNode = node.m_previousMergedOrPrunedNode;
        node.m_previousMergedOrPrunedNode = null;
        --this.m_numberOfMergedOrPrunedNodes;
        this.m_existentialExpansionStrategy.nodeStatusChanged(node);
        if (savedMergedInfo != null) {
            this.m_existentialExpansionStrategy.nodesUnmerged(node, savedMergedInfo);
        }
    }

    protected void destroyLastTableauNode() {
        Node node = this.m_lastTableauNode;
        assert (node.m_nodeState == Node.NodeState.ACTIVE);
        assert (node.m_mergedInto == null);
        assert (node.m_mergedIntoDependencySet == null);
        assert (node.m_previousMergedOrPrunedNode == null);
        this.m_existentialExpansionStrategy.nodeDestroyed(node);
        if (node.m_previousTableauNode == null) {
            this.m_firstTableauNode = null;
        } else {
            node.m_previousTableauNode.m_nextTableauNode = null;
        }
        this.m_lastTableauNode = node.m_previousTableauNode;
        node.destroy();
        node.m_nextTableauNode = this.m_firstFreeNode;
        this.m_firstFreeNode = node;
        --this.m_numberOfNodesInTableau;
        if (this.m_tableauMonitor != null) {
            this.m_tableauMonitor.nodeDestroyed(node);
        }
    }

    public int getNumberOfNodeCreations() {
        return this.m_numberOfNodeCreations;
    }

    public Node getFirstTableauNode() {
        return this.m_firstTableauNode;
    }

    public Node getLastTableauNode() {
        return this.m_lastTableauNode;
    }

    public int getNumberOfAllocatedNodes() {
        return this.m_allocatedNodes;
    }

    public int getNumberOfNodesInTableau() {
        return this.m_numberOfNodesInTableau;
    }

    public int getNumberOfMergedOrPrunedNodes() {
        return this.m_numberOfMergedOrPrunedNodes;
    }

    public Node getNode(int nodeID) {
        for (Node node = this.m_firstTableauNode; node != null; node = node.getNextTableauNode()) {
            if (node.getNodeID() != nodeID) continue;
            return node;
        }
        return null;
    }

    protected List<ExistentialConcept> getExistentialConceptsBuffer() {
        if (this.m_existentialConceptsBuffers.isEmpty()) {
            return new ArrayList<ExistentialConcept>();
        }
        return this.m_existentialConceptsBuffers.remove(this.m_existentialConceptsBuffers.size() - 1);
    }

    public void putExistentialConceptsBuffer(List<ExistentialConcept> buffer) {
        assert (buffer.isEmpty());
        this.m_existentialConceptsBuffers.add(buffer);
    }

    public void checkTableauList() {
        Node node = this.m_firstTableauNode;
        int numberOfNodesInTableau = 0;
        while (node != null) {
            if (node.m_previousTableauNode == null) {
                if (this.m_firstTableauNode != node) {
                    throw new IllegalStateException("First tableau node is pointing wrongly.");
                }
            } else if (node.m_previousTableauNode.m_nextTableauNode != node) {
                throw new IllegalStateException("Previous tableau node is pointing wrongly.");
            }
            if (node.m_nextTableauNode == null) {
                if (this.m_lastTableauNode != node) {
                    throw new IllegalStateException("Last tableau node is pointing wrongly.");
                }
            } else if (node.m_nextTableauNode.m_previousTableauNode != node) {
                throw new IllegalStateException("Next tableau node is pointing wrongly.");
            }
            ++numberOfNodesInTableau;
            node = node.m_nextTableauNode;
        }
        if (numberOfNodesInTableau != this.m_numberOfNodesInTableau) {
            throw new IllegalStateException("Invalid number of nodes in the tableau.");
        }
    }
}

