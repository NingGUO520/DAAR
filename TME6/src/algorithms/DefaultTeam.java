package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultTeam {
	public Point getPlusProchePoint(Point target, int edgeThreshold, ArrayList<Point> hitPoints) {
		Point result = null;
		double min = Integer.MAX_VALUE;
		for(Point p:hitPoints) {
			if(p.equals(target)) continue;
			double d = p.distance(target);
			if(d<min) {
				min = d ;
				result = p;
			}

		}
		return result;
	}

	public ArrayList<Point> renew(ArrayList<Point> chemins){
		ArrayList<Point> result = new ArrayList<Point>();

		int x = (int)(Math.random() * chemins.size());
		int y = (int)(Math.random() * chemins.size());
		if(x>y) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		for(int i=0;i<x;i++ ) {
			result.add( chemins.get(i));
		}
		for(int i = y;i>=x;i--) {
			result.add(chemins.get(i));
		}

		for(int i = y+1;i<chemins.size();i++) {
			result.add(chemins.get(i));
		}

		//		System.out.println("size of result " + result.size());
		return result;
	}



	public  ArrayList<Point> algoPlusCourt(ArrayList<Point> hitPoints,int edgeThreshold,int i){
		ArrayList<Point> hit = (ArrayList<Point>) hitPoints.clone();
		ArrayList<Point> chemin = new ArrayList<Point>();
		Point depart = hit.get(i);
		chemin.add(depart);
		hit.remove(depart);
		Point suivant;
		while(!hit.isEmpty()) {
			suivant = getPlusProchePoint( depart,  edgeThreshold,  hit);
			chemin.add(suivant);
			hit.remove(suivant);
			depart = suivant;
		}
		return chemin;
	}

	public ArrayList<Point> calculAngularTSP(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {


		HashMap<Integer,Integer> dejaVue = new HashMap<Integer,Integer>();
		int[][] paths=new int[points.size()][points.size()];
		for (int i=0;i<paths.length;i++) for (int j=0;j<paths.length;j++) paths[i][j]=i;

		double[][] dist=new double[points.size()][points.size()];

		for (int i=0;i<paths.length;i++) {
			for (int j=0;j<paths.length;j++) {
				if (i==j) {dist[i][i]=0; continue;}
				if (points.get(i).distance(points.get(j))<=edgeThreshold)
					dist[i][j]=points.get(i).distance(points.get(j));
				else
					dist[i][j]=Double.POSITIVE_INFINITY;
				paths[i][j]=j;
			}
		}

		for (int k=0;k<paths.length;k++) {
			for (int i=0;i<paths.length;i++) {
				for (int j=0;j<paths.length;j++) {
					if (dist[i][j]>dist[i][k] + dist[k][j]){
						dist[i][j]=dist[i][k] + dist[k][j];
						paths[i][j]=paths[i][k];
					}
				}
			}
		}

		ArrayList<Point> resultFinal = new ArrayList<Point>();

		//		ArrayList<Point> chemin = hitPoints;
		double min = Integer.MAX_VALUE;
		for(int i = 0;i<hitPoints.size();i++) {
			ArrayList<Point> chemin = algoPlusCourt( hitPoints, edgeThreshold,i);

			ArrayList<Point> result = new ArrayList<Point>();

			int s,t;
			Point p,q;
			for (int index=0;index<chemin.size();index++){
				s=points.indexOf(chemin.get(index));
				t=points.indexOf(chemin.get((index+1)%chemin.size()));
				while (paths[s][t]!=t && paths[s][t]!=s){
					if (points.get(s).distance(points.get(paths[s][t]))>edgeThreshold){
						System.err.println("FATAL ERROR. VERY PANICKED.");
					}

					p=points.get(s);
					q=points.get(paths[s][t]);

					result.add(p);

					s=paths[s][t];
				}
				if (points.get(s).distance(points.get(paths[s][t]))>edgeThreshold || paths[s][t]!=t){
					System.err.println("FATAL ERROR. VERY PANICKED.");
				}

				p=points.get(s);
				q=points.get(t);

				result.add(p);
				result.add(q);
			}

			double score = Evaluator.score(result);
			System.out.println("le score est  = " + score);
			if(score<min) {
				min = score;
				resultFinal = result;
			}
			//			chemin = renew(chemin);
		}
		return resultFinal;
	}
}
