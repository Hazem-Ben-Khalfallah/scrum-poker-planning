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
	
	$scope.tickets=["WEB-1","WEB-2","WEB-3","WEB-4","WEB-5","WEB-6"]
	$scope.cardsMine=[
	                  	  {
	                	    "value": "0",
	                	    "id": 1
	                	  },
	                	  {
	                	    "value": "1/2",
	                	    "id": 2
	                	  },
	                	  {
	                	    "value": "1",
	                	    "id": 3
	                	  },
	                	  {
	                	    "value": "2",
	                	    "id": 4
	                	  },
	                	  {
	                	    "value": "3",
	                	    "id": 5
	                	  },
	                	  {
	                	    "value": "5",
	                	    "id": 6
	                	  },
	                	  {
	                	    "value": "8",
	                	    "id": 7
	                	  },
	                	  {
	                	    "value": "13",
	                	    "id": 8
	                	  },
	                	  {
	                	    "value": "20",
	                	    "id": 9
	                	  },
	                	  {
	                	    "value": "40",
	                	    "id": 10
	                	  },
	                	  {
	                	    "value": "100",
	                	    "id": 11
	                	  },
	                	  {
	                	    "value": "?",
	                	    "id": 12
	                	  }
	                	];
	 $scope.cardsChosen=[];
	 $scope.dropSuccessHandler = function($event,index,type,card){
		 if(type==='from_chosen'){
			 console.log("adding to mine");
			 $scope.cardsChosen.splice(index,1);
			 console.log(card);
			 $scope.cardsMine.push(card);
		 }else if(type==='from_mine'){
			 console.log("adding to chosen")
			 $scope.cardsMine.splice(index,1);
			 console.log(card);
			 $scope.cardsChosen.push(card);
		 }else{
			 console.log(type);
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



