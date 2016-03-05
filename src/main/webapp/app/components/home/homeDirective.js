angular.module('homeDirective', [])
       .directive('home', function(){
  return {
    scope: { home: '=' },
    restrict: 'A',
    templateUrl: 'app/components/home/country.html',
    controller: function($scope, countries){
      countries.find($scope.country.id, function(country) {
        $scope.flagURL = country.flagURL;
      });
    }
  };
});