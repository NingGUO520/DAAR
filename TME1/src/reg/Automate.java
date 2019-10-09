package reg;


public class Automate {
	/************************************ Etape 2 ************************************************/

	private static void etape2(RegExTree tree) {

		int nombreEtats = getNbEtats(tree);

		System.out.println("nombre d'etats "+nombreEtats);

		int[][] tab_trans = new int[nombreEtats][256];
		int tab_init[] = new int[nombreEtats];
		int tab_final[] = new int[nombreEtats];
		int tab_epsilon[][] = new int[nombreEtats][nombreEtats];
		int cpt = 0;
		cpt = construireAutomate(tree,tab_trans ,tab_init ,tab_final ,tab_epsilon ,cpt,nombreEtats);
		System.out.println("cpt = " +cpt);
		System.out.print( "tab init :");
		for(int a :tab_init) 
			System.out.print(a);
		System.out.print( "\n tab_final :");

		for(int a :tab_final) 
			System.out.print(a);

		System.out.print( "\n  tab_epsilon :");


		for(int i =0;i<nombreEtats;i++) {
			for(int j=0;j<nombreEtats; j++) {
				if(tab_epsilon[i][j]==1)
					System.out.print("["+ i+ "]["+ j + "]= "+ tab_epsilon[i][j]+"  ");
			}

		}


		System.out.print( "\n  tab_trans :");

		for(int i =0; i<nombreEtats;i++)
			for(int j=0; j<256;j++)
				if(tab_trans[i][j]!=0) {
					System.out.print(" i = "+ i + "j = "+j + "tab :" + tab_trans[i][j]);
				}
	}

	private static int construireAutomate(RegExTree tree,int[][] tab_trans,int tab_init[],
			int tab_final[],int tab_epsilon[][], int cpt,int nombreEtats) {
		int root = tree.root;
		if(root == RegEx.CONCAT) {


			cpt = construireAutomate(tree.subTrees.get(0),tab_trans,tab_init,tab_final,tab_epsilon,cpt, nombreEtats);

			tab_epsilon[cpt-1] [cpt] = 1 ;
			tab_final[cpt-1] = 0;
			cpt = construireAutomate(tree.subTrees.get(1),tab_trans,tab_init,tab_final,tab_epsilon,cpt,nombreEtats);



		} else if( root == RegEx.ALTERN ) {

			tab_init[cpt] = 1 ;

			int cpt_debut1 = cpt + 1 ;


			int cpt_debut2 = construireAutomate(tree.subTrees.get(0),tab_trans,tab_init,tab_final,tab_epsilon,cpt_debut1,nombreEtats);
			cpt = construireAutomate(tree.subTrees.get(1),tab_trans,tab_init,tab_final,tab_epsilon,cpt_debut2,nombreEtats);

			tab_epsilon[cpt_debut1-1][cpt_debut1] = 1;
			tab_epsilon[cpt_debut1-1][cpt_debut2] = 1;

			tab_epsilon[cpt_debut2-1][cpt] = 1;
			tab_epsilon[cpt-1][cpt] = 1;

			tab_init[cpt_debut1] = 0 ;
			tab_init[cpt_debut2] = 0 ;
			tab_final[cpt] = 1;
			tab_final[cpt_debut2-1] = 0;
			tab_final[cpt-1] = 0;


		} else if(root == RegEx.DOT) {

		} else if( root == RegEx.ETOILE) {
			tab_epsilon[cpt] [cpt+1] = 1;
			cpt++;
			int cpt_debut = cpt;
			cpt = construireAutomate(tree.subTrees.get(0),tab_trans,tab_init,tab_final,tab_epsilon,cpt,nombreEtats);
			tab_final[cpt-1] = 0;
			tab_final[cpt] = 1;
			
			tab_init[cpt_debut] = 0;
			tab_init[cpt_debut - 1] = 1;
			
			tab_epsilon[cpt-1][cpt_debut] = 1; 
			tab_epsilon[cpt-1] [cpt] = 1;

			tab_epsilon[cpt_debut-1][cpt] = 1;
		}else{
			tab_trans[cpt][root] = cpt + 1;
			tab_init[cpt] = 1;
			tab_final[cpt+1] = 1;
			System.out.println("cpt = "+cpt);
			cpt = cpt+2;
			System.out.println("cpt = "+cpt);
		}


		return cpt;
	}


	private static int getNbEtats(RegExTree tree) {

		int nombre = 0;
		int root = tree.root;
		if (root == RegEx.CONCAT) {

		}else if(root == RegEx.ETOILE) {
			nombre +=2;
		}else if(root == RegEx.ALTERN) {
			nombre+=2;
		}else if (root == RegEx.DOT) {
			nombre +=2;
		}else {
			nombre +=2;
		}
		if(!tree.subTrees.isEmpty()) {
			for(RegExTree arbre:tree.subTrees) {


				nombre+=getNbEtats(arbre);
			}
		}


		return nombre;
	}

}

