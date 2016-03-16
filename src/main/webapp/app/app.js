var myApp = angular.module('scrumPokApp', [
  'ngRoute',
  'homeControllers',
  'homeFactory',
  'homeDirective',
  'loginControllers',
  'loginFactory',
  'ui.bootstrap',
  'ang-drag-drop',
  'ngStomp'
]);

myApp.config(function($routeProvider) {
	  $routeProvider.
	    when('/home', {
	      templateUrl: 'app/components/home/home.html',
	    }).
	    when('/login', {
	        templateUrl: 'app/components/login/login.html'
	    }).
	    otherwise({
	      redirectTo: '/login'
	    });
});

myApp.factory('Services',function($tomp,$log){
	$stomp.setDebug(function (args) {
	    $log.debug(args)
	  });
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
	    }
	  };
});


