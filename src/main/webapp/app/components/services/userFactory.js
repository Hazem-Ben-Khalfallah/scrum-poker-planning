angular.module('userFactory', [])
    .factory('userFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            get: function (sessionId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/users?sessionId=' + sessionId;
                $httpWrapper.get(url, onSuccess, null);
            }
        };
    }]);