"use strict";
/*@ngInject*/
angular.module('webSocketFactory', [])
    .factory('webSocketFactory', function ($stomp, $log) {

        var endpoint = '/WebSocketServices',
            topicPrefix = '/topic',
            destinationPrefix = '/app';

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
            $stomp.connect(endpoint)
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