'use strict';

angular.module('volunteercrowdApp')
    .controller('TagDetailController', function ($scope, $rootScope, $stateParams, entity, Tag, Ability) {
        $scope.tag = entity;
        $scope.load = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:tagUpdate', function(event, result) {
            $scope.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
