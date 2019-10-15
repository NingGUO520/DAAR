package kmp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class Matching {



	/**
	 * Calculer le tableau de retenue a partir d'un facteur
	 * @param facteur
	 * @return le tableau de retenue
	 */
	public int[] calculerTabRetenue(char[] facteur) {
		int[] retenue= new int[facteur.length+1];
		//calculer le premier char
		char first_char = facteur[0];
		for(int i = 0; i< facteur.length;i++) {
			if(facteur[i] == first_char) {
				retenue[i]=-1;
			}
		}

		//on regarde si il y a un suffix qui est un prefix
		int i = 0;
		while(i<facteur.length) {
			i++;
			if(retenue[i]!=-1) {
				if(i-1>0) {
					char[] suffix = new char[i-1];
					int index = 0;
					for(int a = 1;a<i;a++) {
						suffix[index] = facteur[a];
						index++;
					}
					retenue[i] = estPrefix(facteur,suffix);
					//optimisation
					if(retenue[i]!=0 && retenue[i]!= -1 && i< facteur.length) {
						if(facteur[i] == facteur[retenue[i]] ) {
							retenue[i] = retenue[retenue[i]];
						}
					}

				}else {
					retenue[i] = 0;
				}

			}
		}		
		retenue[facteur.length] = 0;
		return retenue;

	}

	public int estPrefix(char[] facteur,char[] suffix) {
		int j = 0;
		int i = 0;
		while(i<suffix.length) {
			if(suffix[i] == facteur[j]) {
				if(i == suffix.length-1) {
					return j+1;
				}
				i++;
				j++;

			}else {
				i++;
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param facteur : le motif a rechercher 
	 * @param retenue : le tableau de retenue qu'on a calcule a l'etape precedent
	 * @param texte
	 * @return index du premier lettre du mot recherche
	 * 		    -1 si le facteur n'est pas trouve
	 */
	public int matchingAlgo(char[] facteur, int[]retenue, char[] texte ) {
		int i = 0,j=0;
		while(i<texte.length) {
			if(texte[i] == facteur[j] ) {
				i++;
				j++;
				if(j== facteur.length-1) {
					return i-facteur.length+1;
				}
			}
			else {
				if(retenue[j] == -1) {
					i++;
					j=0;
				}else {
					j= retenue[j];
				}
			}
		}
		/* si le facteur n'est pas trouve dans ce texte */		
		return -1;
	}

	public static void main(String[] args) {

		Matching matching = new Matching();
		char[] facteur = {'m','a','m','a','m','i','a'};

		String nomFichier ;
		String motif ;
		if (args.length ==2) {
			motif = args[0];
			nomFichier = args[1];
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("  >> Please enter a factor: ");
			motif = scanner.next();
			System.out.print("  >> Please enter file name: ");
			nomFichier = scanner.next();
		}
		System.out.println("  >> Search factor \""+motif+"\".");
		System.out.println("  >> ...");
		facteur = motif.toCharArray();
		
		/* Etape 1 : calculer le tableau de retenue*/
		int[] retenue =  matching.calculerTabRetenue(facteur);

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(nomFichier));
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				char[] texte = ligne.toCharArray();
//				System.out.println("texte length : " + texte.length);
//				System.out.println("facteur length : " + facteur.length);
//				
				/* Etape 2 : rechercher le motif dans le texte*/
				int res = matching.matchingAlgo(facteur,retenue,texte);
				if(res!=-1) {
					System.out.println(ligne);
//					System.out.println("le mot a trouvé à index : " + res);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//			System.out.println("tableau de retenue: " );
		//			for(int a:retenue) {
		//				System.out.print(a);
		//				System.out.print(',');
		//
		//			}
		//			System.out.println();

	}

}
