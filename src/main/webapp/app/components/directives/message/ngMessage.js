"use strict";
var ngMessage = angular.module('ngMessage', []);

ngMessage.directive('ngMessage', function () {
    /*@ngInject*/
    var messageController = function ($scope, $timeout) {

        $scope.hideMassage = function () {
            $scope.visible = false;
            $timeout.cancel($scope.errorCountDown);
        };

        $scope.$watch('visible', function () {
            if ($scope.visible == true || $scope.visible == "true") {
                $scope.showMassage($scope.message);
            } else {
                $scope.visible = false;
            }
        });

        $scope.showMassage = function (message) {
            $scope.message = message;
            $timeout.cancel($scope.errorCountDown);
            hideMessageAfter(5);// seconds
            $scope.visible = true;
        };

        function hideMessageAfter(seconds) {
            $scope.messageCountDown = seconds;
            $scope.errorCountDown = $timeout(function () {
                if (seconds == 0) {
                    $scope.hideMassage();
                } else {
                    hideMessageAfter(seconds - 1);
                }
            }, 1000);
        }
    };
    return {
        restrict: "E",
        scope: {
            message: "@",
            visible: "@"
        },
        controller: messageController,
        template: TEMPLATES["app/components/directives/message/message.html"]
    };
});