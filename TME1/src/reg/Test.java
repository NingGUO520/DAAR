package reg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import reg.Automate.Pair;

public class Test {

	HashSet<Integer> caracteres;
	int nombreEtatsEtape2;
	int nombreEtatsEtape3;
	int nombreEtatsEtape4;

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


	/* les tableaux d'etape4 */
	int[][] tab_trans_etape4;
	int[] tab_init_etape4;
	int[] tab_final_etape4;

	public void etape4() {


		// Un tableau qui represente une partition des etats
		// on initialise les etats terminaux avec numero de groupe 1, les restes a numero de groupe 0	
		int[] P = new int[nombreEtatsEtape3];
		for(int i = 0; i<nombreEtatsEtape3;i++ ) {
			if(tab_final_etape3[i]==1) {
				P[i]=1;
			}
		}

		//Deux états sont dans un même ensemble s'ils étaient déjà dans le même ensemble
		//et si les transitions mènent dans les mêmes ensembles.
		int nombreCaracteres = caracteres.size();
		ArrayList<Integer> listCaractetres = new ArrayList<Integer>();
		listCaractetres.addAll(caracteres);
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
			boolean doitSeparer = false;
			ArrayList<Integer> etats = getEtatsDeGroupe(numeroGroupe,P);
			if(etats.size() == 1) continue;

			for(int j=0;j<nombreCaracteres;j++) {

				int tmp = tableau[etats.get(0)][j];
				for(int i=1;i < etats.size();i++ ) {
					if(tableau[etats.get(0)][j]==tmp) doitSeparer = true;

				}


			}



			numeroGroupe++;
		}
	}
	
	public static void modif_tab(int t[]) {
		
		t[8]=1;
		
	}
	public class Pair  {
		int p1;
		int p2;
		public Pair(int p1, int p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
		@Override
		public boolean equals(Object obj) 
		{
			 if (!(obj instanceof Pair))
		            return false;
			Pair pair = (Pair)obj;
			if(this.p1 == pair.p1 && this.p2 == pair.p2) return true;
			return false;

		}
		 @Override
		    public int hashCode() {
		        return Objects.hash(p1,p2);
		    }
	}
	public void test() {
//		HashSet<Integer> P = new HashSet<Integer>();
//		P.add(3);
//		P.add(4);
//		P.add(5);
//		P.add(6);
//		
//		
//		HashSet<Integer> P2 = new HashSet<Integer>();
//		P2.add(6);
//		P2.add(5);
//		P2.add(4);
//		P2.add(3);
		HashSet<Pair> P = new HashSet<Pair>();
//
		Pair p1 = new Pair(1,2);
		Pair p2 = new Pair(1,2);
//
		System.out.println(p1.equals(p2));
//
		P.add(p1);
		System.out.println(P.contains(p2));

//		P.add(new Pair(1,2));
//
//		for(Pair p: P) {
//			System.out.println(p.p1+ " "+p.p2);
//		}
	}

	public static void main(String[] args) {
		Test t = new Test();
		t.test();
//			int tab[] = new int[10];
//			modif_tab(tab);
//			for(int a :tab) 
//			System.out.println(a);
	}

}
