const { WebSocketServer } = require('ws');
const fs = require('fs');
const { port } = require('./config');

const wss = new WebSocketServer({ port: port });

wss.broadcast = function broadcast(msg) {
    console.log("Sending to all: ", msg)
    wss.clients.forEach(function each(client) {
        client.send(msg.toString());
     });
 };

wss.on('connection', function connection(ws) {

    console.log("Client connected!", ws._socket.remoteAddress);

    ws.on('message', function message(data) {
        try {
            wss.broadcast(data)
        } catch (e) {
            console.error(e);
        }
    });

    ws.on('close', function (event) {
        console.log("Closed connection with", ws._socket.remoteAddress)
    })
});