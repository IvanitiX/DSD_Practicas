struct funcion_cuadratica{
   1: required i32 a
   2: required i32 b
   3: required i32 c
   4: required double x1
   5: required double x2
}

service Calculadora{
   void ping(),
   i32 suma(1:i32 num1, 2:i32 num2),
   i32 resta(1:i32 num1, 2:i32 num2),
   i32 producto(1:i32 num1, 2:i32 num2),
   double cociente(1:i32 num1, 2:i32 num2),
   funcion_cuadratica ecuacionGradoDos(1: i32 a, 2: i32 b, 3: i32 c),
   double sumaAcumulada(1: list<i32> acumulado),
   double restaAcumulada(1: list<i32> acumulado),
   double productoAcumulado(1: list<i32> acumulado),
   list<double> sumaVectores(1: list<i32> vector1, 2:list<i32> vector2),
   list<double> restaVectores(1: list<i32> vector1, 2:list<i32> vector2),
   list<double> productoVectores(1: list<i32> vector1, 2:list<i32> vector2),
   list<double> cocienteVectores(1: list<i32> vector1, 2:list<i32> vector2),
   list<list<double>> sumaMatrices(1: list<list<i32>> matriz1, 2:list<list<i32>> matriz2),
   list<list<double>>  restaMatrices(1:list<list<i32>> matriz1, 2:list<list<i32>> matriz2),
   list<list<double>>  productoMatrices(1: list<list<i32>> matriz1, 2:list<list<i32>> matriz2),
}
