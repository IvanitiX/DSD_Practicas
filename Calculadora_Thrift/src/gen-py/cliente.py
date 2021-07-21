import sys
import numpy

from calculadora import Calculadora
from calculadora.ttypes import funcion_cuadratica

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

"""
Variables globales en este fichero
"""

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

def uso():
    print(f"Uso : {argv[0]} <Operando1> <Operacion(+,-,x,:)> <Operando2>")
    exit(-1)

def switch_basico(operacion, oper1, oper2):
    if(operacion == '+'):
        resultado = client.suma(oper1,oper2)
    elif(operacion == '-'):
        resultado = client.resta(oper1,oper2)
    elif(operacion == 'x'):
        resultado = client.producto(oper1,oper2)
    elif(operacion == ':'):
        resultado = client.cociente(oper1,oper2)
    else:
        resultado = None
    return resultado

def switch_acumulativo(operacion, acumulativo):
    if(operacion == '+'):
        resultado = client.sumaAcumulada(acumulativo)
    elif(operacion == '-'):
        resultado = client.restaAcumulada(acumulativo)
    elif(operacion == 'x'):
        resultado = client.productoAcumulado(acumulativo)
    else:
        resultado = None
    return resultado

def switch_vectorial(operacion, v1, v2):
    if len(v1) == len(v2):
        if(operacion == '+'):
            resultado = client.sumaVectores(v1,v2)
        elif(operacion == '-'):
            resultado = client.restaVectores(v1,v2)
        elif(operacion == 'x'):
            resultado = client.productoVectores(v1,v2)
        elif(operacion == ':'):
            resultado = client.cocienteVectores(v1,v2)
        else:
            resultado = None
    else:
        resultado = None

    return resultado

def switch_matricial(operacion, m1, m2):
    if len(m1) == len(m2):
        if(operacion == '+'):
            resultado = client.sumaMatrices(m1,m2)
        elif(operacion == '-'):
            resultado = client.restaMatrices(m1,m2)
        elif(operacion == 'x'):
            resultado = client.productoMatrices(m1,m2)
        else:
            resultado = None
    elif (len(m1[0] == len(m2))) and operacion == 'x':
        resultado = client.productoMatrices(m1,m2)
    else:
        resultado = None

    return resultado
    
"""
Como con la primera parte de la práctica, se hará a través de argumentos.
Como en Python no hay un int main(argc,argv) como tal, montaré esas variables con ayuda de sys
"""
argc = len(sys.argv)
argv = sys.argv

if (argc < 4):
    uso()
else:
    print("hacemos ping al server")
    client.ping()

    
    if (argc == 4 and argv[1] == "-v1"):
        operacion = argv[3]
        ficheroAcumulado = argv[2]

        with open(argv[2]) as f:
            vector = [int(x) for x in f.read().split()]
        resultado = switch_acumulativo(operacion,vector)

        if (resultado != None):
            print(f"[{operacion}] {vector} = {resultado}")
        else:
            uso()

    elif (argc == 5 and argv[1] == '-v2'):
        operacion = argv[3]
        ficheroAcumulado1 = argv[2]
        ficheroAcumulado2 = argv[4]

        with open(argv[2]) as f:
            vector1 = [int(x) for x in f.read().split()]

        with open(argv[4]) as f2:
            vector2 = [int(x) for x in f2.read().split()]

        resultado = switch_vectorial(operacion,vector1,vector2)

        if (resultado != None):
            print(f"{vector1} {operacion} {vector2} = {resultado}")
        else:
            uso()

    elif (argc == 5 and argv[1] == '-m2'):
        operacion = argv[3]
        ficheroAcumulado1 = argv[2]
        ficheroAcumulado2 = argv[4]
        matriz1 = []
        matriz2 = []
        linea_anadir = []

        with open(argv[2]) as f:
            for linea in f:
                linea_anadir = [int(x) for x in linea.split()]
                matriz1.append(linea_anadir)

        with open(argv[4]) as f2:
            for linea in f2:
                linea_anadir = [int(x) for x in linea.split()]
                matriz2.append(linea_anadir)

        resultado = switch_matricial(operacion,matriz1,matriz2)

        if (resultado != None):
            print(f"{matriz1} {operacion} {matriz2} = {resultado}")
        else:
            uso()
        




    elif (argc == 5 and argv[1] == '-s'):
            operando1 = int(argv[2])
            operando2 = int(argv[3])
            operando3 = int(argv[4])
            solucion = client.ecuacionGradoDos(operando1,operando2,operando3)

            if (solucion.x1 == -12345678):
                print(f"Para la ecuación {solucion.a}x^2 + {solucion.b}x + {solucion.c} -> No hay solución")
            else:
                print(f"Para la ecuación {solucion.a}x^2 + {solucion.b}x + {solucion.c} -> X1 = {solucion.x1} | X2 = {solucion.x2}")
    elif (argc == 4):
        operando1 = int(argv[1])
        operacion = argv[2][0]
        operando2 = int(argv[3])

        resultado = switch_basico(operacion,operando1,operando2)

        if (resultado != None):
            print(f"{operando1} {operacion} {operando2} = {resultado}")
        else:
            uso()



    transport.close()

