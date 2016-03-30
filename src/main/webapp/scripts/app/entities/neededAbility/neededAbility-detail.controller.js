'use strict';

angular.module('volunteercrowdApp')
    .controller('NeededAbilityDetailController', function ($scope, $rootScope, $stateParams, entity, NeededAbility, Need) {
        $scope.neededAbility = entity;
        $scope.load = function (id) {
            NeededAbility.get({id: id}, function(result) {
                $scope.neededAbility = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:neededAbilityUpdate', function(event, result) {
            $scope.neededAbility = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
