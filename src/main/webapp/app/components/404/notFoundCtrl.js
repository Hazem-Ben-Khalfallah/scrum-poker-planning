"use strict";
var notFoundCtrl = angular.module('notFoundCtrl', []);

/*@ngInject*/
notFoundCtrl.controller('notFoundCtrl', function ($scope, $location) {

    $scope.home = function () {
        $location.path('/static/login');
    };
});