'use strict';

// Module.
angular.module('app.home', []);

// Config.
angular.module('app.home').config(function ($stateProvider) {
    $stateProvider.state('home', {
        url: '/home',
        templateUrl: 'templates/home/main.html',
        controller: 'HomeCtrl'
    });
});

// Controller.
angular.module('app.home').controller('HomeCtrl', ['$scope',
    function ($scope) {

    }]);

