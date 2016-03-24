var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($stomp,$log,$scope,$sessionStorage,$location,DATA,Services){
	$scope.username=$sessionStorage.username;
	$scope.sessionId=$sessionStorage.sessionId;	
	$scope.isAdmin=$sessionStorage.isAdmin;	
	$scope.color=$sessionStorage.color;	
 	$scope.ticket_name="";
 	$scope.cardsMine=[];

	console.log($sessionStorage.username);
	if($sessionStorage.username===undefined || $sessionStorage.sessionId===undefined){
		$location.path('/login'); 
	}
	Services.subscribe("load_data",function (payload, headers, res) {
		//$scope.cardsMine= payload.body.myCards;
		console.log(payload.body);
		$scope.cardsChosen=payload.body.chosenCard;
		$scope.tickets=payload.body.tickets;
		if($scope.tickets.length>0){
			$scope.loadTicket($scope.tickets[0].name);
		}
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
 	 	Services.subscribe("create_ticket",function (payload, headers, res) {
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
    	 var cardsMine=[{idCard:1},{idCard:2},{idCard:3},{idCard:4},{idCard:5},{idCard:6},{idCard:7},{idCard:8},{idCard:9},{idCard:10},{idCard:11},{idCard:12}];
    	 for(var i=0;i<$scope.tickets.length;i++){
    		if($scope.tickets[i].name===name){
    	    	console.log($scope.tickets[i].name);
    	    	$scope.ticket_name=$scope.tickets[i].name;
    			$scope.cardsChosen=$scope.tickets[i].chosenCards;
    			var myIdCard=null;
    			for(var i=0;i<$scope.cardsChosen;i++){
    				if($scope.cardsChosen[i].user.username==$scope.username){
    					myIdCard=$scope.cardsChosen[i].idCard;
    					var index = array.indexOf({idCard:myIdCard});
    					if (index > -1) {
    						cardsMine.splice(index, 1);
    					}
    					
    					break;
    				}
    			}   			
    		}
    	 }
    	 $scope.cardsMine=cardsMine;
     }; 
          
     $scope.getCardValue = function(idCard){
    	 console.log(idCard)
		 return DATA.cards[idCard]
     }; 
     
     $scope.logout=function(){
 		$sessionStorage.$reset();
	    $stomp.disconnect();
 		$location.path('/login'); 
 	}
});



