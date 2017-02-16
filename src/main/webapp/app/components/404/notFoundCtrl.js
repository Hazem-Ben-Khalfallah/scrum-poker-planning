var notFoundCtrl = angular.module('notFoundCtrl', []);

notFoundCtrl.controller('notFoundCtrl', ['$scope','$location',
    function ($scope, $location) {

        $scope.home = function () {
            $location.path('/static/login');
        };
    }]);