'use strict';

angular.module('volunteercrowdApp')
	.controller('LegalEntityDeleteController', function($scope, $uibModalInstance, entity, LegalEntity) {

        $scope.legalEntity = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            LegalEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
