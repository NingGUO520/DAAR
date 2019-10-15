package rank;

public class Rank_damping {
  private static int ITERATIONS=30;
  private static double DAMPING=.85;
  public Rank_damping(){}
  public static void main(String[] args){
    double tab[][] = new double[5][5];
    tab[0]=new double[] {0,.25,0,5/(double)9,1};
    tab[1]=new double[] {2/(double)7,0,0,4/(double)9,0};
    tab[2]=new double[] {1/(double)7,0,0,0,0};
    tab[3]=new double[] {1/(double)7,.75,0,0,0};
    tab[4]=new double[] {3/(double)7,0,1,0,0};
    for (int i=0;i<5;i++) {
      for (int j=0;j<5;j++) System.out.print(tab[i][j]+" ");
      System.out.println();
    }
    System.out.println();
    System.out.println();

    double v[] = new double[] {.2,.2,.2,.2,.2};
    double vNext[] = new double[] {.2,.2,.2,.2,.2};

    for (int r=0;r<ITERATIONS;r++) {
      for (int i=0;i<5;i++) System.out.print(v[i]+" ");
      System.out.println();
      for (int i=0;i<5;i++) {
        vNext[i]=0;
        for (int j=0;j<5;j++) {
          vNext[i]+=DAMPING*v[j]*tab[i][j];
        }
        vNext[i]+=(1-DAMPING)*.2;
      }
      for (int i=0;i<5;i++) { v[i]=vNext[i]; }
    }

    for (int i=0;i<5;i++) System.out.print(v[i]+" ");
    System.out.println();
  }
}
