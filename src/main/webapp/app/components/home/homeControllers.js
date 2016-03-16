var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($stomp,$log,$scope,DATA){
	if(DATA.websocket==null){
		var websocket = $stomp.connect('/sp')
		.then(function (frame) {
			DATA.websocket=websocket;
		  $stomp.subscribe('/topic/new_user', function (payload, headers, res) {
			  console.log(payload.statusCode);
		    if(payload.statusCode=="OK"){
				if(DATA.notExistant(DATA.listUsers,DATA.user.username)){
					DATA.listUsers.push(payload.body);
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
		    username: DATA.user.username,
		    isAdmin:DATA.user.isAdmin
		  });
		});
	}
	
	
	$scope.cardsMine=['Jani','Hege','Kai','Jani1','Hege1','Kai1','Jani2','Hege2','Kai2'];
	$scope.cardsChosen=[];
	 $scope.dropSuccessHandler = function($event,$data,index,type){
		 if(type==='chosen'){
			 $scope.cardsMine.splice(index,1);
			 $scope.cardsChosen.push($data);
		 }else{
			 $scope.cardsChosen.splice(index,1);
			 $scope.cardsMine.push($data);
		 }
     };

     $scope.onDrop = function($event,$data,array){
         //array.push($data);
     };
	
	/*$scope.onDrop = function(target, source){

	    alert("dropped " + source + " on " + target);

	  };

	  

	  $scope.dropValidate = function(target, source) {

	    return target !== source;

	  };*/
});



