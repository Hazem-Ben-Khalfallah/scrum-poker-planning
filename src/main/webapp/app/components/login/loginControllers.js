var loginControllers = angular.module('loginControllers', []);

loginControllers.controller('loginCtrl', function ($scope,$http,$location,$stomp,$log,DATA){
	$scope.connecter=function(username,isAdmin){
		$stomp
		.connect('/sp')
		.then(function (frame) {
		  $stomp.subscribe('/topic/new_user', function (payload, headers, res) {
		    if(payload.statusCode=="OK"){
		    	DATA.user.username=payload.body.username;
				DATA.user.isAdmin=payload.body.isAdmin;
				if(DATA.notExistant(DATA.listUsers,DATA.user.username)){
					DATA.listUsers.push(payload.body);
					$location.path('/home');       
				}else{
					console.log("choose an other username");
					$stomp.unsubscribe();
					$stomp.disconnect();
				}
		    }else{
		    	console.log(payload);
		    	$stomp.unsubscribe();
				$stomp.disconnect();
		    }
		  });
		  $stomp.send('/app/new_user', {
		    username: username,
		    isAdmin:isAdmin
		  });
		});
	}
});