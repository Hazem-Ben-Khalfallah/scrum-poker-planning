angular.module('$httpWrapper', [])
    .factory('$httpWrapper', ['$http', '$log',
        function ($http, $log) {
            return {
                get: function (url, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;

                    $http({
                        url: url,
                        method: "GET"
                    }).success(function (response) {
                        $log.info('GET: ' + url + ' ' + angular.toJson(response));
                        onSuccess(response);
                    }).error(function (response) {
                        $log.error('GET: ' + url + ' ' + angular.toJson(response));
                        onError(response);
                    });
                },
                post: function (url, params, onSuccess, onError) {
                    onSuccess = onSuccess || angular.noop;
                    onError = onError || angular.noop;
                    $http.post(
                        url,
                        angular.fromJson(params))
                        .success(function (response) {
                            $log.info('POST: ' + url + ' ' + angular.toJson(response));
                            onSuccess(response);
                        })
                        .error(function (error) {
                            $log.error('Error: POST ' + url + ' ' + angular.toJson(error));
                            onError(error);
                        });
                },
                'delete': function (url, onSuccess) {
                    onSuccess = onSuccess || angular.noop;
                    $http.delete(
                        url
                    ).success(function (response) {
                            $log.info('DELETE: ' + url + ' ' + angular.toJson(response));
                            onSuccess(response);
                        });
                }
            }
        }
    ]);