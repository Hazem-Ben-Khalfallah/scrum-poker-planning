var loginControllers = angular.module('loginControllers', []);

loginControllers.controller('loginCtrl', function ($scope,$http,$location,DATA){
	$scope.connecter=function(username){
		DATA.user.username=username;
		console.log(DATA.user.username);
		$location.path('/home');
	}
});