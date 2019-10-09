package radix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Test {

	public class RadixTree{

		String chars;
		ArrayList<RadixTree> fils ;
		public RadixTree() {
			chars = null;
			fils = new ArrayList<RadixTree>();
		}

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
			boolean prefixCommun = false;
			for(int i = 0; i< arbre.fils.size();i++) {
				RadixTree fil = arbre.fils.get(i);
				if(estPrefix(mot,fil.chars)){
					insertion(fil,mot.substring(fil.chars.length()));
					prefixCommun = true;
				}
			}
			if(!prefixCommun) {
				RadixTree nouvelle = new RadixTree(mot);
				arbre.fils.add(nouvelle);
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

	public RadixTree construireTree(HashMap<String,Coordonnees> mapIndex) {
		RadixTree tree = new RadixTree();
		for(Entry<String,Coordonnees> e :mapIndex.entrySet()) {
			String mot = e.getKey();
			insertion(tree,mot);
		}

		return tree;
	}

	public HashMap<String,Coordonnees> lireTexte(String nomFichier) {

		HashMap<String,Coordonnees> mapIndex = new HashMap<String,Coordonnees>();
		ArrayList<String> lignes = new ArrayList<String>();
		String ligne;
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(nomFichier));
			while ((ligne = reader.readLine()) != null) {
				lignes.add(ligne);
			}
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for(int i = 0 ;i< lignes.size();i++) {
			String s = lignes.get(i);
			int j = 0;
			String mot ="";
			int debut = 1;
			while(j<s.length()) {
				char c = s.charAt(j);
				int n = (int)c;
				if((n>=97 && n<=122) || (n>=224 && n<=252)
						|| (n>=65 && n<=90 ) || n == 39 || n == 45) {
					mot+=c;
					if(j == s.length()-1) {
						if(mapIndex.containsKey(mot)) {
							mapIndex.get(mot).addCoord(i+1, debut);
						}else {
							Coordonnees coord = new Coordonnees(i+1,debut);
							mapIndex.put(mot,coord );
						}
						mot ="";
					}
				}else {
					if(mot.length()>0 ) {
						if(mapIndex.containsKey(mot)) {
							mapIndex.get(mot).addCoord(i+1, debut);
						}else {
							Coordonnees coord = new Coordonnees(i+1,debut);
							mapIndex.put(mot,coord );
						}
						mot ="";
						debut = j+2;
					}

				}
				j++;
			}
		}
		return mapIndex;
	}

	public static void main(String[] args) {
		Test t = new Test();

		HashMap<String,Coordonnees> mapIndex = t.lireTexte("test.txt");
		for(Entry<String,Coordonnees> e :mapIndex.entrySet()) {

			System.out.println(e.getKey()+e.getValue().toString());
		}
		RadixTree tree = t.construireTree(mapIndex);
		
		String motAChercher = "ok";
		if(t.rechercher(tree,motAChercher )){
		System.out.print(mapIndex.get(motAChercher).toString());
		}else {
			System.out.print("ce mot n'existe pas dans ce texte");
		}
		


	}

}
