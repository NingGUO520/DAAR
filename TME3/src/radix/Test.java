package radix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Test {

	public class RadixTree{



		String chars;
		ArrayList<RadixTree> fils ;
		public RadixTree(String lettres) {
			chars = lettres;
			fils = new ArrayList<RadixTree>();
		}
		public ArrayList<RadixTree> getSousArbre(){
			return fils;
		}
		public String getKeys() {
			return chars;
		}

		public boolean est_feuille() {
			return fils.isEmpty();

		}
		

	}
	public class Pair{
		int ligne;
		int col;
		public Pair(int l,int c) {
			ligne=l;
			col = c;
		}

	}
	public class Coordonnees{
		ArrayList<Pair> coords;

		public Coordonnees(int l, int c) {
			coords= new ArrayList<Pair>();
			Pair pair = new Pair(l,c);
			coords.add(pair);

		}
		public void addCoord(int l, int c) {
			Pair pair = new Pair(l,c);
			coords.add(pair);
		}
		public String toString() {
			String r = "";
			for(Pair p : coords) {
				r+=" ("+ p.ligne+","+p.col+") ";
			}
			return r;
		}
	}

	public boolean estPrefix(String mot,String pre) {
		if(pre.length()>mot.length()) {
			return false;
		}
		for(int i = 0; i<pre.length();i++) {
			if(pre.charAt(i)!=mot.charAt(i)) {
				return false;
			}
		}

		return true;
	}
	public void insertion(RadixTree arbre, String mot) {
		if(arbre.est_feuille()) {
			if(!mot.isEmpty()) {
				RadixTree nouvelle = new RadixTree(mot);
				arbre.fils.add(nouvelle);
			}
		}else {
			for(RadixTree fil:arbre.fils) {
				if(estPrefix(mot,fil.chars)){
					 insertion(fil,mot.substring(fil.chars.length()));
				}
			}
		}
		

	}

	public boolean rechercher(RadixTree arbre, String motif) {
		if(arbre.est_feuille() && motif.isEmpty()) {
			return true;
		}else if(motif.isEmpty() || arbre.est_feuille()) {
			return false;
		}else {
			for(RadixTree fil:arbre.fils) {
				if(estPrefix(motif,fil.chars)){
					return rechercher(fil,motif.substring(fil.chars.length()));
				}
			}

		}

		return false;

	}
	public void lireTexte() {
		HashMap<String,Coordonnees> mapIndex = new HashMap<String,Coordonnees>();

		String s = "bonjour, je m'appelle Jean-jacque , bonjour";
		System.out.println("substring"+s.substring(3));
		int i = 0;
		String mot ="";
		int debut = 0;
		while(i<s.length()) {


			char c = s.charAt(i);
			int n = (int)c;
			if((n>=97 && n<=122)
					|| (n>=65 && n<=90 ) || n == 39 || n == 45) {
				mot+=c;
				if(i == s.length()-1) {
					if(mapIndex.containsKey(mot)) {
						mapIndex.get(mot).addCoord(1, debut);
					}else {
						Coordonnees coord = new Coordonnees(1,debut);
						mapIndex.put(mot,coord );
					}
				}


			}else {
				if(mot.length()>0 ) {
					if(mapIndex.containsKey(mot)) {
						mapIndex.get(mot).addCoord(1, debut);
					}else {
						Coordonnees coord = new Coordonnees(1,debut);
						mapIndex.put(mot,coord );
						mot ="";
					}

				}
				debut = i+1;
			}
			i++;

		}

		for(Entry<String,Coordonnees> e :mapIndex.entrySet()) {

			System.out.println(e.getKey()+e.getValue().toString());
		}
	}
	public static void main(String[] args) {
		Test t = new Test();
		t.lireTexte();


	}

}
