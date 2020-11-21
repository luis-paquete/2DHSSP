
#define min(a, b) (a < b ? a : b)
#define max(a, b) (a > b ? a : b)

int K, n;
double** dp;
double* cost0;
double* costn;
double* pointsx;
double* pointsy;
double* precalc1;
double* precalc2;

double* simpleRun(int _k, int h, int w);
