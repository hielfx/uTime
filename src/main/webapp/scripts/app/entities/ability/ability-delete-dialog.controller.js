'use strict';

angular.module('volunteercrowdApp')
	.controller('AbilityDeleteController', function($scope, $uibModalInstance, entity, Ability) {

        $scope.ability = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Ability.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
