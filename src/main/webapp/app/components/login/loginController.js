var loginController = angular.module('loginController', []);

loginController.controller('loginCtrl', ['$scope', '$location', '$sessionStorage', '$log', 'userFactory', 'webSocketFactory',
    function ($scope, $location, $sessionStorage, $log, userFactory, webSocketFactory) {

        function init() {
            $sessionStorage.$reset();
        }

        $scope.connect = function () {
            if (!$scope.username || !$scope.sessionId || $scope.loading) {
                return;
            }
            $scope.loading = true;

            var data = {
                username: $scope.username,
                sessionId: $scope.sessionId
            };
            userFactory.connect(data, function () {
                $scope.loading = false;
                $sessionStorage.username = $scope.username;
                $sessionStorage.sessionId = $scope.sessionId;
                $location.path('/home/' + $sessionStorage.sessionId);
            }, function () {
                $scope.loading = false;
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

