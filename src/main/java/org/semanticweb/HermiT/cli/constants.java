package org.semanticweb.HermiT.cli;

class constants {
    protected static final int kTime = 1000;
    protected static final int kDumpClauses = 1001;
    protected static final int kDumpRoleBox = 1002;
    protected static final int kDirectBlock = 1003;
    protected static final int kBlockStrategy = 1004;
    protected static final int kBlockCache = 1005;
    protected static final int kExpansion = 1006;
    protected static final int kBase = 1007;
    protected static final int kParser = 1008;
    protected static final int kDefaultPrefix = 1009;
    protected static final int kDumpPrefixes = 1010;
    protected static final int kTaxonomy = 1011;
    protected static final int kIgnoreUnsupportedDatatypes = 1012;
    protected static final int kPremise = 1013;
    protected static final int kConclusion = 1014;
    protected static final int kNoInconsistentException = 1015;
    protected static final String versionString = constants.versionString();
    protected static final String usageString = "Usage: hermit [OPTION]... IRI...";
    protected static final String[] helpHeader = new String[]{"Perform reasoning on each OWL ontology IRI.", "Example: java -jar Hermit.jar -dsowl:Thing http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl", "    (prints direct subclasses of owl:Thing within the pizza ontology)", "Example: java -jar Hermit.jar --premise=http://km.aifb.uni-karlsruhe.de/projects/owltests/index.php/Special:GetOntology/New-Feature-DisjointObjectProperties-002?m=p --conclusion=http://km.aifb.uni-karlsruhe.de/projects/owltests/index.php/Special:GetOntology/New-Feature-DisjointObjectProperties-002?m=c --checkEntailment", "    (checks whether the conclusion ontology is entailed by the premise ontology)", "", "Both relative and absolute ontology IRIs can be used. Relative IRIs", "are resolved with respect to the current directory (i.e. local file", "names are valid IRIs); this behavior can be changed with the '--base'", "option.", "", "Classes and properties are identified using functional-syntax-style", "identifiers: names not containing a colon are resolved against the", "ontology's default prefix; otherwise the portion of the name", "preceding the colon is treated as a prefix prefix. Use of", "prefixes can be controlled using the -p, -N, and --prefix", "options. Alternatively, classes and properties can be identified with", "full IRIs by enclosing the IRI in <angle brackets>.", "", "By default, ontologies are simply retrieved and parsed. For more", "interesting reasoning, set one of the -c/-k/-s/-S/-e/-U options."};
    protected static final String[] footer = new String[]{"HermiT is a product of Oxford University.", "Visit <http://hermit-reasoner.org/> for details."};
    protected static final String kMisc = "Miscellaneous";
    protected static final String kActions = "Actions";
    protected static final String kParsing = "Parsing and loading";
    protected static final String kPrefixes = "Prefix name and IRI";
    protected static final String kAlgorithm = "Algorithm settings (expert users only!)";
    protected static final String kInternals = "Internals and debugging (unstable)";

    constants() {
    }

    static String versionString() {
        String version = CommandLine.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "<no version set>";
        }
        return version;
    }
}

