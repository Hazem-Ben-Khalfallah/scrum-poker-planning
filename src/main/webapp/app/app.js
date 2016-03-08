var myApp = angular.module('scrumPokApp', [
  'ngRoute',
  'homeControllers',
  'homeFactory',
  'homeDirective',
  'loginControllers',
  'loginFactory',
  'ui.bootstrap'
]);

myApp.config(function($routeProvider) {
  $routeProvider.
    when('/home', {
      templateUrl: 'app/components/home/home.html',
    }).
    when('/login', {
        templateUrl: 'app/components/login/login.html'
    }).
    otherwise({
      redirectTo: '/login'
    });
});


myApp.factory('DATA', function(){
  return {
    user: {
      username: '',
      id:'',
      admin:false
    }
  };
});