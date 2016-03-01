(function() {
	'use strict';

	/* Init application */
	angular
		.module('app', ['ngAnimate', 'ngRoute', 'ngSanitize', 'mgcrea.ngStrap']);



	/* Common application conroller */
	angular
		.module('app')
		.controller('ApplicationCtrl', ApplicationCtrl);

	ApplicationCtrl.$inject = ['$window', 'ConfService'];
	function ApplicationCtrl($window, ConfService) {

		var vm = this;

		vm.projectAuthor 	= null;
		vm.projectName		= null;

		vm.loadConfig = function() {
      ConfService
				.initConfig()
				.error(function() {
					vm.projectAuthor = vm.projectName = 'Error!';
				})
				.then(function(res) {
					vm.projectAuthor 	= res.data.projectAuthor 	|| 'Unknown';
					vm.projectName 		= res.data.projectName 		|| 'Unknown';
					vm.projectWebsite	= res.data.projectWebsite 	|| 'Unknown';
				});
		};

		vm.loadConfig();

		return vm;
	}
})();


(function() {
	'use strict';

	angular
		.module('app')
		.directive('sampleDirective', 	sampleDirective);
	
	function sampleDirective() {
	  return function(scope, element, attrs) {
	    scope.$watch(attrs.inputDisabled, function(val) {
	    	if (val === undefined)
	    		element.prop('disabled', false);
	    	else
	    		element.prop('disabled', true);
	    });
	  };
	}
	
})();
(function() {
	'use strict';

	angular
		.module('app')
		.controller('MainCtrl', MainCtrl);

	MainCtrl.$inject = ['$window', 'MainService'];
	function MainCtrl($window, MainService) {

		var vm = this;

		vm.viewLocation = 'webapp/templates/main/main.html';

		return vm;
	}

})();
(function() {
	'use strict';

	angular
		.module('app')
		.service('MainService', MainService);

	MainService.$inject = ['$http'];
	function MainService($http) {

		var service = {};

		return service;
	}
	
})();
(function() {
	'use strict';
	
	angular
		.module('app')
		.service('ConfService', ConfService);

	ConfService.$inject = ['$http'];
	function ConfService($http) {
		
		var service 		= {};

		service.initConfig	= function() {
			return $http.get('config');
		};
		
		return service;
	}

})();
(function() {
    'use strict';
    
	angular
		.module('app')
		.config(Routes);
	
	Routes.$inject = ['$routeProvider'];
    function Routes($routeProvider) {

    	$routeProvider.
	    	 when('/', {
	    	   templateUrl: 'templates/main/main.html',
	    	   controller:	'MainCtrl',
	           controllerAs: 'main'
	       	 }).
	         otherwise({
	           redirectTo: '/'
	         });
    }
    
})();