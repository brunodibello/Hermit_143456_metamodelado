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
	
	//Equal Rule
	public void testAccountingConsistente1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/AccountingConsistente1.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingConsistente1 es consistente");
		
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
	
	public void testAccountingInconsistente2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/AccountingInconsistente2.owl");
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
	
	public void testF1() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality1.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 1 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality3.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 3 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF5() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality5.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 5 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF7() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality7.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 7 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF11() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality11.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 11 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF13() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality13.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 13 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF17() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality17.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 17 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testF19() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioF/TestEquality19.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("Escenario 19 es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
	
	public void testE2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality2.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 2 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE4() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality4.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 4 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE6() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality6.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 6 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE8() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality8.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 8 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE10() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality10.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 10 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE12() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality12.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 12 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE14() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality14.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 14 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE15() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality15.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 15 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE16() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality16.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 16 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testE18() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"EscenarioE/TestEquality18.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("Escenario 18 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	//Not Equal Rule
	public void testTestDifference2() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"NotEqual/TestDifference2.owl");
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
	
	public void testAccountingConsistente3() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"NotEqual/AccountingConsistente3.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("AccountingConsistente3 es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	//Close Rule
	public void testCloseRuleConsistente() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"closerule/closeruleCon.owl");
		
		cl.main(flags.toArray(new String[flagsCount+1]));
		System.out.println("closeruleCon es consistente");
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, true);
	}
	
	public void testCloseRuleInconsistente() {
		CommandLine cl = new CommandLine();
		flags.add(testCasesPath+"closerule/closeruleInc.owl");
		boolean result = false;
		try {
			cl.main(flags.toArray(new String[flagsCount+1]));
		}catch (InconsistentOntologyException e) {
			System.out.println("closeruleInc es inconsistente");
			result = true;
		}
		
		//remover la flag del escenario
		flags.remove(flagsCount);
		TestCase.assertEquals(true, result);
	}
}
