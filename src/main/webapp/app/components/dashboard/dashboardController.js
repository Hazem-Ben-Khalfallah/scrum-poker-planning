"use strict";
var dashboardController = angular.module('dashboardController', []);

/*@ngInject*/
dashboardController.controller('dashboardCtrl', function ($scope, $location, $localStorage, $log, sessionFactory) {
    function init() {
        $scope.show_modal = false;
        $scope.stories = [];
        $scope.cardSet = 'time';
        $scope.cardTheme = 'none';
        $scope.storyNamePrefix = '';
    }

    $scope.convertToStories = function () {
        $scope.stories = [];
        var sanitizedValue;
        if ($scope.rowStories) {
            var values = $scope.rowStories
                .replace("\r\n", "\n")
                .split("\n");
            angular.forEach(values, function (value) {
                sanitizedValue = $scope.storyNamePrefix + value.trim();
                if (sanitizedValue.length > 0) {
                    if ($scope.stories.indexOf(sanitizedValue) < 0) {
                        $scope.stories.push(sanitizedValue);
                    }
                }
            });
        }
    };

    $scope.remove = function (story) {
        var index = $scope.stories.indexOf(story);
        if (index >= 0) {
            $scope.stories.splice(index, 1);
        }
    };

    $scope.cancel = function () {
        $location.path('/static/login');
    };

    $scope.openModal = function () {
        $scope.show_modal = true;
    };

    $scope.closeModal = function () {
        $scope.show_modal = false;
    };

    $scope.save = function (username) {
        if (!username) {
            return;
        }

        var data = {
            username: username,
            storyNamePrefix: $scope.storyNamePrefix,
            cardSet: $scope.cardSet,
            cardTheme: $scope.cardTheme,
            stories: $scope.stories
        };

        sessionFactory.create(data, function (response) {
            $scope.closeModal();
            $location.path('/static/home/' + $localStorage.currentUser.sessionId);
        });
    };

    init();
});