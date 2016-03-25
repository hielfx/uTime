'use strict';

angular.module('volunteercrowdApp')
    .controller('LegalEntityDetailController', function ($scope, $rootScope, $stateParams, entity, LegalEntity, AppUser) {
        $scope.legalEntity = entity;
        $scope.load = function (id) {
            LegalEntity.get({id: id}, function(result) {
                $scope.legalEntity = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:legalEntityUpdate', function(event, result) {
            $scope.legalEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
