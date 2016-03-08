var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($scope,DATA){
	$scope.username = DATA.user.username;
});
