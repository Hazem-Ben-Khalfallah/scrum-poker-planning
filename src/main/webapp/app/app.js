var countryApp = angular.module('scrumPokApp', [
  'ngRoute',
  'homeControllers',
  'homeFactory',
  'homeDirective',
  'loginControllers',
  'loginFactory',
  'ui.bootstrap'
]);

countryApp.config(function($routeProvider) {
  $routeProvider.
    when('/', {
      templateUrl: 'app/components/home/home.html',
      controller: 'homeListCtrl'
    }).
    when('/login', {
        templateUrl: 'app/components/login/login.html',
        controller: 'homeListCtrl'
      }).
    otherwise({
      redirectTo: '/'
    });
});