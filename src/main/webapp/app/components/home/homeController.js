var homeController = angular.module('homeController', []);

homeController.controller('homeCtrl',
    ['$http', '$log', '$scope', '$sessionStorage', '$location', 'webSocketFactory', 'storyFactory', 'session', 'users', 'stories',
        function ($http, $log, $scope, $sessionStorage, $location, webSocketFactory, storyFactory, session, users, stories) {

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
                $scope.newStory = {};

                $scope.username = $sessionStorage.username;
                $scope.sessionId = $sessionStorage.sessionId;

                //get users
                $scope.users = users;

                //get session info
                $scope.sprintName = session.sprintName;
                if (session.cardSet == 'time') {
                    $scope.cards = cards.time;
                } else if (session.cardSet == 'fibonacci') {
                    $scope.cards = cards.fibonacci;
                } else {
                    $scope.cards = cards.modifiedFibonacci;
                }

                //get stories
                $scope.stories = stories;
                // set current story
                $scope.setCurrentStory(0);

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
                $scope.currentStory = $scope.stories.length > 0 ? $scope.stories[index] : {storyName: '-', order: 0};
            };

            $scope.removeStory = function (story) {
                storyFactory.remove(story.storyId, function (data) {
                    if (data.status === 'OK') {
                        $scope.stories.splice($scope.getIndex(story), 1);
                    }
                });
            };

            $scope.addStory = function () {
                $scope.newStory.sessionId = $scope.sessionId;
                $scope.newStory.order = $scope.stories.length + 1;
                storyFactory.create($scope.newStory, function (data) {
                    $log.info(data);
                    $scope.stories.push(data);
                    $scope.newStory = {};
                });
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
                        user.hasVoted = !!$scope.selectedCard;
                    }
                });
            }

            init();
        }]);

homeController.resolve = {
    session: ['sessionFactory', '$sessionStorage', '$q', function (sessionFactory, $sessionStorage, $q) {
        var delay = $q.defer();
        sessionFactory.get($sessionStorage.sessionId, function (data) {
            delay.resolve(data);
        });
        return delay.promise;
    }],
    users: ['userFactory', '$sessionStorage', '$q', function (userFactory, $sessionStorage, $q) {
        var delay = $q.defer();
        userFactory.get($sessionStorage.sessionId, function (data) {
            delay.resolve(data);
        });
        return delay.promise;
    }],
    stories: ['storyFactory', '$sessionStorage', '$q', function (storyFactory, $sessionStorage, $q) {
        var delay = $q.defer();
        storyFactory.get($sessionStorage.sessionId, function (data) {
            delay.resolve(data);
        });
        return delay.promise;
    }],
    ws: ['webSocketFactory', '$q', function (webSocketFactory, $q) {
        var delay = $q.defer();
        webSocketFactory.connect();
        delay.resolve();
        return delay.promise;
    }]
};