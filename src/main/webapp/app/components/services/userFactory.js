angular.module('userFactory', [])
    .factory('userFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            get: function (sessionId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users?sessionId=' + sessionId;
                $httpWrapper.get(url, onSuccess, onError);
            },
            connect: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users/connect';
                $httpWrapper.post(url, data, onSuccess, onError);
            },
            disconnect: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users/disconnect';
                $httpWrapper.post(url, data, onSuccess, onError);
            }
        };
    }]);