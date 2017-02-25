"use strict";
/*@ngInject*/
angular.module('voteFactory', [])
    .factory('voteFactory', function ($httpWrapper) {
        return {
            get: function (storyId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/votes?storyId=' + storyId;
                $httpWrapper.get(url, onSuccess, onError);
            },
            remove: function (voteId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/votes/' + voteId;
                $httpWrapper.delete(url, onSuccess, onError);
            },
            create: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/votes';
                $httpWrapper.post(url, data, onSuccess, onError);
            }
        };
    });