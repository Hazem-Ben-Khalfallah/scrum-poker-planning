angular.module('homeFactory', [])
    .factory('homeFactory', ['$http', function ($http) {
        return {
            get: function (sessionId, callback) {
                $http({
                    method: 'GET',
                    url: '/session/' + sessionId,
                    cache: true
                }).success(callback);
            }
        };
    }]);