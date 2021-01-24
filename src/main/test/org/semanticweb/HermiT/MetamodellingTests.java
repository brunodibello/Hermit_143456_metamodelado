package org.semanticweb.HermiT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.HermiT.cli.CommandLine;
import org.semanticweb.HermiT.reasoner.AnyURITest;
import org.semanticweb.HermiT.reasoner.BinaryDataTest;
import org.semanticweb.HermiT.reasoner.ComplexConceptTest;
import org.semanticweb.HermiT.reasoner.DatalogEngineTest;
import org.semanticweb.HermiT.reasoner.DatatypesTest;
import org.semanticweb.HermiT.reasoner.DateTimeTest;
import org.semanticweb.HermiT.reasoner.EntailmentTest;
import org.semanticweb.HermiT.reasoner.FloatDoubleTest;
import org.semanticweb.HermiT.reasoner.NumericsTest;
import org.semanticweb.HermiT.reasoner.OWLReasonerTest;
import org.semanticweb.HermiT.reasoner.RDFPlainLiteralTest;
import org.semanticweb.HermiT.reasoner.RIARegularityTest;
import org.semanticweb.HermiT.reasoner.ReasonerCoreBlockingTest;
import org.semanticweb.HermiT.reasoner.ReasonerIndividualReuseTest;
import org.semanticweb.HermiT.reasoner.ReasonerTest;
import org.semanticweb.HermiT.reasoner.RulesTest;
import org.semanticweb.HermiT.reasoner.SimpleRolesTest;
import org.semanticweb.HermiT.reasoner.XMLLiteralTest;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MetamodellingTests extends TestCase {

	String testCasesPath;
	List<String> flags;
	int flagsCount;
	
	protected void setUp() {
		testCasesPath = "ontologias/";
		flags = new ArrayList<String>();
		flags.add("-c");
		flagsCount = 1;
	}
	
	//COMIENZO - Escenario C - Casos consistentes sin metamodelling
	
	public void testEntrega3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioC/Entrega3.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Entrega3 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testFiltroInfantil() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioC/FiltroInfantil.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("FiltroInfantil es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testHidrografia() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioC/Hidrografia.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Hidrografia es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testObjetosHidrograficos() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioC/ObjetosHidrograficos.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("ObjetosHidrograficos es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testEquality20() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioC/TestEquality20.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality20 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	//FIN - Escenario C - Casos consistentes sin metamodelling
	
	//COMIENZO - Escenario D - Casos inconsistentes sin metamodelling
	
	public void testAccountingInconsistente2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioD/AccountingInconsistente2.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("AccountingInconsistente2 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testFIFA_WC() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioD/FIFA_WC.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("FIFA_WC es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testgenerations() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioD/generations.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("generations es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testOntologyEjercicio1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioD/OntologyEjercicio1.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("OntologyEjercicio1 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testWC_2014() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioD/WC_2014.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("WC_2014 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	//FIN - Escenario D - Casos inconsistentes sin metamodelling
	
	//COMIENZO - Escenario E - Casos consistentes con metamodelling (SHIQM)
	
	public void testAccountingConsistente1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/AccountingConsistente1.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingConsistente1 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testAccountingConsistente1Corta() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/AccountingConsistente1Corta.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingConsistente1Corta es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testAccountingConsistente2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/AccountingConsistente2.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingConsistente2 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}

	public void testAccountingConsistente3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/AccountingConsistente3.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingConsistente3 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality2.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality2 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality4() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality4.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality4 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality6() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality6.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality6 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality8() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality8.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality8 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality10() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality10.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality10 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality12() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality12.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality12 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality14() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality14.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality14 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality15() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality15.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality15 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality16() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality16.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality16 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestEquality18() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality18.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestEquality18 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	//FIN - Escenario E - Casos consistentes con metamodelling (SHIQM)
	
	//COMIENZO - Escenario F - Casos inconsistentes con metamodelling (SHIQM)
	
	public void testAccountingInconsistente1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/AccountingInconsistente1.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("AccountingInconsistente1 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testAccountingInconsistente3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/AccountingInconsistente3.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("AccountingInconsistente3 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCycles1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestCycles1.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCycles1 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCycles2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestCycles2.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCycles2 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCycles3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestCycles3.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCycles3 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestDifference2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestDifference2.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestDifference2 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestDifference4() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestDifference4.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestDifference4 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestDifference8() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestDifference8.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestDifference8 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestDifference10() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestDifference10.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestDifference10 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestDifference11() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestDifference11.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestDifference11 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestDifference13() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestDifference13.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestDifference13 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality1.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality1 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality3.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality3 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality5() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality5.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality5 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality7() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality7.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality7 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality9() {
		for (int i=0; i<100; i++) {
			CommandLine cl = new CommandLine();
			flags.add(testCasesPath+"EscenarioF/TestEquality9.owl");
			boolean result = false;
			try {
				cl.main(flags.toArray(new String[flagsCount+1]));
			}catch (InconsistentOntologyException e) {
				System.out.println("TestEquality9 es inconsistente");
				result = true;
			}
			
			//remover la flag del escenario
			flags.remove(flagsCount);
			TestCase.assertEquals(true, result);
		}
	}
	
	public void testTestEquality11() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality11.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality11 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality13() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality13.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality13 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality17() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality17.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality17 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality19() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality19.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality19 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality21() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality21.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality21 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality22() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality22.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality22 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestEquality23() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality23.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestEquality23 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	//FIN - Escenario F - Casos inconsistentes con metamodelling (SHIQM)
	
	//COMIENZO - Escenario G - Casos consistentes con metamodelling (SHIQM*)
	
	public void testAccountingCons1CortaRule() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioG/AccountingCons1CortaRule.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingCons1CortaRule es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestCaseG1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioG/TestCaseG1.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestCaseG1 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestCaseG2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioG/TestCaseG2.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestCaseG2 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestCaseG3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioG/TestCaseG3.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestCaseG3 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestCaseG4() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioG/TestCaseG4.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestCaseG4 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testTestCaseG5() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioG/TestCaseG5.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("TestCaseG5 es consistente");
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	//FIN - Escenario G - Casos consistentes con metamodelling (SHIQM*)
	
	//COMIENZO - Escenario H - Casos inconsistentes con metamodelling (SHIQM*)
	
	public void testTestCaseH1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioH/TestCaseH1.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCaseH1 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCaseH2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioH/TestCaseH2.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCaseH2 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCaseH3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioH/TestCaseH3.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCaseH3 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCaseH4() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioH/TestCaseH4.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCaseH4 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testTestCaseH5() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioH/TestCaseH5.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("TestCaseH5 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	//FIN - Escenario H - Casos inconsistentes con metamodelling (SHIQM*)
	
	//COMIENZO - otras
	
	//FIN - otras
	
}
