
all: run

run: Main.o SMAWP.o
	gcc Main.o SMAWP.o -std=c99 -o run

Main.o: Main.c
	gcc -c Main.c -g -std=c99

SMAWP.o: SMAWP.c
	gcc -c SMAWP.c -g -std=c99

clean:
	rm -f *.o run

