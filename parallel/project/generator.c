#include <stdio.h> 
#include <stdlib.h> 
#include <sys/types.h>
#include <sys/stat.h>
#include "parallel.c"
#include "serial.c"

int main(){
	int n, threads;
	struct stat st = {0};
	if (stat("/Result_Files", &st) == -1) mkdir("Result_Files", 0777); //Courtesy of Arnaud Le Blanc, https://stackoverflow.com/questions/7430248/creating-a-new-directory-in-c

	printf("enter no of vertices :");
	if(!(scanf("%d",&n))) return 1;
	printf("enter number of threads: ");
	if(!(scanf("%i",&threads))) return 1;
	FILE *fil;
	fil = fopen("Result_Files/test.txt", "w");
	int num = n;
	srand(0);
	fprintf(fil,"%i\n", num);
	for(int i = 0; i<num; i++){
		for(int ii = 0; ii<num; ii++){
			if(i == ii) fprintf(fil,"0");
			else{
				int ig = (rand() % 1000);
				fprintf(fil,"%i", ig);
			}
			if(ii<num-1) fprintf(fil,"\t");
		}
		fprintf(fil,"\n");
	}
	fclose(fil);

	fed_main();

	main_parallel(threads);

	int flag = 1;
	FILE *file;
	fil = fopen("Result_Files/result.txt", "r");
	file = fopen("Result_Files/result_serial.txt", "r");
	char buff[9216];
	if(!(fgets(buff, 9216, fil))) return 1;
	char buff_f[9216];
	if(!(fgets(buff_f, 9216, file))) return 1;
	buff[strlen(buff)-1] = '\0';
	n = atoi(buff); 
	const char s[2] = "\t";
	for(int ii = 0; ii<n; ii++){
		if(!(fgets(buff, 9216, fil))) return 1;
		if(!(fgets(buff_f, 9216, file))) return 1;
		buff[strlen(buff)-1] = '\0';
		buff_f[strlen(buff_f)-1] = '\0';
		char *token;
		char *token_f;
		token = strtok(buff, s);
		token_f = strtok(buff_f, s);
		for(int i = 0; i<n; i++){
			if(atoi(token_f) != atoi(token)){
				flag = 0;
			}
			token = strtok(NULL, s);
			token_f = strtok(NULL, s);
		}
	}
	printf("- %i -\n", flag); 
	fclose(fil);
	fclose(file);
}
