var myApp = angular.module('scrumPokApp', [
    'ngRoute',
    'homeController',
    'homeFactory',
    'loginController',
    'dashboardController',
    'dashboardFactory',
    'ui.bootstrap',
    'ngStomp',
    'ngStorage',
    'webSocketFactory'
]);

myApp.config(function ($routeProvider) {
    $routeProvider.
        when('/home/:sessionId', {
            templateUrl: 'app/components/home/home.html',
            controller: 'homeCtrl'
        }).
        when('/login', {
            templateUrl: 'app/components/login/login.html',
            controller: 'loginCtrl',
            resolve: loginController.resolve
        }).
        when('/dashboard', {
            templateUrl: 'app/components/dashboard/dashboard.html',
            controller: 'dashboardCtrl'
        }).
        otherwise({
            redirectTo: '/login'
        });
});