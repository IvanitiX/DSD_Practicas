#!/bin/bash
make -f Makefile.calculadora clean
clear
make -f Makefile.calculadora
./bin/server &
./bin/cliente localhost 2 + 2
./bin/cliente localhost 2 - 2
./bin/cliente localhost 2 x 2
./bin/cliente localhost 2 : 2
./bin/cliente localhost 34 + 67
./bin/cliente localhost 34 - 67
./bin/cliente localhost 34 x 67
./bin/cliente localhost 34 : 67

./bin/cliente localhost -s 3 -5 1
./bin/cliente localhost -s 0 2 -1
./bin/cliente localhost -s 3 0 -1
./bin/cliente localhost -s 3 2 0
./bin/cliente localhost -s 1 1 1

./bin/cliente localhost -v1 ./input_files/vector1.txt +
./bin/cliente localhost -v1 ./input_files/vector1.txt -
./bin/cliente localhost -v1 ./input_files/vector1.txt x

./bin/cliente localhost -v1 ./input_files/vector2.txt +
./bin/cliente localhost -v1 ./input_files/vector2.txt -
./bin/cliente localhost -v1 ./input_files/vector2.txt x

./bin/cliente localhost -v2 ./input_files/vector1.txt + ./input_files/vector2.txt
./bin/cliente localhost -v2 ./input_files/vector1.txt - ./input_files/vector2.txt
./bin/cliente localhost -v2 ./input_files/vector1.txt x ./input_files/vector2.txt
./bin/cliente localhost -v2 ./input_files/vector1.txt : ./input_files/vector2.txt

./bin/cliente localhost -m ./input_files/matriz1.txt + ./input_files/matriz2.txt
./bin/cliente localhost -m ./input_files/matriz1.txt - ./input_files/matriz2.txt
./bin/cliente localhost -m ./input_files/matriz1.txt x ./input_files/matriz2.txt

./bin/cliente localhost -dm ./input_files/matriz1.txt
./bin/cliente localhost -dm ./input_files/matriz2.txt