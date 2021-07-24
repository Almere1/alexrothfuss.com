#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<pthread.h>
#include<sys/types.h>
#include "utils/stopwatch.h"

int n, threads;
int *dist;
static pthread_barrier_t barrier;

void* p_floydWarshell(void *arg){
	int k, j, i, x_start, y_start;
	int flag = 1;
	int *rank = arg; 
	if(threads>n){
		flag = threads/n;
		y_start = (*rank)/flag;	
	} else y_start = (*rank)*n/threads;
	x_start = (n/flag)*((*rank)%flag);

	for (k = 0; k < n; k++){
		for (i = y_start; i < y_start+(flag*n)/threads; i++){
			for (j = x_start; j < (n/flag)+x_start; j++){
				if (dist[i*n+j] > dist[i*n+k] + dist[k*n+j]) dist[i*n+j] = dist[i*n+k] + dist[k*n+j];
			}
		}
		pthread_barrier_wait(&barrier);
	}
	return NULL;
}

int main_parallel(int t){

//phase 1

	FILE *fil;
	fil = fopen("Result_Files/test.txt", "r");
	char buff[9216];
	if(!(fgets(buff, 9216, fil))) return 1;
	buff[strlen(buff)-1] = '\0';
	n = atoi(buff); 
	dist = malloc(sizeof(int)*(n*n));
	const char s[2] = "\t";
	for(int ii = 0; ii<n; ii++){
		if(!(fgets(buff, 9216, fil))) return 1;
		buff[strlen(buff)-1] = '\0';
		char *token;
		token = strtok(buff, s);
		for(int i = 0; i<n; i++){
			dist[ii*n+i] = atoi(token);
			token = strtok(NULL, s);
		}
	}
	fclose(fil);
	threads = t;

//phase 2

	StopWatch_t * watch = malloc(sizeof(StopWatch_t));
	startTimer(watch);

	pthread_barrier_init (&barrier, NULL, threads);
	pthread_t *id = malloc(sizeof(pthread_t)*threads);
	for(int i = 0; i<threads; i++){
		int *ig = malloc(sizeof(int));
		*ig = i; 
		pthread_create(&id[i], NULL, &p_floydWarshell, ig);
	}
	for(int i = 0; i<threads; i++) pthread_join(id[i], NULL);

	stopTimer(watch);
	printf("Time for Parallel: %f\n", getElapsedTime(watch));

//phase 3

	fil = fopen("Result_Files/result.txt", "w");
	for (int i = 0; i < n; i++){
		for (int j = 0; j < n; j++){
			if((i == j) && (dist[i*n+j] != 0)){
				printf("Matrix contains negative cycle, Floyd-Warshall unproductive");
				fclose(fil);
				remove("Result_Files/result.txt");
				return 0;
			}
			if(dist[i*n+j]>1000) fprintf(fil,"%i", 10000000);
			else fprintf(fil,"%i", dist[i*n+j]);
			if(j<n-1) fprintf(fil,"\t");
		}
		if(i<n-1) fprintf(fil, "\n");
	}
	fclose(fil);
	return 0;
}
