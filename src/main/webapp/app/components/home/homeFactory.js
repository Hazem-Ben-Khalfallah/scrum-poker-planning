angular.module('homeFactory', [])
    .factory('homeFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            get: function (sessionId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/session/' + sessionId;
                $httpWrapper.get(url, onSuccess, null);
            }
        };
    }]);