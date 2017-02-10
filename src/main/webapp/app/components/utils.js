angular.module('$httpWrapper', [])
    .factory('$httpWrapper', ['$http', '$log',
        function ($http, $log) {
            return {
                get: function (url, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;

                    $http({url: url, method: "GET"})
                        .success(function (body, status, headers, httpResponse) {
                            $log.info('[' + status + ']' + ' GET: ' + url + ' ', angular.toJson(body));
                            onSuccess(body, status, headers);
                        })
                        .error(function (response) {
                            $log.error('GET: ' + url + ' ' + angular.toJson(response));
                            onError(response);
                        });
                },
                post: function (url, params, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;
                    $http.post(url, angular.fromJson(params))
                        .success(function (body, status, headers, httpResponse) {
                            $log.info('[' + status + ']' + ' POST: ' + url + ' ', angular.toJson(body));
                            onSuccess(body, status, headers);
                        })
                        .error(function (error) {
                            $log.error('Error: POST ' + url + ' ' + angular.toJson(error));
                            onError(error);
                        });
                },
                'delete': function (url, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;
                    $http.delete(url)
                        .success(function (body, status, headers, httpResponse) {
                            $log.info('[' + status + ']' + ' DELETE: ' + url + ' ', angular.toJson(body));
                            onSuccess(body, status, headers);
                        })
                        .error(function (error) {
                            $log.error('Error: DELETE ' + url + ' ' + angular.toJson(error));
                            onError(error);
                        });
                }
            }
        }
    ]);