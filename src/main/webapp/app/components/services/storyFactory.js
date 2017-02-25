"use strict";
/*@ngInject*/
angular.module('storyFactory', [])
    .factory('storyFactory', function ($httpWrapper) {
        return {
            get: function (onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/stories';
                $httpWrapper.get(url, onSuccess, onError);
            },
            remove: function (storyId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/stories/' + storyId;
                $httpWrapper.delete(url, onSuccess, onError);
            },
            endStory: function (storyId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/stories/' + storyId;
                $httpWrapper.post(url, null, onSuccess, onError);
            },
            create: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/stories';
                $httpWrapper.post(url, data, onSuccess, onError);
            }
        };
    });