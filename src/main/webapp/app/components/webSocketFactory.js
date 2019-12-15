"use strict";
/*@ngInject*/
angular.module('webSocketFactory', [])
    .factory('webSocketFactory', function ($stomp, $log) {

        var endpoint = '/WebSocketServices',
            topicPrefix = '/topic',
            destinationPrefix = '/app';

        // do not remove! will be inject from Gulp task
        // usage: gulp --ws WS_URL
        // setting ws url is optional
        var wsPort = '@@inject_ws_port' || window.location.port,
            wssPort = '@@inject_wss_port' || window.location.port,
            port = window.location.protocol === 'http:' ? wsPort : wssPort,
            host = window.location.hostname;

        if('${API_URL}'.indexOf('API_') < 0){
            host = '${API_URL}';
        }

        if('${API_PORT}'.indexOf('API_') < 0){
            port = '${API_PORT}';
        }

        var websocketUrl = window.location.protocol + '//' + host + ':' + port;

        var isConnected = function () {
            return $stomp.stomp != null && $stomp.stomp.connected;
        };

        var send = function (topic, callback, data) {
            if (isConnected()) {
                $stomp.subscribe(topicPrefix + '/' + topic, callback);
                $stomp.send(destinationPrefix + '/' + topic, data);
            } else {
                connect(function (frame) {
                    $stomp.setDebug(function (args) {
                        $log.debug(args)
                    });
                    send(topic, callback, data);
                });
            }
        };

        var sub = function (topic, callback) {
            if (isConnected()) {
                $stomp.subscribe(topicPrefix + '/' + topic, callback);
            } else {
                connect(function (frame) {
                    $stomp.setDebug(function (args) {
                        $log.debug(args)
                    });
                    sub(topic, callback);
                });
            }
        };


        var connect = function (callback) {
            $stomp.connect(websocketUrl + endpoint)
                .then(function (frame) {
                    if (callback) {
                        callback(frame);
                    }
                });
        };

        var disconnect = function () {
            if (isConnected()) {
                $stomp.disconnect();
            }
        };

        return {
            send: send,
            subscribe: sub,
            disconnect: disconnect,
            connect: connect
        }
    });