"use strict";
/*@ngInject*/
angular.module('$httpWrapper', [])
    .factory('$httpWrapper', function ($http, $localStorage, $location, $log) {

        function disconnectUser() {
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = '';
            $location.path('/static/login');
        }

        function getUrl(uri){
            var host = window.location.hostname;
            var port = window.location.port;
            if('${API_URL}'.indexOf('API_') < 0){
                host = '${API_URL}';
            }
            if('${API_PORT}'.indexOf('API_') < 0){
                port = '${API_PORT}';
            }
            return window.location.protocol + '//' + host + ':' + port + uri;
        }

        return {
            get: function (uri, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = getUrl(uri);

                if ($localStorage.currentUser && $localStorage.currentUser.token) {
                    // add jwt token to auth header for all requests made by the $http service
                    $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                }

                $http({url: url, method: "GET"})
                    .then(function (response) {
                        $log.info('[' + response.status + ']' + ' GET: ' + url + ' ', response.data);
                        onSuccess(response.data, response.status, response.headers);
                    }, function (response) {
                        $log.error('GET: ' + ' [' + response.status + '] ' + url);
                        if (response.status == 401) {
                            disconnectUser();
                        }
                        onError(response.data, response.status);
                    });
            },
            post: function (uri, params, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = getUrl(uri);

                if ($localStorage.currentUser && $localStorage.currentUser.token) {
                    // add jwt token to auth header for all requests made by the $http service
                    $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                }

                $http.post(url, angular.fromJson(params))
                    .then(function (response) {
                        $log.info('[' + response.status + ']' + ' POST: ' + url + ' ', response.data);
                        onSuccess(response.data, response.status, response.headers);
                    }, function (response) {
                        $log.error('POST ' + ' [' + response.status + '] ' + url + ' ', response.data);
                        if (response.status == 401) {
                            disconnectUser();
                        }
                        onError(response.data, response.status);
                    });
            },
            'delete': function (uri, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = getUrl(uri);

                if ($localStorage.currentUser && $localStorage.currentUser.token) {
                    // add jwt token to auth header for all requests made by the $http service
                    $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                }

                $http.delete(url)
                    .then(function (response) {
                        $log.info('[' + response.status + ']' + ' DELETE: ' + url + ' ', response.data);
                        onSuccess(response.data, response.status, response.headers);
                    }, function (response) {
                        $log.error('DELETE ' + ' [' + response.status + '] ' + url + ' ', response.data);
                        if (response.status == 401) {
                            disconnectUser();
                        }
                        onError(response.data, response.status);
                    });
            },
            put: function (uri, params, onSuccess, onError) {
                onSuccess = onSuccess || angular.noop;
                onError = onError || angular.noop;
                var url = getUrl(uri);

                if ($localStorage.currentUser && $localStorage.currentUser.token) {
                    // add jwt token to auth header for all requests made by the $http service
                    $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                }

                $http.put(url, angular.fromJson(params))
                    .then(function (response) {
                        $log.info('[' + response.status + ']' + ' PUT: ' + url + ' ', response.data);
                        onSuccess(response.data, response.status, response.headers);
                    }, function (response) {
                        $log.error('PUT ' + ' [' + response.status + '] ' + url + ' ', response.data);
                        if (response.status == 401) {
                            disconnectUser();
                        }
                        onError(response.data, response.status);
                    });
            }
        }
    });