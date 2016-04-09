var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($http, $log, $scope, $sessionStorage, $location, DATA, Services) {
    $scope.username = $sessionStorage.username;
    $scope.sessionId = $sessionStorage.sessionId;
    $scope.isAdmin = $sessionStorage.isAdmin;
    $scope.color = $sessionStorage.color;
    $scope.ticketName = "";
    $scope.cardsMine = [];
    $scope.tickets = [];

    function init() {
        console.log($sessionStorage.username);
        if ($sessionStorage.username === undefined || $sessionStorage.sessionId === undefined) {
            $location.path('/login');
        }

        Services.subscribe("load_data", function (payload, headers, res) {
            console.log("-------------");
            console.log(payload);
            if (payload.statusCode == "OK") {
                $scope.tickets = payload.body.tickets;
            }

            $scope.$apply();
        });

    }

    $scope.removeFromChosen = function (index, card) {
        console.log("-----------------------------");
        console.log("remove from chosen: " + card);
        console.log("chosen: " + $scope.cardsChosen.length);
        console.log("mine: " + $scope.cardsMine.length);
        Services.send("remove_card", function (payload, headers, res) {

        }, {
            sessionId: $sessionStorage.sessionId,
            username: $sessionStorage.username,
            ticketName: $scope.ticketName,
            idCard: card.idCard
        });
    };

    $scope.addToChosen = function (index, card) {
        console.log("-----------------------------");
        console.log("add to chosen: " + card);
        console.log("chosen: " + $scope.cardsChosen.length);
        console.log("mine: " + $scope.cardsMine.length);
        Services.send("add_card", function (payload, headers, res) {
            console.log(payload.body);
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
            console.log(payload.body);

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
});

homeControllers.resolve = {
    ws: ['Services', '$q', function (Services, $q) {
        var delay = $q.defer();
        Services.connect();
        delay.resolve();
        return delay.promise;
    }]
};