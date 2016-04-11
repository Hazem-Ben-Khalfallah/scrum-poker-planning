var homeController = angular.module('homeController', []);

homeController.controller('homeCtrl', ['$http', '$log', '$scope', '$sessionStorage', '$location', 'webSocketFactory', 'homeFactory',
    function ($http, $log, $scope, $sessionStorage, $location, webSocketFactory, homeFactory) {

        function init() {
            if (!$sessionStorage.username || !$sessionStorage.sessionId) {
                $location.path('/login');
            }

            $scope.info = {
                selected: 'users'
            };

            $scope.currentStory = {};

            $scope.username = $sessionStorage.username;
            $scope.sessionId = $sessionStorage.sessionId;
            $scope.users = [
                {name: 'hazem'}, {name: 'khaireddine'}, {name: 'nico'}, {name: 'Seif'},
                {name: 'Raed'}, {name: 'aymen'}, {name: 'ahmed'}
            ];

            homeFactory.get($sessionStorage.sessionId, function (data) {
                $log.info(data);
                $scope.sprintName = data.sprintName;
                $scope.stories = data.stories;
                // set current story
                $scope.selectStory(0);

                if (data.cardSet == 'time') {
                    $scope.cards = cards.time;
                } else if (data.cardSet == 'fibonacci') {
                    $scope.cards = cards.fibonacci;
                } else {
                    $scope.cards = cards.modifiedFibonacci;
                }
            });
        }

        $scope.logout = function () {
            $sessionStorage.$reset();
            webSocketFactory.disconnect();
            $location.path('/login');
        };

        $scope.getIndex = function (story) {
            return $scope.stories.indexOf(story);
        };

        $scope.selectStory = function (index) {
            $scope.currentStory.total = $scope.stories.length;
            $scope.currentStory.name = $scope.currentStory.total > 0 ? $scope.stories[index] : '-';
            $scope.currentStory.index = $scope.stories.indexOf($scope.currentStory.name) + 1;
        };

        $scope.selectCard = function (card) {
            if (!card.selected) {
                card.selected = true;
                card.animate = 'move-up';
            } else {
                card.selected = false;
                card.animate = 'move-down';
            }
        };

        init();
    }]);

homeController.resolve = {
    ws: ['webSocketFactory', '$q', function (webSocketFactory, $q) {
        var delay = $q.defer();
        webSocketFactory.connect();
        delay.resolve();
        return delay.promise;
    }]
};