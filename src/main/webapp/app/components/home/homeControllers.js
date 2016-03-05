var countryControllers = angular.module('homeControllers', []);

countryControllers.controller('homeListCtrl', function ($scope, countries){
  countries.list(function(countries) {
    $scope.countries = countries;
  });
});

countryControllers.controller('homeDetailCtrl', function ($scope, $routeParams, countries){
  countries.find($routeParams.countryId, function(country) {
    $scope.country = country;
  });
});