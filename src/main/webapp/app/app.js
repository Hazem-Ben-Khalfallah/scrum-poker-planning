var myApp = angular.module('scrumPokApp', [
    'ngRoute',
    'homeControllers',
    'homeFactory',
    'homeDirective',
    'loginControllers',
    'loginFactory',
    'ui.bootstrap',
    'ngStomp',
    'ngStorage'
]);

myApp.config(function ($routeProvider) {
    $routeProvider.
        when('/home/:sessionId', {
            templateUrl: 'app/components/home/home.html',
            controller: 'homeCtrl'
        }).
        when('/login', {
            templateUrl: 'app/components/login/login.html',
            controller: 'loginCtrl',
            resolve: loginControllers.resolve
        }).
        otherwise({
            redirectTo: '/login'
        });
});

myApp.factory('Services', function ($stomp, $log) {
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
        $stomp.disconnect();
    };

    return {
        send: send,
        subscribe: sub,
        disconnect: disconnect,
        connect: connect
    }
});

myApp.factory('DATA', function () {
    return {
        websocket: null,
        user: {
            username: '',
            isAdmin: false
        },
        listUsers: [],
        notExistant: function (liste, user) {
            for (var i = 0; i < liste; i++) {
                if (liste[i].username === user) {
                    return false;
                }
            }
            return true;
        },
        cards: {
            "1": "0",
            "2": "1/2",
            "3": "1",
            "4": "2",
            "5": "3",
            "6": "5",
            "7": "8",
            "8": "13",
            "9": "20",
            "10": "40",
            "11": "100",
            "12": "?"
        }
    };
});




