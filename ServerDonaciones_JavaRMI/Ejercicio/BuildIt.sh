echo "____________________________________________"
echo "|DSD - Práctica 3                          |"
echo "|------------------------------------------|"
echo "|Iván Valero Rodríguez                     |"
echo "|Curso 2020-21. Grupo 1.                   |"
echo "|__________________________________________|"

mkdir build
javac -d build *.java
cp server.policy build/

echo ">> Se han compilado todos los archivos en la carpeta build."
echo ">> Para probarlo, vaya a la carpeta build y ejecute desde allí."