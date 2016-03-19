var loginControllers = angular.module('loginControllers', []);

loginControllers.controller('loginCtrl', function ($scope,$location,$sessionStorage,$stomp,$log,DATA){
	console.log("username:"+$sessionStorage.username);
	console.log("sessionId:"+$sessionStorage.sessionId);
	if($sessionStorage.username && $sessionStorage.sessionId){
		$location.path('/home/'+$sessionStorage.sessionId); 
	}
	$scope.connecter=function(username,sessionId){
		$sessionStorage.username=username;
		$sessionStorage.sessionId=sessionId;
		$location.path('/home/'+$sessionStorage.sessionId); 
	}
	var c = 1;
	$scope.generateIdSession=function(){
		  var d = new Date(),
	        m = d.getMilliseconds() + "",
	        u = ++d + m + (++c === 10000 ? (c = 1) : c);
		  $scope.sessionId=u;
	}
});

