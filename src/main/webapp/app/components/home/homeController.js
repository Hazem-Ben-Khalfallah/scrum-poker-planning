var homeController = angular.module('homeController', []);

homeController.controller('homeCtrl', ['$http', '$log', '$scope', '$sessionStorage', '$location', 'webSocketFactory', 'sessionFactory', 'userFactory',
    function ($http, $log, $scope, $sessionStorage, $location, webSocketFactory, sessionFactory, userFactory) {

        function init() {
            if (!$sessionStorage.username || !$sessionStorage.sessionId) {
                $location.path('/login');
            }

            $scope.min = 0;
            $scope.max = 8;

            $scope.info = {
                selected: 'users'
            };

            $scope.currentStory = {};

            $scope.username = $sessionStorage.username;
            $scope.sessionId = $sessionStorage.sessionId;

            userFactory.get($sessionStorage.sessionId, function (data) {
                $scope.users = data;
            });

            sessionFactory.get($sessionStorage.sessionId, function (data) {
                $scope.sprintName = data.sprintName;
                $scope.stories = data.stories;
                // set current story
                $scope.setCurrentStory(0);

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

        $scope.setCurrentStory = function (index) {
            $scope.currentStory.total = $scope.stories.length;
            $scope.currentStory.name = $scope.currentStory.total > 0 ? $scope.stories[index] : '-';
            $scope.currentStory.index = $scope.stories.indexOf($scope.currentStory.name) + 1;
        };

        $scope.selectCard = function (card) {
            if (!card.selected) {
                if ($scope.selectedCard) {
                    $scope.selectedCard.selected = false;
                    $scope.selectedCard.animate = 'move-down';
                }
                $scope.selectedCard = card;
                card.selected = true;
                card.animate = 'move-up';
            } else {
                card.selected = false;
                card.animate = 'move-down';
                $scope.selectedCard = angular.undefined;
            }
            highlightVote();
        };

        function highlightVote() {
            angular.forEach($scope.users, function (user) {
                if (user.name == $scope.username) {
                    if ($scope.selectedCard) {
                        user.hasVoted = true;
                    } else {
                        user.hasVoted = false;
                    }
                }
            });
        }

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