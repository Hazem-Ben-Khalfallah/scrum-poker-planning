angular.module('loginFactory', [])
       .factory('login', function($http){
  return {
    list: function (callback){
      $http({
        method: 'GET',
        url: 'app/components/dataset/user.json',
        cache: true
      }).success(callback);
    }
  };
});