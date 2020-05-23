package org.fing.metamodelling;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.HermiT.model.Atom;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.DLOntology;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.model.Individual;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.model.Term;
import org.semanticweb.HermiT.model.Variable;
import org.semanticweb.HermiT.structural.OWLClausification;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator;
import org.semanticweb.HermiT.tableau.ExtensionTable;
import org.semanticweb.HermiT.tableau.HyperresolutionManager;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.HyperresolutionManager.BodyAtomsSwapper;
import org.semanticweb.HermiT.tableau.Tableau;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLMetamodellingAxiom;

public class MetamodellingAxiomHelper {
	
	private final static String DEF_STRING = "<internal:def#";
	
	/*
	  Dado un individuo y una ontologia devuelve la lista de clases asociados a ese individuo a traves de axiomas de metamodelling 
	*/
	public static List<OWLClassExpression> getMetamodellingClassesByIndividual(Individual ind, DLOntology ontology) {
		List<OWLClassExpression> classes = new ArrayList<OWLClassExpression>();
		if (ind != null) {
			for (OWLMetamodellingAxiom metamodellingAxiom : ontology.getMetamodellingAxioms()) {
				if (ind.toString().equals(metamodellingAxiom.getMetamodelIndividual().toString())) {
					classes.add(metamodellingAxiom.getModelClass());
				}
			}
		}
		return classes;
	}
	
	/*
	  Devuelve true si en la ontologia se encuentra el axioma 
	  <internal:def#1>(X) v <internal:def#2>(X) :- <internal:def#0>(X),
	  :- <#A>(X), <internal:def#2>(X), 
	  <#B>(X) :- <internal:def#2>(X),  
	  :- <#B>(X), <internal:def#1>(X), 
	  <#A>(X) :- <internal:def#1>(X)
	*/
	public static boolean containsInequalityRuleAxiom(OWLClassExpression classA, OWLClassExpression classB, Tableau tableau) {
		DLOntology ontology = tableau.getPermanentDLOntology();
		String defString = DEF_STRING;
		for (DLClause dlClause : ontology.getDLClauses()) {
			if (dlClause.isGeneralConceptInclusion() && dlClause.getHeadLength() == 2 && dlClause.getBodyLength() == 1) {
				Atom def0 = dlClause.getBodyAtom(0);
				Atom def1 = dlClause.getHeadAtom(0);
				Atom def2 = dlClause.getHeadAtom(1);
				if (def0.toString().startsWith(defString) && def1.toString().startsWith(defString) && def2.toString().startsWith(defString)) {
					//Identify the possible axiom
					
					//Set conditions 1
					boolean hasDef1SubClassA = false;
					boolean hasDef2SubClassB = false;
					boolean hasDef2DiffClassA = false;
					boolean hasDef1DiffClassB = false;
					//Set conditions 2
					boolean hasDef2SubClassA = false;
					boolean hasDef1SubClassB = false;
					boolean hasDef1DiffClassA = false;
					boolean hasDef2DiffClassB = false;
					
					//Search for the other subAxioms
					for (DLClause subDLClause : ontology.getDLClauses()) {
						if (subDLClause.isAtomicConceptInclusion() && subDLClause.isGeneralConceptInclusion() && subDLClause.getHeadLength() == 1 && subDLClause.getBodyLength() == 1) { 
							
							//<#A>(X) :- <internal:def#1>(X) || <internal:def#1> :- <#A>(X)
							if ((subDLClause.getHeadAtom(0).getDLPredicate().toString().equals(classA.toString()) && subDLClause.getBodyAtom(0).toString().equals(def1.toString())) ||
									subDLClause.getHeadAtom(0).toString().equals(def1.toString()) && subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classA.toString())) {
								hasDef1SubClassA = true;
							}
							
							//<#B>(X) :- <internal:def#2>(X) || <internal:def#2> :- <#B>(X)
							if ((subDLClause.getHeadAtom(0).getDLPredicate().toString().equals(classB.toString()) && subDLClause.getBodyAtom(0).toString().equals(def2.toString())) ||
									subDLClause.getHeadAtom(0).toString().equals(def2.toString()) && subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classB.toString())) {
								hasDef2SubClassB = true;
							}
																
							//<#B>(X) :- <internal:def#1>(X) || <internal:def#1> :- <#B>(X)
							if ((subDLClause.getHeadAtom(0).getDLPredicate().toString().equals(classB.toString()) && subDLClause.getBodyAtom(0).toString().equals(def1.toString())) ||
									subDLClause.getHeadAtom(0).toString().equals(def1.toString()) && subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classB.toString())) {
								hasDef1SubClassB = true;
							}
							
							//<#A>(X) :- <internal:def#2>(X) || <internal:def#2> :- <#A>(X)
							if ((subDLClause.getHeadAtom(0).getDLPredicate().toString().equals(classA.toString()) && subDLClause.getBodyAtom(0).toString().equals(def2.toString())) ||
									subDLClause.getHeadAtom(0).toString().equals(def2.toString()) && subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classA.toString())) {
								hasDef2SubClassA = true;
							}
						} else if (subDLClause.isGeneralConceptInclusion() && subDLClause.getHeadLength() == 0 && subDLClause.getBodyLength() == 2) {
							
							//:- <#A>(X), <internal:def#2>(X) || :- <internal:def#2>(X), <#A>(X)
							if ((subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classA.toString()) && subDLClause.getBodyAtom(1).toString().equals(def2.toString())) || 
									(subDLClause.getBodyAtom(0).toString().equals(def2.toString()) && subDLClause.getBodyAtom(1).getDLPredicate().toString().equals(classA.toString()))) {
								hasDef2DiffClassA = true;
							}
							
							//:- <#B>(X), <internal:def#1>(X) || :- <internal:def#1>(X), <#B>(X)
							if ((subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classB.toString()) && subDLClause.getBodyAtom(1).toString().equals(def1.toString())) || 
									(subDLClause.getBodyAtom(0).toString().equals(def1.toString()) && subDLClause.getBodyAtom(1).getDLPredicate().toString().equals(classB.toString()))) {
								hasDef1DiffClassB = true;
							}
														
							//:- <#A>(X), <internal:def#1>(X) || :- <internal:def#1>(X), <#A>(X)
							if ((subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classA.toString()) && subDLClause.getBodyAtom(1).toString().equals(def1.toString())) || 
									(subDLClause.getBodyAtom(0).toString().equals(def1.toString()) && subDLClause.getBodyAtom(1).getDLPredicate().toString().equals(classA.toString()))) {
								hasDef1DiffClassA = true;
							}
							
							//:- <#B>(X), <internal:def#2>(X) || :- <internal:def#2>(X), <#B>(X)
							if ((subDLClause.getBodyAtom(0).getDLPredicate().toString().equals(classB.toString()) && subDLClause.getBodyAtom(1).toString().equals(def2.toString())) || 
									(subDLClause.getBodyAtom(0).toString().equals(def2.toString()) && subDLClause.getBodyAtom(1).getDLPredicate().toString().equals(classB.toString()))) {
								hasDef2DiffClassB = true;
							}
						}
					}
					
					if ((hasDef1SubClassA && hasDef2SubClassB && hasDef2DiffClassA && hasDef1DiffClassB) || 
							(hasDef2SubClassA && hasDef1SubClassB && hasDef1DiffClassA && hasDef2DiffClassB)) {
						//Ya se verifico que existen los DLClauses, resta ver si esta en la binaryTable, o sea que si hay un Z con esa clase.
						return tableau.containsClassAssertion(def0.getDLPredicate().toString());
					}
				}
			}
		}
		return false;
	}
	
	/*
	  Devuelve true si en la ontologia se encuentra el axioma que dice que classA es subclase de classB 
	*/
	public static boolean containsSubClassOfAxiom(OWLClassExpression classA, OWLClassExpression classB, DLOntology ontology) {
		for (DLClause dlClause : ontology.getDLClauses()) {
			if (dlClause.isAtomicConceptInclusion()) {
				if (dlClause.getHeadAtom(0).getDLPredicate().toString().equals(classA.toString()) && dlClause.getBodyAtom(0).getDLPredicate().toString().equals(classB.toString())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
		Agrega los siguientes axiomas en la ontologia:
			- classA es subclase de classB
			- classB es subclase de classA
		Crea el nuevo hyperresolutionManager y lo asocia al tableau
	*/
	public static boolean addSubClassOfAxioms(OWLClassExpression classA, OWLClassExpression classB, DLOntology ontology, Tableau tableau) {
		
		Atom classAAtom = Atom.create(AtomicConcept.create(classA.toString().substring(1, classA.toString().length()-1)), Variable.create("X"));
		Atom classBAtom = Atom.create(AtomicConcept.create(classB.toString().substring(1, classB.toString().length()-1)), Variable.create("X"));
		
		Atom[] headAtoms1 = {classAAtom};
		Atom[] bodyAtoms1 = {classBAtom};
		
		DLClause dlClause1 = new DLClause( headAtoms1, bodyAtoms1);
		
		Atom[] headAtoms2 = {classBAtom};
		Atom[] bodyAtoms2 = {classAAtom};
		
		DLClause dlClause2 = new DLClause( headAtoms2, bodyAtoms2);
		
		if (!ontology.getDLClauses().contains(dlClause1) && !ontology.getDLClauses().contains(dlClause2)) {
			ontology.getDLClauses().add(dlClause1);
			ontology.getDLClauses().add(dlClause2);
			
			System.out.println("Se agregan 2 dlClauses");
			System.out.println("-> "+dlClause1);
			System.out.println("-> "+dlClause2);
			
			List<DLClause> dlClauses = new ArrayList<DLClause>() { 
	            { 
	                add(dlClause1); 
	                add(dlClause2); 
	            } 
	        }; 

			createHyperResolutionManager(tableau, dlClauses);
			
			return true;
		}
		
		return false;
	}
	
	/*
	 Se obtiene el proximo X de <internal:def#X> 
	*/
	private static int getNextDef(DLOntology ontology) {
		int nextDef = 0;
		for (DLClause dlClause : ontology.getDLClauses()) {
			for (Atom atom : dlClause.getHeadAtoms()) {
				if (atom.getDLPredicate().toString().contains(DEF_STRING)) {
					String defString = atom.getDLPredicate().toString().substring(atom.getDLPredicate().toString().length() - 2, atom.getDLPredicate().toString().length() - 1);
					int def = Integer.parseInt(defString);
					if (def > nextDef) {
						nextDef = def;
					}
				}
			}
			for (Atom atom : dlClause.getBodyAtoms()) {
				if (atom.getDLPredicate().toString().contains(DEF_STRING)) {
					String defString = atom.getDLPredicate().toString().substring(atom.getDLPredicate().toString().length() - 2, atom.getDLPredicate().toString().length() - 1);
					int def = Integer.parseInt(defString);
					if (def > nextDef) {
						nextDef = def;
					}
				}
			}
		}
		return nextDef == 0 ? nextDef : nextDef + 1;
	}
	
	public static void addInequalityMetamodellingRuleAxiom(OWLClassExpression classA, OWLClassExpression classB, DLOntology ontology, Tableau tableau) {
		int nextDef = getNextDef(ontology);
		String def0 = DEF_STRING + nextDef + ">";
		String def1 = DEF_STRING + (nextDef+1) + ">";
		String def2 = DEF_STRING + (nextDef+2) + ">";
		
		Atom def0Atom = Atom.create(AtomicConcept.create(def0.substring(1, def0.length()-1)), Variable.create("X"));
		Atom def1Atom = Atom.create(AtomicConcept.create(def1.substring(1, def1.length()-1)), Variable.create("X"));
		Atom def2Atom = Atom.create(AtomicConcept.create(def2.substring(1, def2.length()-1)), Variable.create("X"));
		Atom classAAtom = Atom.create(AtomicConcept.create(classA.toString().substring(1, classA.toString().length()-1)), Variable.create("X"));
		Atom classBAtom = Atom.create(AtomicConcept.create(classB.toString().substring(1, classB.toString().length()-1)), Variable.create("X"));
		
		Atom[] headAtoms1 = {def1Atom, def2Atom};
		Atom[] bodyAtoms1 = {def0Atom};
		
		DLClause dlClause1 = new DLClause( headAtoms1, bodyAtoms1);
		
		Atom[] headAtoms2 = {classAAtom};
		Atom[] bodyAtoms2 = {def1Atom};
		
		DLClause dlClause2 = new DLClause( headAtoms2, bodyAtoms2);
		
		Atom[] headAtoms3 = {classBAtom};
		Atom[] bodyAtoms3 = {def2Atom};
		
		DLClause dlClause3 = new DLClause( headAtoms3, bodyAtoms3);
		
		Atom[] headAtoms4 = {};
		Atom[] bodyAtoms4 = {classAAtom, def2Atom};
		
		DLClause dlClause4 = new DLClause( headAtoms4, bodyAtoms4);
		
		Atom[] headAtoms5 = {};
		Atom[] bodyAtoms5 = {classBAtom, def1Atom};
		
		DLClause dlClause5 = new DLClause( headAtoms5, bodyAtoms5);
		
		//New Inequality Axiom
		Atom[] headAtoms6 = {};
		Atom[] bodyAtoms6 = {classAAtom, classBAtom};
		
		DLClause dlClause6 = new DLClause( headAtoms6, bodyAtoms6);
		
		ontology.getDLClauses().add(dlClause1);
		
		ontology.getDLClauses().add(dlClause1);
		ontology.getDLClauses().add(dlClause2);
		ontology.getDLClauses().add(dlClause3);
		ontology.getDLClauses().add(dlClause4);
		ontology.getDLClauses().add(dlClause5);
		ontology.getDLClauses().add(dlClause6);
		
		System.out.println("Se agregan dlClauses por la != metamodelling rule:");
		System.out.println("-> "+dlClause1);
		System.out.println("-> "+dlClause2);
		System.out.println("-> "+dlClause3);
		System.out.println("-> "+dlClause4);
		System.out.println("-> "+dlClause5);
		System.out.println("-> "+dlClause6);
		
		List<DLClause> dlClauses = new ArrayList<DLClause>() { 
            { 
                add(dlClause1); 
                add(dlClause2); 
                add(dlClause3); 
                add(dlClause4); 
                add(dlClause5);
                add(dlClause6);
            } 
        }; 
        
        //create node
        Node zNode = tableau.createNewNamedNode(tableau.getDependencySetFactory().emptySet());
        
        //create axiom in binary table
        tableau.getExtensionManager().addConceptAssertion((LiteralConcept)((Object)def0Atom.getDLPredicate()), zNode, tableau.getDependencySetFactory().emptySet(), true);

		createHyperResolutionManager(tableau, dlClauses);
		
	}
	
	/*
	 Crea un nuevo hyperresolutionManager para asociarlo al tableau
	 Tambien crea un nuevo BranchedHyperresolutionManager con el nuevo hyperresolutionManager, sus nuevos axiomas y los estados de backtracking 
	 actuales del tableau en la coleccion de BranchedHyperresolutionManagers del tableau.
	*/
	private static void createHyperResolutionManager(Tableau tableau, List<DLClause> dlClauses) {
		
		HyperresolutionManager hypM =  new HyperresolutionManager(tableau, tableau.getPermanentDLOntology().getDLClauses());
		
		BranchedHyperresolutionManager branchedHypM = new BranchedHyperresolutionManager();
		branchedHypM.setHyperresolutionManager(hypM);
		branchedHypM.setBranchingIndex(tableau.getCurrentBranchingPointLevel());
		branchedHypM.setBranchingPoint(tableau.m_currentBranchingPoint);
		for (DLClause dlClause: dlClauses) {
			branchedHypM.getDlClausesAdded().add(dlClause);
		}
		
		tableau.getBranchedHyperresolutionManagers().add(branchedHypM);	

		
		tableau.setPermanentHyperresolutionManager(hypM);

	}
  
}
