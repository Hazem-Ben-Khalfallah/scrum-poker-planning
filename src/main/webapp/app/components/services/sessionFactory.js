angular.module('sessionFactory', [])
    .factory('sessionFactory', ['$http', '$httpWrapper', '$localStorage', function ($http, $httpWrapper, $localStorage) {
        return {
            create: function (data, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                $httpWrapper.post('/sessions', data, function (body, status, headers) {
                    var token = headers("jwt-token");
                    if (token) {
                        // store username and token in local storage to keep user logged in between page refreshes
                        $localStorage.currentUser = {
                            username: body.username,
                            sessionId: body.sessionId,
                            admin: true,
                            token: token
                        };
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + token;
                    }
                    onSuccess(body);
                }, onError);
            },
            get: function (sessionId, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = '/sessions/' + sessionId;
                $httpWrapper.get(url, onSuccess, onError);
            }
        };
    }]);