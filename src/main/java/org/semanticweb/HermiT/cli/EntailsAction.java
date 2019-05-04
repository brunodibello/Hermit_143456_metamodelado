/*
 * Decompiled with CFR 0.137.
 * 
 * Could not load the following classes:
 *  org.semanticweb.owlapi.apibinding.OWLManager
 *  org.semanticweb.owlapi.model.IRI
 *  org.semanticweb.owlapi.model.OWLAxiom
 *  org.semanticweb.owlapi.model.OWLDataFactory
 *  org.semanticweb.owlapi.model.OWLOntology
 *  org.semanticweb.owlapi.model.OWLOntologyCreationException
 *  org.semanticweb.owlapi.model.OWLOntologyManager
 */
package org.semanticweb.HermiT.cli;

import java.io.PrintWriter;
import java.util.Set;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.EntailmentChecker;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.cli.Action;
import org.semanticweb.HermiT.cli.StatusOutput;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

class EntailsAction
implements Action {
    final IRI conclusionIRI;

    public EntailsAction(Configuration config, IRI conclusionIRI) {
        this.conclusionIRI = conclusionIRI;
    }

    @Override
    public void run(Reasoner hermit, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
        status.log(2, "Checking whether the loaded ontology entails the conclusion ontology");
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        try {
            OWLOntology conclusions = m.loadOntology(this.conclusionIRI);
            status.log(2, "Conclusion ontology loaded.");
            EntailmentChecker checker = new EntailmentChecker(hermit, m.getOWLDataFactory());
            boolean isEntailed = checker.entails(conclusions.getLogicalAxioms());
            status.log(2, "Conclusion ontology is " + (isEntailed ? "" : "not ") + "entailed.");
            output.println(isEntailed);
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        output.flush();
    }
}

