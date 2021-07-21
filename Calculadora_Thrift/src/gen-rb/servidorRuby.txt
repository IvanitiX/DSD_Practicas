require 'thrift'
require_relative 'calculadora.rb'
require_relative 'calculadora_types.rb'

class CalculadoraHandler
  def initialize()
    @log = {}
  end

  def ping()
    puts "ping()"
  end

  def suma(n1,n2)
    puts "Sumando"
    return n1+n2
  end

  def resta(n1,n2)
    puts "Restando"
    return n1-n2
  end

  def producto(n1,n2)
    puts "Multiplicando"
    return n1*n2
  end

  def cociente(n1,n2)
    puts "Dividiendo"
    return (n1*1.0)/n2
  end

  def ecuacionGradoDos(a,b,c)
    puts "ECUACION"
    result = Funcion_cuadratica.new
    result.a = a
    result.b = b
    result.c = c
    
    if (result.a == 0)
      result.x1 = (-result.c * 1.0)/(result.b * 1.0)
      result.x2 = (-result.c * 1.0)/(result.b * 1.0)
    else
      discriminante = (result.b*result.b) - 4*result.a*result.c
      if (discriminante < 0)
        result.x1 = -12345678
        result.x2 = -12345678
      else
        raiz = Math.sqrt(discriminante)
        result.x1 = (((result.b * result.b) + raiz)*1.0)/(2.0*result.a)
        result.x2 = (((result.b * result.b) - raiz)*1.0)/(2.0*result.a)
      end
    end
    puts "Resultado extraido"
    return result
  end

  def sumaAcumulada(arrayAcumulativo)
    acumulado = 0
    for item in arrayAcumulativo do
      acumulado = acumulado + item
    end
    return acumulado
  end

  def restaAcumulada(arrayAcumulativo)
    acumulado = arrayAcumulativo[0]
    for item in 1..arrayAcumulativo.length-1 do
      acumulado = acumulado - arrayAcumulativo[item]
    end
    return acumulado
  end

  def productoAcumulado(arrayAcumulativo)
    acumulado = 1
    for item in arrayAcumulativo do
      acumulado = acumulado * item
    end
    return acumulado
  end

  def sumaVectores(v1, v2)
    total = Array.new(v1.length)
    for i in 0..v1.length-1 do
      total[i] = v1[i] + v2[i]
    end
    return total
  end

  def restaVectores(v1, v2)
    total = Array.new(v1.length)
    for i in 0..v1.length-1 do
      total[i] = v1[i] - v2[i]
    end
    return total
  end

  def productoVectores(v1, v2)
    total = Array.new(v1.length)
    for i in 0..v1.length-1 do
      total[i] = v1[i] * v2[i]
    end
    return total
  end

  def cocienteVectores(v1, v2)
    total = Array.new(v1.length)
    for i in 0..v1.length-1 do
      total[i] = v1[i]*1.0 / v2[i]
    end
    return total
  end

  def sumaMatrices(m1,m2)
    total = Array.new(m1.length){Array.new(m2.length)}
    for i in 0..m1.length-1 do
      for j in 0..m2.length-1 do
        total[i][j] = m1[i][j] + m2[i][j]
      end
    end

    return total
  end

  def restaMatrices(m1,m2)
    total = Array.new(m1.length){Array.new(m2.length)}
    for i in 0..m1.length-1 do
      for j in 0..m2.length-1 do
        total[i][j] = m1[i][j] - m2[i][j]
      end
    end

    return total
  end

  def productoMatrices(m1,m2)
    total = Array.new(m1[0].length){Array.new(m2.length)}
    for i in 0..m2[0].length-1 do
      for j in 0..m1.length-1 do
        valor = 0
        for k in 0..m1[0].length-1 do
          valor = valor + m1[j][k] * m2[k][i]
        end
        total[j][i] = valor
      end
    end

    return total
  end

end

handler = CalculadoraHandler.new()
processor = Calculadora::Processor.new(handler)
transport = Thrift::ServerSocket.new(9090)
transportFactory = Thrift::BufferedTransportFactory.new()
server = Thrift::SimpleServer.new(processor, transport, transportFactory)

puts "Iniciando servidor..."
server.serve()
puts "Hecho"
