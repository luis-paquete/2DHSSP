#include <stdio.h>
#include <stdlib.h>
#include "global.h"
#include <sys/time.h>
#include <string.h>

typedef struct St{
	double x, y;
}St;

int compare(const void *a, const void *b){
	if( ((St*)a)->x == ((St*)b)->x )
		return 0;
	else if( ((St*)a)->x < ((St*)b)->x)
		return -1;
	else
		return 1;
}

double origx = 0;
double origy = 0;

#define value2(k1, k2) precalc1[k1] + precalc2[k2] + pointsx[k1] * pointsy[k2]

double mainAlgo(){
	register int i, k;	

	// pre calculations
	cost0 = (double*) malloc(sizeof(double) * (n+1) );
	costn = (double*) malloc(sizeof(double) * (n+1) );
	cost0[0] = 0;
	costn[n] = 0;
	for(i=1; i<n; i++){
		cost0[i] = cost0[i-1] + pointsx[i] * (pointsy[i] - pointsy[i+1]);
	}
	for(i=n-1; i>=1; i--){
		costn[i] = costn[i+1] + (pointsx[i] - pointsx[i-1])  * (pointsy[i]);
	}

	// dp algorithm
	dp = (double**) malloc(sizeof(double*) * (K+2) );
	for(i=0; i<K+2; i++){
		dp[i] = (double*) malloc(sizeof(double) * (n+1) );
	}
	
	precalc1 = (double*) malloc(sizeof(double) * (n+1));
	precalc2 = (double*) malloc(sizeof(double) * (n+2));

	for(i=0; i<n; i++){
		precalc1[i] = cost0[n-1] - cost0[i] - pointsx[i] * pointsy[i+1];
	}
	for(i=1; i<=n; i++){
		precalc2[i] = -costn[i] - pointsx[i-1] * pointsy[i];
	}
	
	for(i=1; i<=n; i++){
		dp[1][i] = value2(0, i);
	}
	
	K++;
	
	for(k=2; k<=K; k++){
		double* res = (double*) simpleRun(k, n+1-K, (n-K) + 2);
		
		memcpy(dp[k] + k, res, sizeof(double) * (n+1-K) );	
		free(res);
	}
	
	double res = (cost0[n-1] - dp[K][n]);

	free(cost0);
	free(costn);
	free(precalc1);
	free(precalc2);
	for(int i=0; i<K+1; i++){
		free(dp[i]);
	}
	free(dp);

	return res;
}

double run(double* px, double* py, int m, int _K){
	register int i;
	
	n = m;
	K = _K;
	pointsx = (double*) malloc(sizeof(double) * (n+2) );
	pointsy = (double*) malloc(sizeof(double) * (n+2) );

	pointsx[0] = origx;

	for(i=1; i<=n; i++){
		pointsx[i] = px[i-1];
		pointsy[i] = py[i-1];
	}
	pointsy[0] = pointsy[1];

	pointsx[n+1] = pointsx[n];
	pointsy[n+1] = origy;
	n++;

	double res = mainAlgo();
	free(pointsx);
	free(pointsy);

	return res;
}

void readInput(int argc, char* args[]){
	if(argc != 3){
		printf("Usage: ./run k_value input_file\n");
		return;
	}
	
	int i, n, k;
	k = atoi(args[1]);

	freopen(args[2], "r", stdin);

	//printf("Error: File %s not found\n", args[2]);
	scanf("%d", &n);

	if(k > n || k < 0){
		printf("Error: k_value must be between 0 and n\n");
		return;	
	}

	St* points = (St*) malloc(sizeof(St) * n);
	double* pointsx = (double*) malloc(sizeof(double) * n);
	double* pointsy = (double*) malloc(sizeof(double) * n);
	
	for(i=0; i<n; i++){
		scanf("%lf %lf", &points[i].x, &points[i].y);
	}
	qsort(points, n, sizeof(St), compare);
	
	for(i=0; i<n; i++){
		pointsx[i] = points[i].x;
		pointsy[i] = points[i].y;
	}
	free(points);

	struct timeval tvBegin, tvEnd;
	gettimeofday(&tvBegin, NULL);

	double myResult = run(pointsx, pointsy, n, k);

	gettimeofday(&tvEnd, NULL);
	long int diff = (tvEnd.tv_usec + 1000000 * tvEnd.tv_sec) - (tvBegin.tv_usec + 1000000 * tvBegin.tv_sec);
	free(pointsx);
	free(pointsy);

	printf("Result: %lf; Time spent: %d\n", myResult, diff/1000);
}

int main(int argc, char* args[]){

	readInput(argc, args);

}
