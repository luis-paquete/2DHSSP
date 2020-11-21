#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "global.h"
#include <float.h>

int SIZE = 500;

int* rows;
int* columns;
int s_rows, s_columns;

int k;

int w, h;
double* best; //value, column
int* colbest;

//#define get(ty, tx) ((ty < n+1-K+k && tx <= ty-1) ? precalc1[tx] + precalc2[ty] + pointsx[tx] * pointsy[ty] + dp[k-1][tx] : DBL_MAX)

double get(int ty, int tx){
	return ((ty < n+1-K+k && tx <= ty-1) ? precalc1[tx] + precalc2[ty] + pointsx[tx] * pointsy[ty] + dp[k-1][tx] : DBL_MAX);
}

void reduce(int* rows, int s_rows, int* columns, int s_columns){
	
	int* stackx = (int*) malloc(sizeof(int) * s_rows);
	int* stacky = (int*) malloc(sizeof(int) * (s_rows+1));
	
	// conquer algorithm
	register int r=0, c, s_size=0;
	stackx[s_size] = columns[0];
	stacky[s_size] = rows[0];

	for(c=1; c < s_columns; c++){
		while( get( rows[s_size] + k, columns[c] + k-1) <= 
		       get(stacky[s_size] + k, stackx[s_size] + k -1)){
			s_size--;
			if(s_size<=0) 
				break;
		}

		if(s_size < s_rows - 1){
			s_size++;
			stacky[s_size] = rows[s_size];
			stackx[s_size] = columns[c];
		}
	}
	// end of conquer algorithm


	// Base case
	register int i, j;
	if(s_rows <= 2){
		for(i=0; i<s_rows; i++){
			r = rows[i];
			best[r] = DBL_MAX;
			for(j=0; j<s_columns; j++){
				c = columns[j];

				double tv = get(r+k, c+k-1);

				if(tv < best[r]){
					best[r] = tv;
					colbest[r] = c;
				}
			}
		}
		
		free(stackx);
		free(stacky);

		return;
	}

	//recursive call with interpolation
	int* rows2 = (int*) malloc(sizeof(int) * (s_rows/2+1));
	for(i=1; i<s_rows; i=i+2)
		rows2[i/2] = rows[i];

	reduce(rows2, s_rows/2, stackx, s_rows);
	free(rows2);

	//calc mins
	j = 0;
	for(i=0; i<s_rows; i+=2){
		r = rows[i];
		
		best[r] = DBL_MAX;
		
		c=columns[j];

		int end;
		if(i+1 < s_rows)
			end = colbest[rows[i+1]];
		else
			end = columns[s_columns-1];

		while(c <= end){

			if(r < n+1-K && c <= r){
				double t = get(r+k,c+k-1);
				//printf("%lf\n",t);
				if(best[r] > t){
					best[r] = t;
					colbest[r] = c;
				}
			}

			j++;
			if(j < s_columns)
				c=columns[j];
			else
				c=end+1;
		}
		
		j--;
	}

	free(stackx);
	free(stacky);

}

double* simpleRun(int _k, int h, int w){
	k = _k;
	
	//int rows[h]; 
	int i;
	int* rows = (int*) malloc(sizeof(int) * (h));

	for(i=0; i<h; i++)
		rows[i] = i;
	
	//int columns[w];
	int* columns = (int*) malloc(sizeof(int) * (w));
	for(i=0; i<w; i++)
		columns[i] = i;

	best = (double*) malloc(sizeof(double) * (h+1));
	colbest = (int*) malloc(sizeof(int) * (h+1));

	reduce(rows, h, columns, w);
	free(rows);
	free(columns);
	free(colbest);
	return best;
}


