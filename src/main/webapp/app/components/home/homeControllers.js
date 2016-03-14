var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($stomp,$log,$scope,DATA){
	$scope.username = DATA.user.username;
	});

