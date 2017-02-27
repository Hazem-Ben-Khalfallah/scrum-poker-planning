"use strict";
var myApp = angular.module('scrumPokerApp', [
    'ngRoute',
    '$httpWrapper',
    'angularify.semantic.modal',
    'ngStomp',
    'ngStorage',
    'ngEnter',
    'ngMessage',
    'storiesFilter',
    'webSocketFactory',
    'sessionFactory',
    'voteFactory',
    'userFactory',
    'storyFactory',
    'sessionFactory',

    'loginController',
    'dashboardController',
    'homeController',
    'notFoundCtrl'
]);

/*@ngInject*/
myApp.config(function ($routeProvider, $locationProvider) {
    // use the HTML5 History API
    $locationProvider.html5Mode(true);

    $routeProvider
        .when('/static/home/:sessionId', {
            template: TEMPLATES['app/components/home/home.html'],
            controller: 'homeCtrl',
            resolve: homeController.resolve
        })
        .when('/static/login', {
            template: TEMPLATES['app/components/login/login.html'],
            controller: 'loginCtrl',
            resolve: loginController.resolve
        })
        .when('/static/dashboard', {
            template: TEMPLATES['app/components/dashboard/dashboard.html'],
            controller: 'dashboardCtrl'
        })
        .when('/static/404', {
            template: TEMPLATES['app/components/404/404.html'],
            controller: 'notFoundCtrl'
        })
        .when('/', {
            redirectTo: '/static/login'
        })
        .otherwise({
            redirectTo: '/static/404'
        });
});