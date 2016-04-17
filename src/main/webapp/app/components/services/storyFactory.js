angular.module('storyFactory', [])
    .factory('storyFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            get: function (sessionId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/stories?sessionId=' + sessionId;
                $httpWrapper.get(url, onSuccess, null);
            },
            remove: function (storyId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/stories/' + storyId;
                $httpWrapper.delete(url, onSuccess, null);
            },
            create: function (data, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/stories';
                $httpWrapper.post(url, data, onSuccess, null);
            }
        };
    }]);