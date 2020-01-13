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
import org.semanticweb.HermiT.model.Term;
import org.semanticweb.HermiT.model.Variable;
import org.semanticweb.HermiT.structural.OWLClausification;
import org.semanticweb.HermiT.tableau.DLClauseEvaluator;
import org.semanticweb.HermiT.tableau.ExtensionTable;
import org.semanticweb.HermiT.tableau.HyperresolutionManager.BodyAtomsSwapper;
import org.semanticweb.HermiT.tableau.Tableau;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLMetamodellingAxiom;

public class MetamodellingAxiomHelper {
	
	public static List<OWLClassExpression> getMetamodellingClassesByIndividual(Individual ind, DLOntology ontology) {
		List<OWLClassExpression> classes = new ArrayList<OWLClassExpression>();
		for (OWLMetamodellingAxiom metamodellingAxiom : ontology.getMetamodellingAxioms()) {
			if (ind.toString().equals(metamodellingAxiom.getMetamodelIndividual().toString())) {
				classes.add(metamodellingAxiom.getModelClass());
			}
		}
		return classes;
	}
	
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
	
	public static void addSubClassOfAxioms(OWLClassExpression classA, OWLClassExpression classB, DLOntology ontology, Tableau tableau) {
		Atom[] headAtoms1 = {Atom.create(AtomicConcept.create(classA.toString().substring(1, classA.toString().length()-1)), Variable.create("X"))};
		Atom[] bodyAtoms1 = {Atom.create(AtomicConcept.create(classB.toString().substring(1, classB.toString().length()-1)), Variable.create("X"))};
		
		DLClause dlClause1 = new DLClause( headAtoms1, bodyAtoms1);
		
		Atom[] headAtoms2 = {Atom.create(AtomicConcept.create(classB.toString().substring(1, classB.toString().length()-1)), Variable.create("X"))};
		Atom[] bodyAtoms2 = {Atom.create(AtomicConcept.create(classA.toString().substring(1, classA.toString().length()-1)), Variable.create("X"))};
		
		DLClause dlClause2 = new DLClause( headAtoms2, bodyAtoms2);
		
		if (!ontology.getDLClauses().contains(dlClause1) && !ontology.getDLClauses().contains(dlClause2)) {
			ontology.getDLClauses().add(dlClause1);
			ontology.getDLClauses().add(dlClause2);
			
			System.out.println("Se agregan 2 dlClauses");
			System.out.println("-> "+dlClause1);
			System.out.println("-> "+dlClause2);

			tableau.setPermanentHyperresolutionManager();
		}
	}
}
