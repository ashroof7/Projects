/*
 ============================================================================
 Name        : threaded_Mat_Mul.c
 Author      : Ashraf Saleh
 Version     :
 Copyright   : refer to my blog
 Description : Mutlithreaded Matrix Multiplication C, Ansi-style
 ============================================================================
 */
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

//matrix a is m*p while b is q*n
// c is m*n m rows, n cols
int m,p,q,n;
int **a, ** b, **c;

struct timeval tv;
long long curTime(){
	gettimeofday(&tv,NULL);
	return	tv.tv_usec;
}


void *mulrow(void* row);
void *mulcell(void* row);



void print(int** a,int _i, int _j){
	int i,j;
	for (i = 0; i < _i; ++i) {
		for (j = 0; j < _j; ++j)
			printf("%d\t",a[i][j]);
		printf("\n");
	}
}

void writeMat(int** a,int _i, int _j, FILE* f){
	int i,j;
	for (i = 0; i < _i; ++i) {
		for (j = 0; j < _j; ++j)
			fprintf(f,"%d ",a[i][j]);
		fprintf(f,"\n");
	}
}

void multiply(int** a , int** b, int** c){
	int i,j,k;
	for (i = 0; i < m; ++i) {
		for ( j = 0; j < n; ++j) {
			for (k = 0; k < q; ++k) {
				c[i][j] += (a[i][k]) * (b[k][j]);
			}
		}
	}
}

struct cell{int i,j;};

void multiply1(){
	// The computation of each element of the output matrix happens in a thread.
	int i,j,r,t;

	//dynamically allocate #cells threads
	pthread_t** threads = (pthread_t**) malloc(sizeof(pthread_t*)*m*n);
	struct cell** cells = (struct cell**) malloc(sizeof(struct cell*)*m*n);

	for (i = 0; i < m; ++i) {
		for (j = 0 ; j < n ; j ++){
			t = i*n + j;
			threads[t] =(pthread_t*) malloc(sizeof(pthread_t));
			cells[t] = malloc(sizeof(struct cell));
			cells[t]->i = i ; cells[t]->j = j;
			r = pthread_create(threads[t], NULL, mulcell, (void*) cells[t]);
			if (r<0)
				printf("failed to create thread at mul1 at (i,j) %d,%d\n",i,j);
		}
	}

	//wait for the threads to finish
	for (i = 0; i < m*n; ++i){
		pthread_join(*threads[i],NULL);
		free(cells[i]);
//		 no need to free threads[i] as its already freed by pthread_exit();
	}
	//free allocated memory
	free(threads);
	free(cells);
}

void *mulcell(void* param){
	//	passing a struct to overcome the limitation of passing one argument
	struct cell *cel = (struct cell*)param;
	int k, i = cel->i, j = cel->j;

	for (k = 0; k < p; ++k){
		c[i][j] += a[i][k] * b[k][j];
	}

	pthread_exit(0);
	return NULL;//will never execute
}

void multiply2(){
	//The computation of each row of the output matrix happens in a thread.
	int i,r;

	//dynamically allocate #rows threads
	pthread_t** threads = (pthread_t**) malloc(sizeof(pthread_t*)*m);
	int* rows = (int*) malloc(sizeof(int)*m);

	for (i = 0; i < m; ++i) {
		threads[i] =(pthread_t*) malloc(sizeof(pthread_t));
		rows[i] = i;
		r = pthread_create(threads[i], NULL, mulrow, (void*) &rows[i]);
		if (r<0)
			printf("failed to create thread at mul2 at i =  %d\n",i);
	}

	//wait for the threads to finish
	for (i = 0; i < m; ++i)
		pthread_join(*threads[i],NULL);

	//free allocated memory
	free(threads);
	free(rows);
}

void *mulrow(void* row){
	int j, i = *((int*)row) ,k;

	for (j = 0 ; j < n ; j ++)
		for (k = 0 ; k < p ; k++)
			c[i][j] += a[i][k] * b[k][j];
	pthread_exit(NULL);
	return NULL;// anyway this line won't be processes yet it's added to prevent eclipse from showing warnings
}

int **read_matrix(FILE *f,int m, int n){
	int i,j;
	int **a = (int**) malloc(sizeof(int *)*m);
	for (i = 0; i < m; ++i) {
		a[i] = (int *) malloc(sizeof(int)*n);
		for (j = 0; j < n; ++j) {
			fscanf(f,"%d",&a[i][j]);
		}
	}
	return a;
}


long long benchmark(int nElements, int itrNum , void(*myfn)()){
	n = m = p = nElements;
	a = (int**) malloc(sizeof(int*)*nElements);
	b = (int**) malloc(sizeof(int*)*nElements);
	c = (int**) malloc(sizeof(int*)*nElements);

	int i,j,k;
	long long averagetime =0 ,t1,t2;
	for (i = 0; i < nElements; ++i){
		a[i] = (int*) malloc(sizeof(int)*nElements); 
		b[i] = (int*) malloc(sizeof(int)*nElements);
		c[i] = (int*) malloc(sizeof(int)*nElements);
	}
	for (k = 0; k < itrNum; ++k) {
		for (i = 0; i < nElements; ++i)
			for (j = 0; j < nElements; ++j)
				a[i][j] =rand()%1001 , b[i][j] =rand()%1001;

		t1 = curTime();
//		multiply1();
		(*myfn)();
		t2 = curTime();
		averagetime += (t2-t1);
	}

	print(c,nElements,nElements);
	averagetime/=itrNum;
	printf("benchmark using a[%d][%d] * b[%d][%d]: average on %d samples = %lld usec\n",
			nElements,nElements,nElements,nElements,itrNum,averagetime);
	return averagetime;
}


int main(void) {
	benchmark(400,1,multiply2);
	int i,j;
	FILE* in  = fopen("input.txt","r");
	// reading matrix a
	fscanf(in,"%d %d",&m,&p);
	a = read_matrix(in,m,p);

	// reading matrix b
	fscanf(in,"%d %d",&q,&n);
	b = read_matrix(in,q,n);

	if (p!=q){
		printf("incompatible matrices --not supported\n");
		return 0;
	}

	//initializing matrix c
	c = (int**) malloc(sizeof(int *)*m);
	for (i = 0; i < m; ++i){
		c[i] = (int *) malloc(sizeof(int)*n);
		for (j = 0; j < n; ++j)
			c[i][j] = 0;
	}

	long long t1,t2;
	FILE* out = fopen("output.txt","w");

//	multiply(a,b,c);
//	multiply(a,b,c);
//	multiply(a,b,c);

	for (i = 0; i < m; ++i)
		for (j = 0; j < n; ++j)
			c[i][j] = 0;


	fprintf(out,"result of non-threaded multiplication\n");
	t1 = curTime();
	multiply(a,b,c);
	t2 = curTime();
	writeMat(c,m,n,out);
	fprintf(out,"elapsed time = %lld usec\n\n",t2-t1);

	for (i = 0; i < m; ++i)
		for (j = 0; j < n; ++j)
			c[i][j] = 0;

	fprintf(out,"result of multiplication1 (thread for each cell)\n");
	t1 = curTime();
	multiply1();
	t2 = curTime();
	writeMat(c,m,n,out);
	fprintf(out,"elapsed time = %lld usec\n\n",t2-t1);
	for (i = 0; i < m; ++i)
		for (j = 0; j < n; ++j)
			c[i][j] = 0;


	fprintf(out,"result of multiplication2 (thread for each row)\n");
	t1 = curTime();
	multiply2();
	t2 = curTime();
	writeMat(c,m,n,out);
	fprintf(out,"elapsed time = %lld usec\n\n",t2-t1);

	fclose(out);
	print(c,m,n);
	return 0;
}
