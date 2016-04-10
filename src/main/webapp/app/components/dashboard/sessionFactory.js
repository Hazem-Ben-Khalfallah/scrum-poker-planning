angular.module('sessionFactory', [])
    .factory('sessionFactory', ['$httpWrapper', function ($httpWrapper) {
        return {
            create: function (data, onSuccess) {
                $httpWrapper.post('/session',
                    data,
                    onSuccess || angular.noop());
            }
        };
    }]);