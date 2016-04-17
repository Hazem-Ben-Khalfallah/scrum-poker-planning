angular.module('sessionFactory', [])
    .factory('sessionFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            create: function (data, onSuccess) {
                $httpWrapper.post('/sessions',
                    data,
                    onSuccess || angular.noop());
            },
            get: function (sessionId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/sessions/' + sessionId;
                $httpWrapper.get(url, onSuccess, null);
            }
        };
    }]);