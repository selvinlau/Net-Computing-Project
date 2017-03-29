

const http = require('http');

var express = require('express');
const Router = require('router');
const finalhandler = require('finalhandler');
const app = new Router();
const view = require('consolidate');
const path = require('path');
var net = require('net');

//sample server for testing socket
/*
var serverSocket = net.createServer(function(socket) {
	socket.write('Echo server\r\n');
	socket.pipe(socket);
});

serverSocket.listen(1337, '127.0.0.1');
*/


app.use(express.static(__dirname));

app.use((req, res, next) => {
	res.render = function render(filename, params) {
		var file = path.resolve(__dirname + '/views', filename);
		view.mustache(file, params || {}, function(err, html) {
			if (err) { return next(err); }
			res.setHeader('Content-Type', 'text/html');
			res.end(html);
		});
	};
	next();
});

app.get('/', (req, res) => {
	res.render('dashboard.html')
});

app.get('/dashboard', (req, res) => {
	
	
	//Send data to dashboard.html
	res.render('dashboard.html', {
		obj: []
	});
});

app.get('/chart', (req, res) => {
/*	//sample JSON 
	var str = '[{"id":{"timestamp":1490285755,"machineIdentifier":4487081,"processIdentifier":17584,"counter":7635585},"name":"Server1","className":"entity.Device","temperature":100.5999984741211,"timestamp":12345},{"id":{"timestamp":1490286095,"machineIdentifier":4487081,"processIdentifier":17900,"counter":2810230},"name":"Server1","className":"entity.Device","temperature":100.5999984741211,"timestamp":12345},{"id":{"timestamp":1490286097,"machineIdentifier":4487081,"processIdentifier":17900,"counter":2810232},"name":"Server1","className":"entity.Device","temperature":100.5999984741211,"timestamp":12345},{"id":{"timestamp":1490286098,"machineIdentifier":4487081,"processIdentifier":17900,"counter":2810234},"name":"Server1","className":"entity.Device","temperature":100.5999984741211,"timestamp":12345}]';
	var obj = JSON.parse(str);
*/
	// ===== CALL API REST SERVER =============
	var request = require('request');
	request('http://localhost:4567/getAllDevices', function (error, response, body) {
	  	console.log('error:', error); // Print the error if one occurred
	  	console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
		
	    obj = JSON.parse(body);
		res.render('chart.html', {
			json: obj
		});
	});
	
	// ===== CALL API REST SERVER =============

	
});


const server = http.createServer();
server.on('request', (req, res) => {
	app(req, res, finalhandler(req, res));
});

server.listen(process.env.PORT || 3000);


