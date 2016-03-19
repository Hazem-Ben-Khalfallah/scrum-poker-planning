var myApp = angular.module('scrumPokApp', [
  'ngRoute',
  'homeControllers',
  'homeFactory',
  'homeDirective',
  'loginControllers',
  'loginFactory',
  'ui.bootstrap',
  'ang-drag-drop',
  'ngStomp',
  'ngStorage'
]);

myApp.config(function($routeProvider) {
	  $routeProvider.
	    when('/home/:sessioId', {
	      templateUrl: 'app/components/home/home.html',
	    }).
	    when('/login', {
	        templateUrl: 'app/components/login/login.html'
	    }).
	    otherwise({
	      redirectTo: '/login'
	    });
	  
	
});

myApp.factory('Services',function($stomp,$log){
	var sub = function (topic,callback,data){
		if($stomp.stomp!=null){
			$stomp.subscribe('/topic/'+topic,callback);
			$stomp.send('/app/'+topic, data);
		}else{
			 $stomp.connect('/sp')
			  .then(function (frame) {
				  $stomp.setDebug(function (args) {
					  $log.debug(args)
				   });
				  sub(topic,callback,data);
			  });
		}
	};
	return {
		subscribe : sub
	}	
});

myApp.factory('DATA', function(){
	  return {
		websocket:null,
	    user: {
	      username: '',
	      isAdmin:false
	    },
	    listUsers:[],
	    notExistant:function(liste,user){
	    	for(var i=0;i<liste;i++){
	    		if(liste[i].username===user){
	    			return false;
	    		}
	    	}
	    	return true;
	    },
	    cards : {
	    		"1":"0",
	    		"2":"1/2",
	    		"3":"1",
	    		"4":"2",
	    		"5":"3",
	    		"6":"5",
	    		"7":"8",
	    		"8":"13",
	    		"9":"20",
	    		"10":"40",
	    		"11":"100",
	    		"12":"?",
	    }
	  };
});




