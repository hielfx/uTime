'use strict';

angular.module('volunteercrowdApp')
	.controller('NeededAbilityDeleteController', function($scope, $uibModalInstance, entity, NeededAbility) {

        $scope.neededAbility = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            NeededAbility.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
