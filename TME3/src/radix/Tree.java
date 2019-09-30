package radix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Tree {

	
	
	public void stocker(ArrayList<String>  lignes) {
		
		for(String ligne : lignes) {
			String [] mots = ligne.split(" ");
			for(int i = 0; i<mots.length;i++)
			map.put(mots[i], 1);
			
			
		}
		
		
	
	}
	public static ArrayList<String> getLignes(String nomFichier){

		ArrayList<String> lignes = new ArrayList<String>();
		String ligne;
		
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(nomFichier));
				  while ((ligne = reader.readLine()) != null) {
				      System.out.println(ligne);
				      lignes.add(ligne);

			    }
			    reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     
		
	  
		return lignes;
		
	}
	public static void main(String[] args) {
		Tree r = new Tree();
		ArrayList<String> lignes = Tree.getLignes("56667-0.txt");
		r.stocker(lignes);
		for(Entry<String,Integer> e : r.getMap().entrySet()) {
			System.out.println(e.getKey()+" "+e.getValue());
			
		}
	
		

	}

}
