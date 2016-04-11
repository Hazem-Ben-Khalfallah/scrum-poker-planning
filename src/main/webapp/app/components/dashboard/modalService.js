'use strict';
angular.module('angularify.semantic.modal', [])

    .directive('modal', function () {
        return {
            restrict: 'E',
            replace: true,
            transclude: true,
            require: 'ngModel',
            scope: {
                username: '='
            },
            template: '<div class="ui basic modal" ng-transclude></div>',
            link: function (scope, element, attrs, ngModel) {
                element.modal({
                    onHide: function () {
                        ngModel.$setViewValue(false);
                    }
                });
                scope.$watch(function () {
                    return ngModel.$modelValue;
                }, function (modelValue) {
                    element.modal(modelValue ? 'show' : 'hide');
                });
                scope.$on('$destroy', function () {
                    element.modal('hide');
                    element.remove();
                });
            }
        }
    });