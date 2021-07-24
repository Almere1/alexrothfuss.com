CCBIN=/usr/bin/gcc
CC=$(CCBIN)

default: generator

generator: generator.c utils/stopwatch.c parallel.c serial.c 
	$(CC) generator.c parallel.c serial.c utils/stopwatch.c -lpthread -o gen


