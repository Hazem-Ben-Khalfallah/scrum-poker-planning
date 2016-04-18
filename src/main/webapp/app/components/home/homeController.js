var homeController = angular.module('homeController', []);

var Types = {
    story: "story",
    user: "user",
    vote: "vote"
};

homeController.controller('homeCtrl',
    ['$http', '$log', '$scope', '$sessionStorage', '$location', 'webSocketFactory', 'storyFactory', 'voteFactory',
        'session', 'users', 'stories',
        function ($http, $log, $scope, $sessionStorage, $location, webSocketFactory, storyFactory, voteFactory,
                  session, users, stories) {

            function getVotes() {
                //get saved votes
                voteFactory.get($scope.currentStory.storyId, function (data) {
                    $scope.votes = data;
                    $scope.currentVote = angular.undefined;

                    for (var i = 0, len = $scope.votes.length; i < len; i++) {
                        if ($scope.votes[i].username == $scope.currentUser.username) {
                            $scope.currentVote = $scope.votes[i];
                            break;
                        }
                    }

                    highlightCard();
                    highlightVotes();
                })
            }

            function init() {
                if (!$sessionStorage.username || !$sessionStorage.sessionId) {
                    $location.path('/login');
                }

                $scope.min = 0;
                $scope.max = 8;

                $scope.info = {
                    selected: 'users'
                };

                $scope.votes = [];

                $scope.currentStory = {};
                $scope.newStory = {};

                $scope.currentVote = {};

                $scope.username = $sessionStorage.username;
                $scope.sessionId = $sessionStorage.sessionId;

                //get users
                $scope.users = users;
                //get current user
                angular.forEach($scope.users, function (user) {
                    if (user.username == $scope.username) {
                        $scope.currentUser = user;
                    }
                });

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
                $scope.setCurrentStory($scope.stories[0]);
            }

            $scope.logout = function () {
                $sessionStorage.$reset();
                webSocketFactory.disconnect();
                $location.path('/login');
            };

            $scope.getIndex = function (type, item) {
                if (type === Types.story)
                    return $scope.stories.indexOf(item);

                if (type === Types.vote) {
                    for (var i = 0, len = $scope.votes.length; i < len; i++) {
                        if ($scope.votes[i].voteId === item.voteId) {
                            return i;
                        }
                    }
                    return -1;
                }

                if (type === Types.user)
                    return $scope.users.indexOf(item);
            };

            $scope.setCurrentStory = function (story) {
                $scope.currentStory = story;
                //reset votes
                animateCard($scope.selectedCard);
                //get votes from backend
                getVotes();
            };

            $scope.removeStory = function (story) {
                storyFactory.remove(story.storyId, function (data) {
                    if (data.status === 'OK') {
                        $scope.stories.splice($scope.getIndex(Types.story, story), 1);
                    }
                });
            };

            $scope.addStory = function () {
                $scope.newStory.sessionId = $scope.sessionId;
                $scope.newStory.order = $scope.stories.length + 1;
                storyFactory.create($scope.newStory, function (data) {
                    $scope.stories.push(data);
                    $scope.newStory = {};
                });
            };

            $scope.createVote = function (card) {
                var data = {
                    sessionId: $scope.sessionId,
                    storyId: $scope.currentStory.storyId,
                    username: $scope.currentUser.username,
                    value: card.id
                };
                if ($scope.currentVote) {
                    data.voteId = $scope.currentVote.voteId;
                }

                voteFactory.create(data, function (response) {
                    var index = $scope.getIndex(Types.vote, response);
                    if (index < 0) {
                        $scope.votes.push(response);
                    } else {
                        $scope.votes[index] = response;
                    }

                    $scope.currentVote = response;
                    animateCard(card);
                    highlightVotes();
                })
            };

            $scope.removeVote = function (card) {
                voteFactory.remove($scope.currentVote.voteId, function (response) {
                    if (response.status === 'OK') {
                        $scope.votes.splice($scope.getIndex(Types.vote, $scope.currentVote), 1);
                        $scope.currentVote = {};
                        animateCard(card);
                        highlightVotes();
                    }
                })
            };


            $scope.selectCard = function (card) {
                if ($scope.currentStory.ended)
                    return;
                if (!card.selected) {
                    $scope.createVote(card);
                } else {
                    $scope.removeVote(card);
                }

                highlightVotes();
            };

            $scope.endVote = function () {
                if (!$scope.currentStory.ended)
                    return;
                storyFactory.endStory($scope.currentStory.storyId, function (response) {
                    if (response.status !== 'OK') {
                        $scope.currentStory.ended = false;
                    }
                });
            };

            $scope.getCard = function (id) {
                var selectCard;
                for (var i = 0, len = $scope.cards.length; i < len; i++) {
                    if ($scope.cards[i].id == id) {
                        selectCard = $scope.cards[i];
                        break;
                    }
                }

                return selectCard;
            };

            $scope.getColor = function (vote) {
                if(!vote){
                    return {};
                }
                var field = $scope.getCard(vote).color,
                    color = {};
                color[field] = $scope.currentStory.ended;
                return color;
            };

            function animateCard(card) {
                if (!card)
                    return;
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
            }

            function highlightVotes() {
                var vote;
                angular.forEach($scope.users, function (user) {
                    user.hasVoted = false;
                    user.vote = angular.undefined;
                    for (var i = 0, len = $scope.votes.length; i < len; i++) {
                        vote = $scope.votes[i];
                        if (vote.username == user.username) {
                            user.hasVoted = true;
                            user.vote = vote.value;
                            break;
                        }
                    }

                });
            }

            function highlightCard() {
                for (var i = 0, len = $scope.votes.length; i < len; i++) {
                    if ($scope.votes[i].username == $scope.currentUser.username) {
                        var card = $scope.getCard($scope.votes[i].value);
                        animateCard(card);
                        break;
                    }
                }
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