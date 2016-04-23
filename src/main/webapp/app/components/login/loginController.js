var loginController = angular.module('loginController', []);

loginController.controller('loginCtrl', ['$scope', '$location', '$sessionStorage', '$log', 'userFactory', 'webSocketFactory',
    function ($scope, $location, $sessionStorage, $log, userFactory, webSocketFactory) {

        function init() {
            $sessionStorage.$reset();
        }

        $scope.connect = function () {
            if (!$scope.username || !$scope.sessionId) {
                return;
            }

            var data = {
                username: $scope.username,
                sessionId: $scope.sessionId
            };
            userFactory.connect(data, function () {
                $sessionStorage.username = $scope.username;
                $sessionStorage.sessionId = $scope.sessionId;
                $location.path('/home/' + $sessionStorage.sessionId);
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

