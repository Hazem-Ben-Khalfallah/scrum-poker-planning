angular.module('voteFactory', [])
    .factory('voteFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            get: function (storyId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/votes?storyId=' + storyId;
                $httpWrapper.get(url, onSuccess, null);
            },
            remove: function (voteId, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/votes/' + voteId;
                $httpWrapper.delete(url, onSuccess, null);
            },
            create: function (data, onSuccess) {
                onSuccess = onSuccess || angular.noop;
                var url = '/votes';
                $httpWrapper.post(url, data, onSuccess, null);
            }
        };
    }]);