angular.module('$httpWrapper', [])
    .factory('$httpWrapper', ['$http', '$localStorage', '$location', '$log',
        function ($http, $localStorage, $location, $log) {
            function disconnectUser() {
                delete $localStorage.currentUser;
                $location.path('/login');
            }

            return {
                get: function (url, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;

                    if ($localStorage.currentUser && $localStorage.currentUser.token) {
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                    }

                    $http({url: url, method: "GET"})
                        .success(function (body, status, headers, httpResponse) {
                            $log.info('[' + status + ']' + ' GET: ' + url + ' ', angular.toJson(body));
                            onSuccess(body, status, headers);
                        })
                        .error(function (body, status, headers, httpResponse) {
                            $log.error('GET: ' + ' [' + status + '] ' + url);
                            if (status == 401) {
                                disconnectUser();
                            }
                            onError(body, status);
                        });
                },
                post: function (url, params, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;

                    if ($localStorage.currentUser && $localStorage.currentUser.token) {
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                    }

                    $http.post(url, angular.fromJson(params))
                        .success(function (body, status, headers, httpResponse) {
                            $log.info('[' + status + ']' + ' POST: ' + url + ' ', angular.toJson(body));
                            onSuccess(body, status, headers);
                        })
                        .error(function (body, status, headers, httpResponse) {
                            $log.error('POST ' + ' [' + status + '] ' + url + ' ' + angular.toJson(body));
                            if (status == 401) {
                                disconnectUser();
                            }
                            onError(body, status);
                        });
                },
                'delete': function (url, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;

                    if ($localStorage.currentUser && $localStorage.currentUser.token) {
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
                    }

                    $http.delete(url)
                        .success(function (body, status, headers, httpResponse) {
                            $log.info('[' + status + ']' + ' DELETE: ' + url + ' ', angular.toJson(body));
                            onSuccess(body, status, headers);
                        })
                        .error(function (body, status, headers, httpResponse) {
                            $log.error('DELETE ' + ' [' + status + '] ' + url + ' ' + angular.toJson(body));
                            if (status == 401) {
                                disconnectUser();
                            }
                            onError(body, status);
                        });
                }
            }
        }
    ]);