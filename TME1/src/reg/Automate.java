package reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class Automate {


	HashSet<Integer> caracteres;
	int nombreEtatsEtape2;
	int nombreEtatsEtape3;

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
	//	int[][] tab_trans_etape3_inverse;

	public Automate(RegExTree tree) {

		caracteres = new HashSet<Integer>();
		nombreEtatsEtape2 = getNbEtatsWithEpsilon(tree);
		System.out.println("nombre d'etats total dans un automate avec ε-transitions "+nombreEtatsEtape2);
		System.out.println("nombre de caractere "+caracteres.size());


		tab_trans_etape2 = new int[nombreEtatsEtape2][256];
		//		Initialiser le tableau de transition a -1
		for(int i = 0;i<nombreEtatsEtape2;i++)
			for(int j= 0;j<256;j++)
				tab_trans_etape2[i][j]=-1;

		tab_init_etape2 = new int[nombreEtatsEtape2];
		tab_final_etape2 = new int[nombreEtatsEtape2];
		tab_epsilon_etape2 = new int[nombreEtatsEtape2][nombreEtatsEtape2];
		etape2(tree);

		tab_trans_etape3 = new int[nombreEtatsEtape2][256];
		//		tab_trans_etape3_inverse = new int[nombreEtatsEtape2][256];

		//		Initialiser le tableau de transition a -1
		for(int i = 0;i<nombreEtatsEtape2;i++) {
			for(int j= 0;j<256;j++) {
				tab_trans_etape3[i][j]=-1;
				//				tab_trans_etape3_inverse[i][j]=-1;
			}
		}
		etape3();

		/* debuger */
		System.out.print( "tab init :");
		for(int a :tab_init_etape3) 
			System.out.print(a);
		System.out.print( "\n tab_final :");
		for(int a :tab_final_etape3) 
			System.out.print(a);

		System.out.print( "\n  tab_trans :");
		for(int i =0; i<nombreEtatsEtape2;i++)
			for(int j=0; j<256;j++)
				if(tab_trans_etape3[i][j]!=-1) {
					System.out.print("["+ i+ "]["+ j + "]= "+ + tab_trans_etape3[i][j]+"  ");
				}

		//Calculer le tableau inverse de transitions
		//		for(int i = 0;i<nombreEtatsEtape2;i++) {
		//			for(int j= 0;j<256;j++) {
		//				if(tab_trans_etape3[i][j]!=-1) {
		//					int v = tab_trans_etape3[i][j];
		//					tab_trans_etape3_inverse[v][j]=i;
		//				}
		//
		//			}
		//		}

		for(int a :caracteres) {
			System.out.println("caractere = "+ a);
		}

	}

	/**
	 * Etape 2 :
	 *  transformer un arbre en un automate fini non-deterministe avec ε-transitions selon la methode Aho-Ullman
	 * @param tree
	 */
	private void etape2(RegExTree tree) {

		int cpt = 0; // compteur pour le numero de l'etat
		cpt = construireAutomateAvecEpsilon(tree,cpt);
		//		/* debuger */
		//		System.out.print( "tab init :");
		//		for(int a :tab_init_etape2) 
		//			System.out.print(a);
		//		System.out.print( "\n tab_final :");
		//
		//		for(int a :tab_final_etape2) 
		//			System.out.print(a);
		//		System.out.print( "\n  tab_epsilon :");
		//		for(int i =0;i<nombreEtatsEtape2;i++) {
		//			for(int j=0;j<nombreEtatsEtape2; j++) {
		//				if(tab_epsilon_etape2[i][j]==1)
		//					System.out.print("["+ i+ "]["+ j + "]= "+ tab_epsilon_etape2[i][j]+"  ");
		//			}
		//		}
		//		System.out.print( "\n  tab_trans :");
		//		for(int i =0; i<nombreEtatsEtape2;i++)
		//			for(int j=0; j<256;j++)
		//				if(tab_trans_etape2[i][j]!=-1) {
		//					System.out.print("["+ i+ "]["+ j + "]= "+ + tab_trans_etape2[i][j]+"  ");
		//				}
	}

	/**
	 * Etape 3 :
	 * transformer en un automate fini deterministe avec la methode des sous-ensembles
	 * automate déterministe (DFA)
	 */
	public void etape3() {

		int numGroupe = 0;
		HashMap<Integer,ArrayList<Integer>> renommage = new HashMap<Integer,ArrayList<Integer>>();
		ArrayList<Integer> groupeInitial = new ArrayList<Integer>();
		for(int i  = 0 ; i < nombreEtatsEtape2;i++) {
			if(tab_init_etape2[i]==1) {
				groupeInitial.add(i);
			}
		}
		groupeInitial = elargirEtats(groupeInitial);

		PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
		queue.add(numGroupe);
		renommage.put(numGroupe,groupeInitial);
		numGroupe++;
		while(!queue.isEmpty()) {
			int numero = queue.poll();
			ArrayList<Integer> etatsDeparts = renommage.get(numero);
			for(Integer a : caracteres) {
				ArrayList<Integer> etatsArrives = new ArrayList<Integer>();
				for(Integer depart: etatsDeparts) {
					if(tab_trans_etape2[depart][a]!=-1) {
						etatsArrives.add(tab_trans_etape2[depart][a]);
					}
				}

				etatsArrives = elargirEtats(etatsArrives);
				if(!etatsArrives.isEmpty()) {
					if(!renommage.containsValue(etatsArrives)) {
						tab_trans_etape3[numero][a] = numGroupe;
						queue.add(numGroupe);
						renommage.put(numGroupe,etatsArrives);
						numGroupe++;
					}else {
						int arrive = getKey(renommage,etatsArrives);
						tab_trans_etape3[numero][a]=arrive;
					}
				}
			}
		}
		nombreEtatsEtape3 = renommage.size();
		tab_init_etape3 = new int[nombreEtatsEtape3];
		tab_final_etape3 = new int[nombreEtatsEtape3];
		tab_init_etape3[0]=1;
		//remplir le tableau de etats finaux
		for(Entry<Integer,ArrayList<Integer>> entry : renommage.entrySet()) {
			int key = entry.getKey();
			ArrayList<Integer> value = entry.getValue();
			for(int v : value) {
				if(tab_final_etape2[v]==1) tab_final_etape3[key]=1;
			}
		}

	}


	/**
	 * Etape 4 : Minimisation d'un automate fini en utilisant Algorithme de Moore
	 * transformer en un automate equivalent avec un nombre minimum d’etats
	 */
	public void etape4() {

		int nombreCaracteres = caracteres.size();
		ArrayList<Integer> listCaractetres = new ArrayList<Integer>();
		listCaractetres.addAll(caracteres);

		// Un tableau qui represente une partition des etats
		int[] P = new int[nombreEtatsEtape3];
		for(int i = 0; i<nombreEtatsEtape3;i++ ) {
			if(tab_final_etape3[i]==1) {
				P[i]=1;
			}
		}

		//Deux états sont dans un même ensemble s'ils étaient déjà dans le même ensemble
		//et si les transitions mènent dans les mêmes ensembles.
		int numeroGroupe = 0;
		int numeroMax = getMax(P);
		int tableau[][] = new int[nombreEtatsEtape3][nombreCaracteres];
		while(numeroGroupe<=numeroMax) {
			for(int i = 0;i<nombreEtatsEtape3;i++) {
				if(P[i] == numeroGroupe ) {
					for(int c = 0; c <listCaractetres.size();c++) {
						int a = listCaractetres.get(c); 
						int arrive = tab_trans_etape3[i][a];
						if(arrive!=-1) {
							tableau[i][c] = P[arrive];
						}
					}
				}
			}

			numeroGroupe++;
		}

		//faire la repartition
		numeroGroupe = 0;
		while(numeroGroupe<=numeroMax) {
			for(int i=0;i<nombreEtatsEtape3;i++ ) {
				if(P[i]==numeroGroupe) {
					for(int j=0;j<nombreCaracteres;j++) {
						

					}

				}
			}


			numeroGroupe++;
		}
	}

	public int getMax(int[] tab) {
		int max = tab[0];
		for(int i = 1;i< tab.length;i++) {
			if(tab[i]>max) max = tab[i];
		}
		return max;
	}

	/**
	 * @param etats : le groupe d'etats a elargir
	 * @return
	 */
	public ArrayList<Integer> elargirEtats(ArrayList<Integer> etats){
		ArrayList<Integer> target = (ArrayList<Integer>) etats.clone();
		ArrayList<Integer> result = elargirEtatsBis(target);
		while(result.size()>target.size()) {
			target = (ArrayList<Integer>) result.clone();
			result = elargirEtatsBis(target);
		}

		return result;
	}

	/**
	 * On agrandit le groupe en ajoutant les etats qui sont les etats arrives par ε-transitions
	 * @param etats
	 * @return
	 */
	public ArrayList<Integer> elargirEtatsBis(ArrayList<Integer> etats){
		ArrayList<Integer> result = (ArrayList<Integer>) etats.clone();
		for(Integer etat: etats) {
			for(int i = 0;i<nombreEtatsEtape2;i++) {
				if(tab_epsilon_etape2[etat][i]==1) {
					if(!result.contains(i)) result.add(i);
				}
			}
		}

		return result;
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

	private  int getNbEtatsWithEpsilon(RegExTree tree) {

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
			caracteres.add(root);
			nombre +=2;
		}
		if(!tree.subTrees.isEmpty()) {
			for(RegExTree arbre:tree.subTrees) {
				nombre+=getNbEtatsWithEpsilon(arbre);
			}
		}
		return nombre;
	}

	public <K, V> K getKey(HashMap<K, V> map, V value) {
		for (Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

}

