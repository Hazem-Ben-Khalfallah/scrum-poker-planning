"use strict";
/*@ngInject*/
angular.module('userFactory', [])
    .factory('userFactory', function ($http, $httpWrapper, $localStorage) {
        return {
            get: function (onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users';
                $httpWrapper.get(url, onSuccess, onError);
            },
            connect: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users/connect';
                $httpWrapper.post(url, data, function (body, status, headers) {
                    var token = headers("jwt-token");
                    if (token) {
                        // store username and token in local storage to keep user logged in between page refreshes
                        $localStorage.currentUser = {
                            username: body.username,
                            sessionId: body.sessionId,
                            admin: body.admin,
                            token: token
                        };
                    }
                    onSuccess(body);
                }, onError);
            },
            disconnect: function (onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users/disconnect';
                $httpWrapper.post(url, {}, function (body) {
                    // remove user from local storage and clear http auth header
                    delete $localStorage.currentUser;
                    $http.defaults.headers.common.Authorization = '';
                    onSuccess(body);
                }, onError);
            },
            banUser: function (username, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users/ban/' + username;
                $httpWrapper.delete(url, onSuccess, onError);
            }
        };
    });