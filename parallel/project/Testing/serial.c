#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<sys/types.h>
#include "utils/stopwatch.h"

int n;
int *dist;
void f_floydWarshell(){
	int i, j, k;
	for (k = 0; k < n; k++){
		for (i = 0; i < n; i++){
			for (j = 0; j < n; j++){
				if (dist[i*n+j] > dist[i*n+k] + dist[k*n+j]) dist[i*n+j] = dist[i*n+k] + dist[k*n+j];
			}
		}
	}
	return;
}
int fed_main(){
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

	StopWatch_t * watch = malloc(sizeof(StopWatch_t));
	startTimer(watch);

	f_floydWarshell();

	stopTimer(watch);
	printf("Time for Serial: %f\n", getElapsedTime(watch));

	fil = fopen("Result_Files/result_serial.txt", "w");
	for (int i = 0; i < n; i++){
		for (int j = 0; j < n; j++){
			if((i == j) && (dist[i*n+j] != 0)){
				printf("Matrix contains negative cycle, Floyd-Warshall unproductive");
				fclose(fil);
				remove("Result_Files/result_serial.txt");
				return 0;
			}
			fprintf(fil,"%i", dist[i*n+j]);
			if(j<n-1) fprintf(fil,"\t");
		}
		if(i<n-1) fprintf(fil, "\n");
	}
	fclose(fil);
	return 0;
}
