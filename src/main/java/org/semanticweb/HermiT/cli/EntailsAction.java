package org.semanticweb.HermiT.cli;

import java.io.PrintWriter;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.EntailmentChecker;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
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

