var dashboardController = angular.module('dashboardController', []);

dashboardController.controller('dashboardCtrl', ['$scope', '$location', '$sessionStorage', '$log', 'DATA', 'Services',
    function ($scope, $location, $sessionStorage, $log, DATA, Services) {
        function init() {
            $scope.stories = [];
            $scope.cardSet = 'time';
        }

        $scope.convertToStories = function () {
            $scope.stories = [];
            var sanitizedValue;
            if ($scope.rowStories) {
                var values = $scope.rowStories
                    .replace("\r\n", "\n")
                    .split("\n");
                angular.forEach(values, function (value) {
                    var sanitizedValue = value.trim();
                    if (sanitizedValue.length > 0) {
                        if ($scope.stories.indexOf(sanitizedValue) < 0) {
                            $scope.stories.push(sanitizedValue);
                        }
                    }
                });
            }
        };

        $scope.remove = function (story) {
            var index = $scope.stories.indexOf(story);
            if (index >= 0) {
                $scope.stories.splice(index, 1);
            }
        };

        $scope.cancel = function () {
            $location.path('/login');
        };

        $scope.save = function () {
            $log.info($scope.sprintName);
            $log.info($scope.cardSet);
            $log.info($scope.stories);
        };

        $scope.generateIdSession = function () {
            var hash = "";
            var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            for (var i = 0; i < 10; i++) {
                hash += possible.charAt(Math.floor(Math.random() * possible.length));
            }

            $scope.sessionId = hash;
        };

        init();
    }]);