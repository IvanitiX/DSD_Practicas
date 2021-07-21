var serviceURL = 'localhost:8080';
var socket = io.connect(serviceURL);

socket.on('connect', function(){
    console.log("Nos hemos conectado :)")
});

function sendInfo(){
    var temperatura = document.getElementById("temp").value;
    var luminosidad = document.getElementById("lumens").value;
    var latitud = document.getElementById("latitud").value;
    var longitud = document.getElementById("longitud").value;
    var datetime = new Date();
    var timemark = datetime.toString();
    console.log(timemark);

    if (longitud >= -180 && longitud <= 180 && latitud >= -90 && latitud <= 90) {
        socket.emit('infoSensores', {temp:temperatura, lumi:luminosidad, timemark: datetime, lat: latitud, long:longitud});
    } else {
        socket.emit('infoSensores', {temp:temperatura, lumi:luminosidad, timemark: datetime});
    }

    

    document.getElementById("temp").value = "";
    document.getElementById("lumens").value = "";
    document.getElementById("latitud").value = "";
    document.getElementById("longitud").value = "";
}

