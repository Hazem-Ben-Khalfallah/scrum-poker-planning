angular.module('userFactory', [])
    .factory('userFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            get: function (sessionId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/users?sessionId=' + sessionId;
                $httpWrapper.get(url, onSuccess, null);
            },
            connect: function (data, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/users/connect';
                $httpWrapper.post(url, data, onSuccess, null);
            },
            disconnect: function (data, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/users/disconnect';
                $httpWrapper.post(url, data, onSuccess, null);
            }
        };
    }]);