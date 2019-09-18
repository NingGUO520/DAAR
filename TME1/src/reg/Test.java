package reg;

public class Test {

	
	public static void modif_tab(int t[]) {
		
		t[8]=1;
		
	}
	public static void main(String[] args) {
			int tab[] = new int[10];
			modif_tab(tab);
			for(int a :tab) 
			System.out.println(a);
	}

}
