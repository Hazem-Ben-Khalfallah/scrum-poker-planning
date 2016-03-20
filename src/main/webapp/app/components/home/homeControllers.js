var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($stomp,$log,$scope,$sessionStorage,$location,DATA,Services){
	$scope.username=$sessionStorage.username;
	$scope.sessionId=$sessionStorage.sessionId;	
 	$scope.ticket_name="";

	console.log($sessionStorage.username);
	if($sessionStorage.username===undefined || $sessionStorage.sessionId===undefined){
		$location.path('/login'); 
	}
	Services.subscribe("new_user",function (payload, headers, res) {
		//$scope.cardsMine= payload.body.myCards;
		console.log(payload.body);
		//$scope.cardsChosen=payload.body.chosenCard;
		$scope.tickets=payload.body.tickets;
		$scope.$apply();
	},{
		username:$sessionStorage.username,
		sessionId:$sessionStorage.sessionId
	});
		
	
	
     $scope.removeFromChosen = function(index,card){
		 $scope.cardsChosen.splice(index,1);
		 $scope.cardsMine.push(card);
		 console.log("-----------------------------");
		 console.log("card: "+card.isMine);
		 console.log("remove chosen: "+$scope.cardsChosen.length);
		 console.log("remove chosen: "+$scope.cardsMine.length);
	 }; 
     
     $scope.addToChosen = function(index,card){
    	 if($scope.cardsMine.length>=12){
    		$scope.cardsMine.splice(index,1);
    		$scope.cardsChosen.push(card);
    	 }
    	 console.log("-----------------------------");
    	 console.log("card: "+card.isMine);
		 console.log("add chosen: "+$scope.cardsChosen.length);
		 console.log("add chosen: "+$scope.cardsMine.length);
     }; 
     
 	 $scope.newTicket = function(ticket_name){
 	 	Services.subscribe("new_ticket",function (payload, headers, res) {
 			//$scope.cardsMine= payload.body.myCards;
 			console.log(payload);
 			//$scope.cardsChosen=payload.body.chosenCard;
 			$scope.tickets=payload.body.tickets;
 			$scope.$apply();
 		},{
 			name:ticket_name,
 			sessionId:$sessionStorage.sessionId
 		});
     }; 
 	
     $scope.loadTicket = function(name){
    	 console.log("-------")
    	 for(var i=0;i<$scope.tickets.length;i++){
    		if($scope.tickets[i].name===name){
    	    	 console.log($scope.tickets[i].name);
    	    	$scope.ticket_name=$scope.tickets[i].name;
    			$scope.cardsChosen=$scope.tickets[i].chosenCards;
    		}
    	 }
     }; 
          
     $scope.getCardValue = function(idCard){
		 return DATA.cards[idCard]
     }; 
     
     $scope.logout=function(){
 		$sessionStorage.$reset();
	    $stomp.disconnect();
 		$location.path('/login'); 
 	}
});



