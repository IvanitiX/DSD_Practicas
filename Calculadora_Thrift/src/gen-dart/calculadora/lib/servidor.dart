import 'dart:async';
import 'dart:io';

import 'package:logging/logging.dart';
import 'package:thrift/thrift.dart';
import 'package:thrift/thrift_console.dart';
import 'package:calculadora/calculadora.dart';

TProtocol _protocol;
TProcessor _processor;

main() {
  Logger.root.level = Level.ALL;
  Logger.root.onRecord.listen((LogRecord rec) {
    print('${rec.level.name}: ${rec.time}: ${rec.message}');
  });

  int port = 9090;
  _runTcpServer(port);
}

Future _runTcpServer(int port) async {
  var serverSocket = await ServerSocket.bind('127.0.0.1', port);
  print('listening for TCP connections on $port');

  Socket socket = await serverSocket.first;
  await _initProcessor(new TTcpSocket(socket));
}

Future _initProcessor(TSocket socket) async {
  TServerSocketTransport transport = new TServerSocketTransport(socket);
  _processor = new CalculadoraProcessor(new CalculadoraServer());
  _protocol = new TBinaryProtocol(transport);
  await _protocol.transport.open();

  print('connected');

  await transport.onIncomingMessage.listen(_processMessage);
}

Future _processMessage(_) async {
  _processor.process(_protocol, _protocol);
}

class CalculadoraServer implements Calculadora {
  final _log = {};

  Future ping() async {
    print('ping()');
  }

  @override
  Future<int> suma(int num1, int num2) async {
    print("Suma");
    return num1 + num2;
  }

  @override
  Future<double> cociente(int num1, int num2) async {
    print("Cociente");
    return (num1 * 1.0) / (num2 * 1.0);
  }

  @override
  Future<int> producto(int num1, int num2) async {
    print("Producto");
    return num1 * num2;
  }

  @override
  Future<int> resta(int num1, int num2) async {
    print("Resta");
    return num1 - num2;
  }
}
