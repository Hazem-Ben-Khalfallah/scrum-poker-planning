angular.module('userFactory', [])
    .factory('userFactory', ['$http', '$httpWrapper', '$localStorage', function ($http, $httpWrapper, $localStorage) {
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
                $httpWrapper.post(url, data, function (body, status, headers) {
                    var token = headers("jwt-token");
                    if (token) {
                        // store username and token in local storage to keep user logged in between page refreshes
                        $localStorage.currentUser = {
                            username: data.username,
                            sessionId: data.sessionId,
                            token: token
                        };
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + token;
                    }
                    onSuccess(body);
                }, onError);
            },
            disconnect: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/users/disconnect';
                $httpWrapper.post(url, data, function (body) {
                    // remove user from local storage and clear http auth header
                    delete $localStorage.currentUser;
                    $http.defaults.headers.common.Authorization = '';
                    onSuccess(body);
                }, onError);
            }
        };
    }]);