var myApp = angular.module('scrumPokApp', [
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
    'homeController'
]);

myApp.config(function ($routeProvider, $locationProvider) {
    // use the HTML5 History API
    $locationProvider.html5Mode(true);

    $routeProvider
        .when('/static/home/:sessionId', {
            templateUrl: 'app/components/home/home.html',
            controller: 'homeCtrl',
            resolve: homeController.resolve
        })
        .when('/static/login', {
            templateUrl: 'app/components/login/login.html',
            controller: 'loginCtrl',
            resolve: loginController.resolve
        })
        .when('/static/dashboard', {
            templateUrl: 'app/components/dashboard/dashboard.html',
            controller: 'dashboardCtrl'
        })
        .otherwise({
            redirectTo: '/static/login'
        });
});