package dp;

import java.util.*;
import java.io.*;

public class Main{
	public static int SIZE = 256;
	public int n;
	public Point[] points;
	//public int[][] diff;
	public double[][] dp;
	public double[] cost0;
	public double[] costn;
	public Point orig = new Point(0, 0);
	public int K;

	public static int min(int a, int b){
		if(a < b) return a;
		else return b;
	}
	public double value2(int k1, int k2){
		return cost0[n-1] - cost0[k1] - costn[k2] - points[k1].x * points[k1+1].y - points[k2-1].x * points[k2].y + points[k1].x * points[k2].y;
	}

	public double mainAlgo(){
		int i, o, x, y, k;

		// pre calculations
		cost0 = new double[n+1];
		costn = new double[n+1];
		cost0[0] = 0;
		costn[n-1] = 0;
		for(i=1; i<n; i++){
			cost0[i] = cost0[i-1] + points[i].x * (points[i].y - points[i+1].y);
		}
		for(i=n-1; i>=1; i--){
			costn[i] = costn[i+1] + (points[i].x - points[i-1].x)  * (points[i].y);
		}

		// dp algorithm
		dp = new double[K+2][n+1];
		
		for(i=1; i<=n; i++){
			dp[1][i] = value2(0, i);
		}
		
		K++;
		
		SMAWP smawp = new SMAWP(dp, points, cost0, costn, n, K);
		for(k=2; k<=K; k++){
			smawp.setK(k);
			double[] res = smawp.simpleRun(n+1-K, (n-K) + 2);
			
			for(i=0; i<res.length && i+k < dp[k].length; i++){
				dp[k][i+k] = res[i];
			}

		}	

		return (cost0[n-1] - dp[K][n]);

	}
	public double run(Point[] p, int m, int K){
		int i, o, x, y;
		
		n = m;
		this.K = K;
		points = new Point[n+2];

		for(i=0; i<= n+1; i++){
			points[i] = new Point();
		}
		points[0].x = orig.x;

		for(i=1; i<=n; i++){
			points[i].x = p[i-1].x;
			points[i].y = p[i-1].y;
		}
		points[0].y = points[1].y;

		points[n+1].x = points[n].x;
		points[n+1].y = orig.y;
		n++;

		return mainAlgo();
	}
	public Main(String args[]){
		readInput(args);

	}

	public static void main(String[] args){
		new Main(args);
	}


	public void readInput(String[] args){
		if(args.length != 2){
			System.out.println("Usage: ./run k_value input_file\n");
			return;
		}
		
		int i, n, k;
		Scanner sc;
		k = Integer.parseInt(args[0]);
		File in = new File(args[1]);

		try{
			sc = new Scanner(in);
		}catch(IOException e){
			System.out.println("Error: File " + args[1] + " not found\n");
			return;	
		}
		n = sc.nextInt();
		if(k > n || k < 0){
			System.out.println("Error: k_value must be between 0 and n\n");
			return;	
		}

		Point[] points = new Point[n];
		
		for(i=0; i<n; i++){
			points[i] = new Point(sc.nextDouble(), sc.nextDouble());
		}

		Arrays.sort(points);


		java.util.Date startDate, endDate;
		startDate = new java.util.Date();

		double myResult = run(points, n, k);

		endDate = new java.util.Date();
		long mydiff = endDate.getTime() - startDate.getTime();
		System.out.println("Result: " + myResult + "; Time spent: " + mydiff);
	}

}
