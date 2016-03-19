var homeControllers = angular.module('homeControllers', []);

homeControllers.controller('homeCtrl', function ($stomp,$log,$scope,$sessionStorage,$location,DATA,Services){
	$scope.username=$sessionStorage.username;
	$scope.sessionId=$sessionStorage.sessionId;	

	Services.subscribe("new_user",function (payload, headers, res) {
		$scope.cardsMine= payload.body.myCards;
		console.log(payload.body.myCards);
		$scope.cardsChosen=payload.body.chosenCard;
		$scope.$apply();
	},{
		username:$sessionStorage.username,
		sessionId:$sessionStorage.sessionId
	});
		
	$scope.tickets=["WEB-1","WEB-2","WEB-3","WEB-4","WEB-5","WEB-6","WEB-7","WEB-8"]
	
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
     
     $scope.getCardValue = function(idCard){
		 return DATA.cards[idCard]
     }; 
     
     $scope.logout=function(){
 		$sessionStorage.$reset();
	    $stomp.disconnect();
 		$location.path('/login'); 
 	}
});



