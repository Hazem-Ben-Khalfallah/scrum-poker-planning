var homeController = angular.module('homeController', []);

homeController.controller('homeCtrl', ['$http', '$log', '$scope', '$sessionStorage', '$location', 'webSocketFactory', 'homeFactory',
    function ($http, $log, $scope, $sessionStorage, $location, webSocketFactory, homeFactory) {
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
            webSocketFactory.send("remove_card", function (payload, headers, res) {

            }, {
                sessionId: $sessionStorage.sessionId,
                username: $sessionStorage.username,
                ticketName: $scope.ticketName,
                idCard: card.idCard
            });
        };

        $scope.addToChosen = function (index, card) {
            webSocketFactory.send("add_card", function (payload, headers, res) {
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
            webSocketFactory.send("create_ticket", function (payload, headers, res) {
                $log.info(payload.body);

            }, {
                ticketName: tName,
                sessionId: $sessionStorage.sessionId
            });
        };

        $scope.logout = function () {
            $sessionStorage.$reset();
            webSocketFactory.disconnect();
            $location.path('/login');
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