'use strict';

angular.module('volunteercrowdApp')
    .controller('AbilityDetailController', function ($scope, $rootScope, $stateParams, entity, Ability, Tag, AppUser) {
        $scope.ability = entity;
        $scope.load = function (id) {
            Ability.get({id: id}, function(result) {
                $scope.ability = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:abilityUpdate', function(event, result) {
            $scope.ability = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
