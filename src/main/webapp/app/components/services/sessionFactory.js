angular.module('sessionFactory', [])
    .factory('sessionFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            create: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                $httpWrapper.post('/sessions', data, onSuccess, onError);
            },
            get: function (sessionId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/sessions/' + sessionId;
                $httpWrapper.get(url, onSuccess, onError);
            }
        };
    }]);