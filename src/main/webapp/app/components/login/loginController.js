var loginController = angular.module('loginController', []);

loginController.controller('loginCtrl', ['$scope', '$location', '$localStorage', '$log', 'userFactory',
    function ($scope, $location, $localStorage, $log, userFactory) {

        function init() {
            if ($localStorage.currentUser) {
                $location.path('/home/' + $localStorage.currentUser.sessionId);
            }
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
                $location.path('/home/' + $localStorage.currentUser.sessionId);
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

