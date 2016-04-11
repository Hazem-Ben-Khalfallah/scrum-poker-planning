var dashboardController = angular.module('dashboardController', []);

dashboardController.controller('dashboardCtrl',
    ['$scope', '$location', '$sessionStorage', '$log', 'sessionFactory',
        function ($scope, $location, $sessionStorage, $log, sessionFactory) {
            function init() {
                $scope.show_modal = false;
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
                        sanitizedValue = value.trim();
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

            $scope.openModal = function () {
                $scope.show_modal = true;
            };

            $scope.closeModal = function () {
                $scope.show_modal = false;
            };

            $scope.save = function (username) {
                $sessionStorage.sessionId = generateIdSession();
                $sessionStorage.username = username;
                var data = {
                    sessionId: $sessionStorage.sessionId,
                    sprintName: $scope.sprintName,
                    username: username,
                    cardSet: $scope.cardSet,
                    stories: $scope.stories
                };

                sessionFactory.create(data, function (response) {
                    $scope.closeModal();
                    $location.path('/home/' + $sessionStorage.sessionId);
                });
            };

            function generateIdSession() {
                var hash = "";
                var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

                for (var i = 0; i < 10; i++) {
                    hash += possible.charAt(Math.floor(Math.random() * possible.length));
                }

                return hash;
            }

            init();
        }]);