'use strict';

angular.module('volunteercrowdApp')
    .controller('NeedDetailController', function ($scope, $rootScope, $stateParams, entity, Need, AppUser, NeededAbility, Disponibility) {
        $scope.need = entity;
        $scope.load = function (id) {
            Need.get({id: id}, function(result) {
                $scope.need = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:needUpdate', function(event, result) {
            $scope.need = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
