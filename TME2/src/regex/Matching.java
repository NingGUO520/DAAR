package regex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Matching {



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
		return -1;



	}
	public static void main(String[] args) {


		String nomFichier = "60341-0.txt";
		
		Matching matching = new Matching();
		char[] facteur = {'m','a','m','a','m','i','a'};
		char[] facteur2 = {'c','h','i','c','h','a'};
		char[] facteur3 = {'S','a','r','g','o','n'};
		char[] facteur4 = {'c','h','i','c','h','i','c','h','a'};

//		
//		try {
//			FileInputStream flux =new FileInputStream(nomFichier); 
//			ObjectInputStream lecture = new ObjectInputStream(flux);
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		int[] retenue =  matching.calculerTabRetenue(facteur4);
		System.out.println("tableau de retenue: " );
		for(int a:retenue) {
			System.out.print(a);
			System.out.print(',');

		}
		System.out.println();
		String texte = "abcdefgmamamiaabcdefg";
		char[] chars = texte.toCharArray();
		System.out.println("texte length : " + chars.length);
		System.out.println("facteur length : " + facteur.length);

		int res = matching.matchingAlgo(facteur,retenue,chars);

		System.out.println("le mot a trouvé à index : " + res);

	}

}
