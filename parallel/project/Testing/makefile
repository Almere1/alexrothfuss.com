CCBIN=/usr/bin/gcc
CC=$(CCBIN) -Wall -Werror -O3

default: gen

gen: generator.c parallel.c serial.c utils/stopwatch.c utils/stopwatch.h
	$(CC) -o gen generator.c parallel.c serial.c utils/stopwatch.c

.PHONY: clean
clean:
	rm -Rf *.o grader *.dSYM

