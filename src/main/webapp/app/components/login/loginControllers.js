var loginControllers = angular.module('loginControllers', []);

loginControllers.controller('loginCtrl', function ($scope,$http,$location,$stomp,$log,DATA){
	$scope.connecter=function(username,isAdmin){
		DATA.user.username=username;
		DATA.user.isAdmin=isAdmin;
		$location.path('/home'); 
   }
});