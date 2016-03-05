angular.module('homeFactory', [])
       .factory('countries', function($http){
  return {
    list: function (callback){
      $http({
        method: 'GET',
        url: 'app/components/dataset/countries.json',
        cache: true
      }).success(callback);
    },
    find: function(id, callback){
      $http({
        method: 'GET',
        url: 'app/components/dataset/country_' + id + '.json',
        cache: true
      }).success(callback);
    }
  };
});