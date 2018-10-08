"use strict";
var storiesFilter = angular.module('storiesFilter', []);

storiesFilter.filter('storiesFilter', function () { // register new filter

    return function (stories, showEndedStories) { // filter arguments
        var filteredStories = [];
        for (var i = 0, len = stories.length; i < len; i++) {
            if (showEndedStories) {
                filteredStories.push(stories[i]);
            } else {
                if (!stories[i].ended) {
                    filteredStories.push(stories[i]);
                }
            }
        }
        return filteredStories; // implementation

    };
});