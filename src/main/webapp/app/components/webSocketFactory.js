angular.module('webSocketFactory', [])
    .factory('webSocketFactory', ['$stomp', '$log',
        function ($stomp, $log) {

            var endpoint = '/WebSocketServices',
                topicPrefix = '/topic',
                destinationPrefix = '/app';

            var send = function (topic, callback, data) {
                if ($stomp.stomp != null) {
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
                if ($stomp.stomp != null) {
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
                $log.info('connect ws!!');
                $stomp.connect(endpoint)
                    .then(function (frame) {
                        if (callback) {
                            callback(frame);
                        }
                    });
            };

            var disconnect = function () {
                if ($stomp.stomp != null) {
                    $stomp.disconnect();
                }
            };

            return {
                send: send,
                subscribe: sub,
                disconnect: disconnect,
                connect: connect
            }
        }]);