var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', ['$http', '$log', '$scope', '$sessionStorage', '$location', 'DATA', 'Services', 'homeFactory',
    function ($http, $log, $scope, $sessionStorage, $location, DATA, Services, homeFactory) {
        $scope.username = $sessionStorage.username;
        $scope.sessionId = $sessionStorage.sessionId;
        $scope.isAdmin = $sessionStorage.isAdmin;
        $scope.color = $sessionStorage.color;
        $scope.ticketName = "";
        $scope.cardsMine = [];
        $scope.tickets = [];

        function init() {
            $log.info($sessionStorage.username);
            if (!$sessionStorage.username || !$sessionStorage.sessionId) {
                $location.path('/login');
            }

            $scope.cardsMine = [{idCard: 1}];

            homeFactory.get($sessionStorage.sessionId, function (data) {
                    $log.info('----');
                    $log.info(data);
                    $scope.tickets = data.tickets;
                }
            );

        }

        $scope.removeFromChosen = function (index, card) {
            Services.send("remove_card", function (payload, headers, res) {

            }, {
                sessionId: $sessionStorage.sessionId,
                username: $sessionStorage.username,
                ticketName: $scope.ticketName,
                idCard: card.idCard
            });
        };

        $scope.addToChosen = function (index, card) {
            Services.send("add_card", function (payload, headers, res) {
                $log.info(payload.body);
            }, {
                sessionId: $sessionStorage.sessionId,
                username: $sessionStorage.username,
                ticketName: $scope.ticketName,
                idCard: card.idCard,
                color: $sessionStorage.color
            });
        };

        $scope.newTicket = function (tName) {
            Services.send("create_ticket", function (payload, headers, res) {
                $log.info(payload.body);

            }, {
                ticketName: tName,
                sessionId: $sessionStorage.sessionId
            });
        };

        $scope.getCardValue = function (idCard) {
            return DATA.cards[idCard]
        };

        $scope.logout = function () {
            $sessionStorage.$reset();
            Services.disconnect();
            $location.path('/login');
        };

        init();
    }]);

homeControllers.resolve = {
    ws: ['Services', '$q', function (Services, $q) {
        var delay = $q.defer();
        Services.connect();
        delay.resolve();
        return delay.promise;
    }]
};