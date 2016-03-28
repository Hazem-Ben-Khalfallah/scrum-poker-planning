var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($http,$stomp,$log,$scope,$sessionStorage,$location,DATA,Services){
	$scope.username=$sessionStorage.username;
	$scope.sessionId=$sessionStorage.sessionId;	
	$scope.isAdmin=$sessionStorage.isAdmin;	
	$scope.color=$sessionStorage.color;	
 	$scope.ticketName="";
 	$scope.cardsMine=[];
 	$scope.tickets=[];

	console.log($sessionStorage.username);
	if($sessionStorage.username===undefined || $sessionStorage.sessionId===undefined){
		$location.path('/login'); 
	}
	/*Services.subscribe("load_tickets",function (payload, headers, res) {
		$scope.tickets=payload.body;
		if($scope.tickets.length>0){
			$scope.loadTicket($scope.tickets[0].ticketName);
		}
			//$scope.$apply();
	},{
		sessionId:$sessionStorage.sessionId
	});*/
	
	Services.subscribe("load_data",function (payload, headers, res) {
		console.log("-------------");
		console.log(payload);
		if(payload.statusCode=="OK"){
			$scope.tickets=payload.body.tickets;
		}

			$scope.$apply();
	});
	
	/*$scope.loadTicket = function(ticketName){
		console.log(ticketName)
		Services.subscribe("load_cards",function (payload, headers, res) {
			update(payload)
		},{
 			ticketName:ticketName,
 			sessionId:$sessionStorage.sessionId
 		});
    
    	 $scope.cardsMine=cardsMine;
     }; 
     
     function update(data){
 		console.log(data);
 			$scope.cardsChosen=data.body;
 			var cardsMine=[{idCard:1,color:$scope.color,username:$scope.username},{idCard:2,color:$scope.color,username:$scope.username},{idCard:3,color:$scope.color,username:$scope.username},{idCard:4,color:$scope.color,username:$scope.username},{idCard:5,color:$scope.color,username:$scope.username},{idCard:6,color:$scope.color,username:$scope.username},{idCard:7,color:$scope.color,username:$scope.username},{idCard:8,color:$scope.color,username:$scope.username},{idCard:9,color:$scope.color,username:$scope.username},{idCard:10,color:$scope.color,username:$scope.username},{idCard:11,color:$scope.color,username:$scope.username},{idCard:12,color:$scope.color,username:$scope.username}];
 			for(var i=0;i<$scope.cardsChosen.length;i++){
 			console.log($scope.cardsChosen[i].username+"-"+$scope.username);
 			if($scope.cardsChosen[i].username==$scope.username){
 				myIdCard=$scope.cardsChosen[i].idCard;
 				var index = -1;
 				for(var j=0;j<cardsMine.length;j++){
 					console.log(myIdCard+"#"+cardsMine[j].idCard);
 					if(cardsMine[j].idCard==myIdCard){
 						index=j;
 						break;
 					}
 				}
 				if (index > -1) {
 					cardsMine.splice(index, 1);
 				}
 				break;
 			}
 		}
 			$scope.cardsMine=cardsMine
 			//$scope.ticketName=ticketName;
 			$scope.$apply();
     }
     
     /*	 /*$http({
    		  method: 'GET', 
    		  url: '/load_cards_mine',
    		  data: {sessionId:$sessionStorage.sessionId,username:$sessionStorage.username}
    		}).then(function(resp) {
    		  console.log(resp.data);
    		});*/
    	 /*$http.post("/load_cards_mine",{'sessionId':$sessionStorage.sessionId,'username':$sessionStorage.username}).success(function (response){
    		      console.log('in success method');
    		      console.log(response);
    		  }).error(function(data, status) {
    			  console.log('in ERROR method');
    	  });*/
    	 /*var cardsMine=[{idCard:1},{idCard:2},{idCard:3},{idCard:4},{idCard:5},{idCard:6},{idCard:7},{idCard:8},{idCard:9},{idCard:10},{idCard:11},{idCard:12}];
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
    	 }*/
		
     $scope.removeFromChosen = function(index,card){
		console.log("-----------------------------");
		console.log("remove from chosen: "+card);
		console.log("chosen: "+$scope.cardsChosen.length);
		console.log("mine: "+$scope.cardsMine.length);
		Services.sub_send("remove_card",function (payload, headers, res) {
			/*if(payload.statusCode=="OK"){
				$scope.cardsChosen.splice(index,1);
				$scope.cardsMine.push(card);
				$scope.$apply();
			}else{
				console.log("ERROR REMOVE: "+card.idCard)
			}*/
		},{
			sessionId:$sessionStorage.sessionId,
			username:$sessionStorage.username,
			ticketName:$scope.ticketName,
			idCard:card.idCard
		});	 
	 }; 
     
     $scope.addToChosen = function(index,card){
    	 console.log("-----------------------------");
    	 console.log("add to chosen: "+card);
		 console.log("chosen: "+$scope.cardsChosen.length);
		 console.log("mine: "+$scope.cardsMine.length);
    	 Services.sub_send("add_card",function (payload, headers, res) {
				console.log(payload.body);
 			/*if(payload.statusCode=="OK"){
		    	$scope.cardsMine.splice(index,1);
 		    	$scope.cardsChosen.push(card);
 				$scope.$apply();
 			}else{
 				console.log("ERROR ADD: "+card.idCard)
 			}*/
 		},{
 			sessionId:$sessionStorage.sessionId,
 			username:$sessionStorage.username,
 			ticketName:$scope.ticketName,
 			idCard:card.idCard,
 			color:$sessionStorage.color
 		});
     }; 
     
 	 $scope.newTicket = function(tName){
 	 	Services.sub_send("create_ticket",function (payload, headers, res) {
 	 		console.log(payload.body);
 			//$scope.tickets.push(payload.body);
 			//$scope.loadTicket(tName);
 			//$scope.$apply();
 		},{
 			ticketName:tName,
 			sessionId:$sessionStorage.sessionId
 		});
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



