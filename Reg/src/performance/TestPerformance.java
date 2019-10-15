package performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import automate.RegEx;
import kmp.Matching;
import radix.Radix;
import radix.Radix.RadixTree;

/**
 * 
 * @author Ning GUO
 *
 */
public class TestPerformance {


	private static final String text1 = "test/56667-0.txt";

	static String[] listMotif = {"Sargon","Nebuchadnezzar","West-Semitic","Shalmaneser","approximately","Citadel"
			,"Ishtar","kingdom","Babylon","Babylonian"} ;
	
	static String[] listExpr = {"S(a|g|r)*on","Ba*o*n","W.st*.i.i*c","Sa*.eng","(a|k|l)*d"};
	
	
	public void testAutomateExprReg() {
		HashMap<String,Long> resultAutomate = new HashMap<String,Long>();
		RegEx reg = new RegEx();
		System.out.println("test de l'algorithmes des automates >>>>>>>>>");
		
		for(String expr : listExpr) {
			String []args = {expr,text1 };
			long start = System.currentTimeMillis(); 
			reg.main(args);
			long end = System.currentTimeMillis(); 

			System.out.println("le temps d'execution pour motif : "+expr+" texte = "+text1+" : " +(end-start));
			resultAutomate.put(expr, (end-start));

		}
		
		writeToFile( "result/resultAutomateExprReg.txt", resultAutomate);
	}
	
	

	public void testAutomate() {
		HashMap<String,Long> resultAutomate = new HashMap<String,Long>();
		RegEx reg = new RegEx();
		System.out.println("test de l'algorithmes des automates >>>>>>>>>");
		
		for(String motif : listMotif) {
			String []args = {motif,text1 };
			long start = System.currentTimeMillis(); 
			reg.main(args);
			long end = System.currentTimeMillis(); 

			System.out.println("le temps d'execution pour motif : "+motif+" texte = "+text1+" : " +(end-start));
			resultAutomate.put(motif, (end-start));

		}
		
		writeToFile( "result/resultAutomate.txt", resultAutomate);
		
	}

	
	public void testKmp() {
		HashMap<String,Long> resultKMP = new HashMap<String,Long>();
		System.out.println("test de l'algorithmes de KMP >>>>>>>>>");
		
		
		for(String motif : listMotif) {
			String []args = {motif,text1 };
			long start = System.currentTimeMillis(); 
			Matching.main(args);
			long end = System.currentTimeMillis(); 

			System.out.println("le temps d'execution pour motif : "+motif+" texte = "+text1+" : " +(end-start));
			resultKMP.put(motif, (end-start));

		}
		
	
		writeToFile( "result/resultKMP.txt", resultKMP);
		
	}

	
	public void testRadix() {
		HashMap<String,Long> resultRadix = new HashMap<String,Long>();
		System.out.println("test de l'algorithmes de Radix Tree >>>>>>>>>");
		Radix radix = new Radix();
		RadixTree tree = radix.genererRadixTree(text1);

		
		for(String motif : listMotif) {
			long start = System.currentTimeMillis(); 

			radix.chercherMotif(tree, motif);
			long end = System.currentTimeMillis(); 

			System.out.println("le temps d'execution pour motif : "+motif+" texte = "+text1+" : " +(end-start));
			resultRadix.put(motif, (end-start));
		}
		
		writeToFile( "result/resultRadixTree.txt", resultRadix);
		
	}
	
	public void egrep(String motif, String nomFichier) {
		
		String [] tableau={"egrep",motif,nomFichier};
		try {
			Process proc=Runtime.getRuntime().exec(tableau);
			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(proc.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = proc.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
//				System.exit(0);
			} 
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public void testEgrepExprReg() {
		HashMap<String,Long> resultEgrep = new HashMap<String,Long>();
		System.out.println("test de performance pour egrep  >>>>>>>>>");
		
		
		for(String expr : listExpr) {

			long start = System.currentTimeMillis(); 
			egrep(expr,text1);
			long end = System.currentTimeMillis(); 
			System.out.println("le temps d'execution pour motif : "+expr+" texte = "+text1+" : " +(end-start));
			resultEgrep.put(expr, (end-start));
		}
		
	
		writeToFile( "result/resultEgrepExprReg.txt", resultEgrep);
		
		
	}
	public void testEgrep() {
		HashMap<String,Long> resultEgrep = new HashMap<String,Long>();
		System.out.println("test de performance pour egrep  >>>>>>>>>");
		
		
		for(String motif : listMotif) {

			long start = System.currentTimeMillis(); 
			egrep(motif,text1);
			long end = System.currentTimeMillis(); 
			System.out.println("le temps d'execution pour motif : "+motif+" texte = "+text1+" : " +(end-start));
			resultEgrep.put(motif, (end-start));

		}
		
	
		writeToFile( "result/resultEgrep.txt", resultEgrep);
		
		
	}

	public void writeToFile(String nomFichier, HashMap<String,Long> result) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(nomFichier));
			for(Entry<String,Long> entry: result.entrySet()) {
				String ligne = entry.getKey()+"\t"+entry.getValue()+"\n";
				writer.write(ligne);
			}
			writer.close();
			System.out.println("Fin de l'ecriture du resultat >>>>>>>>");
			System.out.println("Le resultat est dans le repertoire result ");

		} catch (IOException e) {
			e.printStackTrace();
		}



	}
	
	public static void main(String[] args) {

		TestPerformance test = new TestPerformance();
		
		/*Test de performance de l'algorithme des Automates*/
		test.testAutomate();
		
		
		/*Test de performance de l'algorithme de KMP*/
		test.testKmp() ;
		
		/*Test de performance de l'algorithme de Radix Tree*/
		test.testRadix();
		
		/*Test de performance du commande shell egrep*/
		test.testEgrep();
		
		
		
		//Pour les expression regulieres
		test. testAutomateExprReg();
		test.testEgrepExprReg();

	}

	//  public void readFile() {
	//	try {
	//		URL url = new URL(adresseURL);
	//		 URLConnection urlconn = url.openConnection();
	//		 urlconn.connect();
	//            HttpURLConnection httpconn =(HttpURLConnection)urlconn;
	//            int HttpResult = httpconn.getResponseCode();
	//            if(HttpResult != HttpURLConnection.HTTP_OK) {
	//                System.out.print("Connexion erreur avec http://www.gutenberg.org/ ");
	//            } else {
	//            	
	//            }
	//	} catch (MalformedURLException e) {
	//		e.printStackTrace();
	//	} catch (IOException e) {
	//		e.printStackTrace();
	//	}
	//}

}
