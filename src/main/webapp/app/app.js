var countryApp = angular.module('scrumPokApp', [
  'ngRoute',
  'homeControllers',
  'homeFactory',
  'homeDirective',
  'ui.bootstrap'
]);

countryApp.config(function($routeProvider) {
  $routeProvider.
    when('/', {
      templateUrl: 'app/components/home/home.html',
      controller: 'homeListCtrl'
    }).
    otherwise({
      redirectTo: '/'
    });
});