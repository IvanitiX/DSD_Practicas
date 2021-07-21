typedef double secuencia<>;
typedef secuencia matriz<>;

struct funcion_cuadratica{
    int a;
    int b;
    int c;
    double valor_1;
    double valor_2;
};

program RPC_CALC{
    version DIRVER{
        double suma(double,double) = 1;
        double resta(double,double) = 2;
        double producto(double,double) = 3;
        double cociente(double,double) = 4;
        funcion_cuadratica ecuacionGradoDos(int,int,int) = 5; 
        double sumaAcumuladaVector(secuencia) = 6;
        double restaAcumuladaVector(secuencia) = 7;
        double productoAcumuladaVector(secuencia) = 8;
        secuencia sumaVectores(secuencia,secuencia) = 9;
        secuencia restaVectores(secuencia,secuencia) = 10;
        secuencia productoVectores(secuencia,secuencia) = 11;
        secuencia cocienteVectores(secuencia,secuencia) = 12;
        matriz sumaMatrices(matriz,matriz) = 13;
        matriz restaMatrices(matriz,matriz) = 14;
        matriz productoMatrices(matriz,matriz) = 15;
        double determinanteMatrices(matriz) = 16;
    } = 1;
} = 0x20000001;

