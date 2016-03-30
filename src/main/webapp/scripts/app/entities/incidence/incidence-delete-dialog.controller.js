'use strict';

angular.module('volunteercrowdApp')
	.controller('IncidenceDeleteController', function($scope, $uibModalInstance, entity, Incidence) {

        $scope.incidence = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Incidence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
