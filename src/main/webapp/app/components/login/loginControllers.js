var loginControllers = angular.module('loginControllers', []);

loginControllers.controller('loginCtrl', function ($scope,$location,$sessionStorage,$stomp,$log,DATA,Services){
	console.log("username:"+$sessionStorage.username);
	console.log("sessionId:"+$sessionStorage.sessionId);

	if($sessionStorage.username && $sessionStorage.sessionId){
		$location.path('/home/'+$sessionStorage.sessionId); 
	}
	$scope.connecter=function(username,sessionId){
		Services.sub_send("connect","connect",function (payload, headers, res) {
			if(payload.statusCode=="OK"){
				console.log(payload);
				$sessionStorage.username=username;
				$sessionStorage.sessionId=sessionId;
				$sessionStorage.isAdmin=payload.body.isAdmin;
				$sessionStorage.color=payload.body.color;
				console.log("#########:"+payload.body.color);
				$location.path('/home/'+$sessionStorage.sessionId); 
				$scope.$apply();
			}else{
				console.log("probleme in connection");
			}
		},{
			username:username,
			sessionId:sessionId
		});		
	}
	var c = 1;
	$scope.generateIdSession=function(){
		  var d = new Date(),
	        m = d.getMilliseconds() + "",
	        u = ++d + m + (++c === 10000 ? (c = 1) : c);
		  $scope.sessionId=u;
	}
});

