package reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Automate {
	

	HashSet<Integer> caracteres;
	/* Un tableau qui stocke les transitions dans etape2*/
	int[][] tab_trans_etape2;
	/* tableau qui stocke les etats initiaux dans etape2*/
	int[] tab_init_etape2;
	/* tableau qui stocke les etats finaux dans etape2*/
	int[] tab_final_etape2;
	 /*tableau qui stocke les ε-transitions dans etape2*/
	int[][] tab_epsilon_etape2;


	/* Un tableau qui stocke les transitions dans etape3 */
	int[][] tab_trans_etape3;
	/* tableau qui stocke les etats initiaux dans etape3 */
	int[] tab_init_etape3;
	/* tableau qui stocke les etats finaux dans etape3 */
	int[] tab_final_etape3;
	
	public Automate(RegExTree tree) {

		caracteres = new HashSet<Integer>();
		int nombreEtats = getNbEtatsWithEpsilon(tree);
		System.out.println("nombre d'etats total dans un automate avec ε-transitions "+nombreEtats);
		tab_trans_etape2 = new int[nombreEtats][256];
		tab_init_etape2 = new int[nombreEtats];
		tab_final_etape2 = new int[nombreEtats];
		tab_epsilon_etape2 = new int[nombreEtats][nombreEtats];
		etape2(tree,nombreEtats);
		

	}

	/**
	 * Etape 2 :
	 *  transformer un arbre en un automate fini non-deterministe avec ε-transitions selon la methode Aho-Ullman
	 * @param tree
	 */
	private void etape2(RegExTree tree, int nombreEtats) {

		int cpt = 0; // compteur pour le numero de l'etat
		cpt = construireAutomateAvecEpsilon(tree,cpt);
		/* debuger */
		System.out.print( "tab init :");
		for(int a :tab_init_etape2) 
			System.out.print(a);
		System.out.print( "\n tab_final :");

		for(int a :tab_final_etape2) 
			System.out.print(a);
		System.out.print( "\n  tab_epsilon :");
		for(int i =0;i<nombreEtats;i++) {
			for(int j=0;j<nombreEtats; j++) {
				if(tab_epsilon_etape2[i][j]==1)
					System.out.print("["+ i+ "]["+ j + "]= "+ tab_epsilon_etape2[i][j]+"  ");
			}
		}
		System.out.print( "\n  tab_trans :");
		for(int i =0; i<nombreEtats;i++)
			for(int j=0; j<256;j++)
				if(tab_trans_etape2[i][j]!=0) {
					System.out.print("["+ i+ "]["+ j + "]= "+ + tab_trans_etape2[i][j]+"  ");
				}
	}

	/**
	 * Etape 3 :
	 * transformer en un automate fini deterministe avec la methode des sous-ensembles
	 * automate déterministe (DFA)
	 */
	public void etape3(int nombreEtatsEtape2) {
		int numGroupe = 0;
		HashMap<Integer,ArrayList<Integer>> renommage = new HashMap<Integer,ArrayList<Integer>>();
		ArrayList<Integer> groupeInitial = new ArrayList<Integer>();
		for(int i  = 0 ; i < nombreEtatsEtape2;i++) {
			if(tab_init_etape2[i]==1) {
				groupeInitial.add(i);
			}
		}
		for(int init : groupeInitial) {
			for(int i  = 0 ; i < nombreEtatsEtape2;i++) {
				if(tab_epsilon_etape2[init][i]==1) {
					groupeInitial.add(i);
				}
			}
		}
		
		renommage.put(numGroupe, groupeInitial);
		

	}


	/**
	 * Etape 4 :
	 * transformer en un automate equivalent avec un nombre minimum d’etats
	 * automate déterministe minimum (DFA, minimum)
	 */
	public void etape4() {

	}

	/**
	 * En etape 2
	 * Construire un automate fini non-deterministe avec ε-transitions selon la methode Aho-Ullman
	 * @param tree
	 * @param cpt : le numero du premier etat
	 * @return renvoyer le compteur 
	 */
	private  int construireAutomateAvecEpsilon(RegExTree tree,int cpt) {

		int root = tree.root;

		if(root == RegEx.CONCAT) {

			int deuxieme = construireAutomateAvecEpsilon(tree.subTrees.get(0),cpt);
			tab_epsilon_etape2[deuxieme-1] [deuxieme] = 1 ;
			tab_final_etape2[deuxieme-1] = 0;
			cpt = construireAutomateAvecEpsilon(tree.subTrees.get(1),deuxieme);
			tab_init_etape2[deuxieme] = 0;

		} else if(root == RegEx.ALTERN ) {

			/*On ajoute 4 ε-transitions total, et on augmente le compteur de 2*/
			int debut = cpt;
			int premier = cpt + 1 ;
			int fin = construireAutomateAvecEpsilon(tree.subTrees.get(0),premier);
			int deuxieme = fin+1;
			cpt = construireAutomateAvecEpsilon(tree.subTrees.get(1),deuxieme);
			tab_final_etape2[fin] = 1;
			tab_init_etape2[debut] = 1 ;
			tab_epsilon_etape2[debut][premier] = 1;
			tab_epsilon_etape2[debut][deuxieme] = 1;
			tab_epsilon_etape2[fin-1][fin] = 1;
			tab_epsilon_etape2[cpt-1][fin] = 1;
			tab_init_etape2[premier] = 0 ;
			tab_init_etape2[deuxieme] = 0 ;
			tab_final_etape2[fin-1] = 0;
			tab_final_etape2[cpt-1] = 0;
			cpt++;

		}else if(root == RegEx.ETOILE) {

			/*On ajoute 4 ε-transitions total, et on augmente le compteur de 2*/
			tab_init_etape2[cpt] = 1;
			cpt++;
			int cpt_debut = cpt;
			tab_epsilon_etape2[cpt_debut-1] [cpt_debut] = 1;
			cpt = construireAutomateAvecEpsilon(tree.subTrees.get(0),cpt_debut);
			tab_final_etape2[cpt-1] = 0;
			tab_init_etape2[cpt_debut] = 0;
			tab_epsilon_etape2[cpt-1][cpt_debut] = 1; 
			tab_epsilon_etape2[cpt_debut-1][cpt] = 1;
			tab_epsilon_etape2[cpt-1] [cpt] = 1;
			tab_final_etape2[cpt] = 1;
			cpt++;

		}else{/*root == RegEx.DOT*/
			tab_trans_etape2[cpt][root] = cpt + 1;
			tab_init_etape2[cpt] = 1;
			tab_final_etape2[cpt+1] = 1;
			cpt = cpt+2;
		}
		return cpt;
	}
	


	/**
	 * Obtenir le nombre d'etats total dans un automate avec ε-transitions
	 * @param tree
	 * @return
	 */
	
	private static int getNbEtatsWithEpsilon(RegExTree tree) {

		int nombre = 0;
		int root = tree.root;
		if (root == RegEx.CONCAT) {
			//faire rien
		}else if(root == RegEx.ETOILE) {
			nombre +=2;
		}else if(root == RegEx.ALTERN) {
			nombre+=2;
		}else if (root == RegEx.DOT) {
			nombre +=2;
		}else {
			/*quand c'est un caractere*/ 
			nombre +=2;
		}
		if(!tree.subTrees.isEmpty()) {
			for(RegExTree arbre:tree.subTrees) {
				nombre+=getNbEtatsWithEpsilon(arbre);
			}
		}
		return nombre;
	}


}

