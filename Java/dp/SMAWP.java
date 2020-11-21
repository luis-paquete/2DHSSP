package dp;

import java.util.Scanner;

public class SMAWP{
	public static int SIZE = 500;

	public double[] rows;
	public double[] columns;
	public int s_rows, s_columns;

	public double[][] dp;
	public Point[] points;
	public double[] cost0;
	public double[] costn;
	public double[] precalc1;
	public double[] precalc2;
	public int k;
	public int K, n;

	public int w, h;
	public double[] best; //value, column
	public int[] colbest;

	private double min(double a, double b){
		return a < b ? a : b;
	}

	public double get(int ty, int tx){
		if(ty < n+1-K+k && tx <= ty-1)
			return precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];
		else
			return Double.MAX_VALUE;
	}

	public void reduce(int[] rows, int s_rows, int[] columns, int s_columns){
		
		int[] stackx = new int[s_rows];
		int[] stacky = new int[s_rows+1];
		int s_size=0;

		// conquer algorithm
		int r=0, c;
		stackx[s_size] = columns[0];
		stacky[s_size] = rows[0];

		for(c=1; c < s_columns; c++){
			
			int ty = rows[s_size] + k;
			int tx = columns[c] + k-1;
			double tv, tv2;

			if(ty < n+1-K+k && tx <= ty-1)
				tv = precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];
			else
				tv = Integer.MAX_VALUE;

			ty = stacky[s_size] + k;
			tx = stackx[s_size] + k - 1;
			if(ty < n+1-K+k && tx <= ty-1)
				tv2 = precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];
			else
				tv2 = Integer.MAX_VALUE;

			while( tv <= tv2){
				s_size--;
				if(s_size < 0)
					break;

				ty = rows[s_size] + k;
				tx = columns[c] + k-1;
				if(ty < n+1-K+k && tx <= ty-1)
					tv = precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];
				else
					tv = Integer.MAX_VALUE;

				ty = stacky[s_size] + k;
				tx = stackx[s_size] + k -1;
				if(ty < n+1-K+k && tx <= ty-1)
					tv2 = precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];
				else
					tv2 = Integer.MAX_VALUE;

			}

			if(s_size < s_rows - 1){
				s_size++;
				stacky[s_size] = rows[s_size];
				stackx[s_size] = columns[c];
			}
		}
		// end of conquer algorithm


		// Base case
		int i, j;
		if(s_rows <= 2){
			for(i=0; i<s_rows; i++){
				r = rows[i];
				best[r] = Integer.MAX_VALUE;
				for(j=0; j<s_columns; j++){
					c = columns[j];

					int ty = r + k;
					int tx = c + k-1;
					double tv;

					if(ty < n+1-K+k && tx <= ty-1)
						tv = precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];
					else
						tv = Double.MAX_VALUE;

					if(tv < best[r]){
						best[r] = tv;
						colbest[r] = c;
					}
				}
			}

			return;
		}

		//recursive call with interpolation
		int[] rows2 = new int[s_rows/2+1];
		int s_size2;
		for(i=1; i<s_rows; i=i+2)
			rows2[i/2] = rows[i];

		reduce(rows2, s_rows/2, stackx, s_rows);

		//calc mins
		int inf, sup;
		j = 0;
		for(i=0; i<s_rows; i+=2){
			r = rows[i];
			
			best[r] = Double.MAX_VALUE;
			
			c=columns[j];

			int end;
			if(i+1 < s_rows)
				end = colbest[rows[i+1]];
			else
				end = columns[s_columns-1];

			while(c <= end){

				if(r < n+1-K && c <= r){
					/*int ty = r + k;
					int tx = c + k-1;
					int t = precalc1[tx] + precalc2[ty] + points[tx].x * points[ty].y + dp[k-1][tx];*/
					double t = get(r+k,c+k-1); //Better time with this
					
					if(best[r] > t){
						best[r] = t;
						colbest[r] = c;
					}
				}

				j++;
				if(j < columns.length)
					c=columns[j];
				else
					c=end+1;
			}
			
			j--;
		}

	}

	public void setK(int k){
		this.k = k;
	}

	public SMAWP(double[][] dp, Point[] points, double[] cost0, double[] costn, int n, int K){
		this.dp = dp;
		this.points = points;
		this.cost0 = cost0;
		this.costn = costn;
		this.n = n;
		this.K = K;
		
		precalc1 = new double[n+1];
		precalc2 = new double[n+1];
		int i;
		for(i=0; i<n; i++){
			precalc1[i] = cost0[n-1] - cost0[i] - points[i].x * points[i+1].y;
		}
		for(i=1; i<n; i++){
			precalc2[i] = -costn[i] - points[i-1].x * points[i].y;
		}
	}

	public double[] simpleRun(int h, int w){
		int[] rows = new int[h];
		for(int i=0; i<h; i++){
			rows[i] = i;
		}
		int[] columns = new int[w];
		for(int i=0; i<w; i++){
			columns[i] = i;
		}
		best = new double[h+1];
		colbest = new int[h+1];

		reduce(rows, h, columns, w);
		return best;
	}
		
}

