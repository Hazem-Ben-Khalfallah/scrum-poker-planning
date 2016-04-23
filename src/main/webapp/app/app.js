var myApp = angular.module('scrumPokApp', [
    'ngRoute',
    '$httpWrapper',
    'angularify.semantic.modal',
    'ui.bootstrap',
    'ngStomp',
    'ngStorage',
    'ngEnter',
    'webSocketFactory',
    'sessionFactory',
    'voteFactory',
    'userFactory',
    'storyFactory',
    'sessionFactory',

    'loginController',
    'dashboardController',
    'homeController'
]);

myApp.config(function ($routeProvider) {
    $routeProvider.
        when('/home/:sessionId', {
            templateUrl: 'app/components/home/home.html',
            controller: 'homeCtrl',
            resolve: homeController.resolve
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