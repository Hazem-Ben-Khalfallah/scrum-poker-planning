var loginController = angular.module('loginController', []);

loginController.controller('loginCtrl', ['$scope', '$location', '$sessionStorage', '$log', 'webSocketFactory',
    function ($scope, $location, $sessionStorage, $log, webSocketFactory) {

        function init() {
            $sessionStorage.$reset();
        }

        $scope.connect = function () {
            webSocketFactory.send("connect", function (payload, headers, res) {
                if (payload.statusCode == "OK") {
                    $log.info(payload);
                    $sessionStorage.username = $scope.username;
                    $sessionStorage.sessionId = $scope.sessionId;
                    $sessionStorage.isAdmin = payload.body.isAdmin;
                    $location.path('/home/' + $sessionStorage.sessionId);
                    $scope.$apply();
                } else {
                    $log.info("problem in connection");
                }
            }, {
                username: $scope.username,
                sessionId: $scope.sessionId
            });
        };

        $scope.newSession = function () {
            $location.path('/dashboard');
        };

        init();
    }]);

loginController.resolve = {
    ws: ['webSocketFactory', '$q', function (webSocketFactory, $q) {
        var delay = $q.defer();
        webSocketFactory.connect();
        delay.resolve();
        return delay.promise;
    }]
};

