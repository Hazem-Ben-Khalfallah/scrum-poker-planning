var loginController = angular.module('loginController', []);

loginController.controller('loginCtrl', ['$scope', '$location', '$localStorage', '$log', 'userFactory',
    function ($scope, $location, $localStorage, $log, userFactory) {

        function init() {
            if ($localStorage.currentUser) {
                $location.path('/static/home/' + $localStorage.currentUser.sessionId);
            }
        }

        $scope.hideMassage = function () {
            $scope.messageVisible = false;
        };


        $scope.showMassage = function (message) {
            $scope.message = message;
            $scope.messageVisible = true;
        };

        $scope.connect = function () {
            $scope.hideMassage();

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
                $location.path('/static/home/' + $localStorage.currentUser.sessionId);
            }, function (error, status) {
                $scope.loading = false;
                $scope.showMassage(error.exception);
            });

        };

        $scope.newSession = function () {
            $location.path('/static/dashboard');
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

