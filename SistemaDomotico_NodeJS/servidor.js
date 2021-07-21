var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");
var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

var httpServer = http.createServer(
	function(request, response) {
		var uri = url.parse(request.url).pathname;
		if (uri=="/") uri = "/usuarios.html";
		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				console.log("Peticion invalida: "+uri);
				response.writeHead(200, {"Content-Type": "text/plain"});
				response.write('404 Not Found\n');
				response.end();
			}
		});
	}
);

MongoClient.connect("mongodb://localhost:27017/", { useUnifiedTopology: true }, function(err, db) {
	httpServer.listen(8080);
	var dbo = db.db("pruebaBaseDatos");
	var io = socketio(httpServer);
	var allClients = new Array();
	var estadoPersiana = false;
	var estadoAC = false;
	var info = "";
			dbo.createCollection("medidas", function (err,collection) {
				if (err) {
					var collection = dbo.collection("medidas");
				}
				io.sockets.on('connection',
			function(client) {
				allClients.push({address:client.request.connection.remoteAddress, port:client.request.connection.remotePort});
				console.log('New connection from ' + client.request.connection.remoteAddress + ':' + client.request.connection.remotePort);
				io.sockets.emit('all-connections', allClients);
				io.sockets.emit('actualizarActuadores',{persiana: estadoPersiana, aire: estadoAC});
				client.on('infoSensores', function(data){
					info = data;
					console.log(info);
					collection.insertOne(info, {safe:true}, function(err, result) {});
					console.log(collection.find().toArray());
					io.sockets.emit('actualizarSensores',info);
				});
				client.on('actualizarSensores', function(){
					io.sockets.emit('actualizarSensores',info);
				});
				client.on('alertatemperatura', function(data){
					if(data.limite == "max"){
						if (estadoAC == false) {
							estadoAC = true;
						}
						if (estadoPersiana == true) {
							estadoPersiana = false;
						}
						
					}
					if(data.limite == "min"){
						if (estadoAC == false) {
							estadoAC = true;
						}
					}
					io.sockets.emit('actualizarActuadores',{persiana: estadoPersiana, aire: estadoAC});
				});
				client.on('alertaluminosidad', function(data){
					if(data.limite == "max"){
						if (estadoPersiana == true) {
							estadoPersiana = false;
						}
					}
					if(data.limite == "min"){
						if (estadoPersiana == false) {
							estadoPersiana = true;
						}
					}
					io.sockets.emit('actualizarActuadores',{persiana: estadoPersiana, aire: estadoAC});
				});
				client.on('ac', function(){
					estadoAC = !estadoAC;
					console.log(estadoAC);
					io.sockets.emit('actualizarActuadores',{persiana: estadoPersiana, aire: estadoAC});
				});
				client.on('persiana', function(){
					estadoPersiana = !estadoPersiana;
					io.sockets.emit('actualizarActuadores',{persiana: estadoPersiana, aire: estadoAC});
				});
				client.on('alertas', function(data){
					io.sockets.emit('alertas',data);
				});
				client.on('stats', function(){
					const func = collection.find().toArray().then(function (regs) {
						var cuenta = regs.length;
						var minimoT = regs[0].temp;
						var maximoT = regs[0].temp;
						var minimoL = regs[0].lumi;
						var maximoL = regs[0].lumi;
						var mediaL = 0;
						var mediaT = 0;

						regs.forEach(element => {
							if (element.temp >= maximoT) {
								maximoT = element.temp;
							}
							if (element.temp <= minimoT) {
								maximoT = element.temp;
							}
							if (element.lumi >= maximoL) {
								maximoL = element.lumi;
							}
							if (element.lumi <= minimoL) {
								minimoL = element.lumi;
							}
							mediaL = mediaL + parseInt(element.lumi);
							mediaT = mediaT + parseInt(element.temp);

							
					});
					console.log(mediaL,mediaT);
					mediaL = mediaL*1.0 / cuenta;
					mediaT = mediaT*1.0 / cuenta;

					estadisticas = {media_lm: mediaL, media_tp:mediaT, maxlm: maximoL, maxtp: maximoT, minlm: minimoL, mintp: minimoT, cuenta: cuenta};
					
					io.sockets.emit('stats',estadisticas);
					});

					
				});
		
				client.on('disconnect', function() {
					console.log("El cliente "+client.request.connection.remoteAddress+" se va a desconectar");
					console.log(allClients);
		
					var index = -1;
					for(var i = 0; i<allClients.length;i++){
						//console.log("Hay "+allClients[i].port);
						if(allClients[i].address == client.request.connection.remoteAddress
							&& allClients[i].port == client.request.connection.remotePort){
							index = i;
						}			
					}
		
					if (index != -1) {
						allClients.splice(index, 1);
						io.sockets.emit('all-connections', allClients);
					}else{
						console.log("EL USUARIO NO SE HA ENCONTRADO!")
					}
					console.log('El usuario '+client.request.connection.remoteAddress+' se ha desconectado');
				});
			}
		);
			});
	});

	



console.log("Servicio Socket.io iniciado");

